package com.magestore.app.pos.api.m1.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundCreditParams;
import com.magestore.app.lib.model.sales.OrderRefundGiftCard;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderTakePaymentParam;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListOrder;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 8/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSOrderDataAccessM1 extends POSAbstractDataAccessM1 implements OrderDataAccess {
    public class Gson2PosOrderParseModel extends Gson2PosAbstractParseImplement {
        @Override
        public Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<PosOrder>() {
            }
                    .getType(), new OrderParamsConverter());
            builder.registerTypeAdapter(new TypeToken<PosCartItem.OptionsValue>() {
            }
                    .getType(), new ReOrderParamsConverter());
            return builder.create();
        }

        public class ReOrderParamsConverter implements JsonDeserializer<PosCartItem.OptionsValue> {
            public PosCartItem.OptionsValue deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
                // đọc Gson với object, bỏ qua value
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                Gson gson = builder.create();
                PosCartItem.OptionsValue decode = gson.fromJson(arg0, PosCartItem.OptionsValue.class);

                // xét xem value là mảng hay String
                List<String> values = null;
                JsonObject decodeObj = arg0.getAsJsonObject();
                if (decodeObj.get("value").isJsonArray()) {
                    values = gson.fromJson(decodeObj.get("value"), new TypeToken<List<String>>() {
                    }.getType());
                } else {
                    String single = gson.fromJson(decodeObj.get("value"), String.class);
                    values = new ArrayList<String>();
                    values.add(single);
                    decode.value = single;
                }
                decode.id = gson.fromJson(decodeObj.get("id"), String.class);
                decode.values = values;
                if (values != null && values.size() > 0) decode.value = null;
                return decode;
            }
        }

        private static final String JSON_EXTENSION_PAYENT = "payment";
        private static final String JSON_EXTENSION_BILLING_ADDRESS = "billing_address";
        private static final String JSON_EXTENSION_STREET = "street";

        public class OrderParamsConverter implements JsonDeserializer<PosOrder> {
            @Override
            public PosOrder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject obj = json.getAsJsonObject();
                if (obj.has(JSON_EXTENSION_PAYENT)) {
                    obj.remove(JSON_EXTENSION_PAYENT);
                }
                if (obj.has(JSON_EXTENSION_BILLING_ADDRESS)) {
                    JsonObject obj_billing = obj.getAsJsonObject(JSON_EXTENSION_BILLING_ADDRESS);
                    if (obj_billing.has(JSON_EXTENSION_STREET)) {
                        obj_billing.remove(JSON_EXTENSION_STREET);
                    }
                }
                PosOrder order = new Gson().fromJson(obj, PosOrder.class);
                return order;
            }
        }
    }

    private class OrderEntity {
        String email = null;
        String id = null;

        OrderStatus statusHistory;

        Model entity;

        OrderCommentParams comment;
    }

    private class StatusEntity {
        OrderEntity comment;
        String id = null;
    }

    private class SendEmailEntity {
        String error;
        String message;
    }

    @Override
    public List<Order> retrieve(int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterNotEqual("status", "onhold")
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            if (ConfigUtil.isManageOrderByMe())
                paramBuilder.setFilterEqual("webpos_staff_id", ConfigUtil.getStaff().getID());
            if (ConfigUtil.isManageOrderByLocation())
                paramBuilder.setFilterEqual("location_id", ConfigUtil.getStaff().getStaffLocation().getID());

            if (!StringUtil.isNullOrEmpty(status))
                paramBuilder.setFilterIn("status", status);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
//            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(Gson2PosListOrder.class);
            Gson2PosListOrder listOrder = (Gson2PosListOrder) rp.doParse();
            List<Order> list = (List<Order>) (List<?>) (listOrder.items);

            return convertData(list);
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Order> retrieve(String searchString, int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterNotEqual("status", "onhold")
                    .setFilterOrLike("increment_id", finalSearchString)
                    .setFilterOrLike("customer_email", finalSearchString)
                    .setFilterOrLike("customer_firstname", finalSearchString)
                    .setFilterOrLike("customer_lastname", finalSearchString)
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            if (ConfigUtil.isManageOrderByMe())
                paramBuilder.setFilterEqual("webpos_staff_id", ConfigUtil.getStaff().getID());
            if (ConfigUtil.isManageOrderByLocation())
                paramBuilder.setFilterEqual("location_id", ConfigUtil.getStaff().getStaffLocation().getID());

            if (!StringUtil.isNullOrEmpty(status))
                paramBuilder.setFilterIn("status", status);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(Gson2PosListOrder.class);
            Gson2PosListOrder listOrder = (Gson2PosListOrder) rp.doParse();
            List<Order> list = (List<Order>) (List<?>) (listOrder.items);
            return convertData(list);
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_EMAIL);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.email = email;
            orderEntity.id = orderId;

            rp = statement.execute(orderEntity);
            SendEmailEntity sendEmail = new Gson().fromJson(rp.readResult2String(), SendEmailEntity.class);
            String message = "";
            if (sendEmail != null) {
                if (sendEmail.error.equals("false")) {
                    message = sendEmail.message;
                }
            }

            return message;
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_COMMENTS);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.statusHistory = orderStatus;

            StatusEntity statusEntity = new StatusEntity();
            statusEntity.id = orderId;
            statusEntity.comment = orderEntity;

            rp = statement.execute(statusEntity);
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosOrder.class);
            return (Order) rp.doParse();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public boolean orderRefundByCredit(OrderRefundCreditParams orderRefundCreditParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return false;
    }

    @Override
    public boolean orderRefundByGiftCard(OrderRefundGiftCard orderRefundGiftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return false;
    }

    @Override
    public Order orderRefund(OrderRefundParams refundParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderInvoiceUpdateQty(OrderUpdateQtyParam orderUpdateQtyParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_INVOICE_UPDATE_QTY);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute(orderUpdateQtyParam);
            String json = StringUtil.truncateJson(rp.readResult2String());
            Gson2PosOrderParseModel implement = new Gson2PosOrderParseModel();
            Gson gson = implement.createGson();
            Order order = gson.fromJson(json, PosOrder.class);
            return order;
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order orderInvoice(OrderInvoiceParams invoiceParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_INVOICE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.entity = invoiceParams;

            rp = statement.execute(orderEntity);
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosOrder.class);
            return (Order) rp.doParse();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order orderCancel(OrderCommentParams cancelParams, String orderID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_CANCEL);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);


            OrderEntity orderEntity = new OrderEntity();
            orderEntity.comment = cancelParams;
            orderEntity.id = orderID;

            rp = statement.execute(orderEntity);
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosOrder.class);
            return (Order) rp.doParse();
        } catch (Exception e) {
            throw new DataAccessException(e);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Product> retrieveOrderItem(String Ids) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public List<CheckoutPayment> retrievePaymentMethod() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderTakePayment(OrderTakePaymentParam orderTakePaymentParam, String orderID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setFilterNotEqual("status", "onhold")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(Gson2PosListOrder.class);
            Gson2PosListOrder listOrder = (Gson2PosListOrder) rp.doParse();
            return listOrder.total_count;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Order> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Order> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("created_at")
                    .setFilterNotEqual("status", "onhold")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            if (ConfigUtil.isManageOrderByMe())
                paramBuilder.setFilterEqual("webpos_staff_id", ConfigUtil.getStaff().getID());
            if (ConfigUtil.isManageOrderByLocation())
                paramBuilder.setFilterEqual("location_id", ConfigUtil.getStaff().getStaffLocation().getID());

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(Gson2PosListOrder.class);
            Gson2PosListOrder listOrder = (Gson2PosListOrder) rp.doParse();
            List<Order> list = (List<Order>) (List<?>) (listOrder.items);

            return convertData(list);
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Order> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterNotEqual("status", "onhold")
                    .setFilterOrLike("increment_id", finalSearchString)
                    .setFilterOrLike("customer_email", finalSearchString)
                    .setFilterOrLike("customer_firstname", finalSearchString)
                    .setFilterOrLike("customer_lastname", finalSearchString)
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            if (ConfigUtil.isManageOrderByMe())
                paramBuilder.setFilterEqual("webpos_staff_id", ConfigUtil.getStaff().getID());
            if (ConfigUtil.isManageOrderByLocation())
                paramBuilder.setFilterEqual("location_id", ConfigUtil.getStaff().getStaffLocation().getID());

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(Gson2PosListOrder.class);
            Gson2PosListOrder listOrder = (Gson2PosListOrder) rp.doParse();
            List<Order> list = (List<Order>) (List<?>) (listOrder.items);
            return convertData(list);
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Order oldModel, Order newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Order... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Order... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    private List<Order> convertData(List<Order> list) {
        for (Order order : list) {
            if (order.getItemsInfoBuy() != null) {
                List<OrderCartItem> listItem = order.getItemsInfoBuy().getListOrderCartItems();
                if (listItem != null && listItem.size() > 0) {
                    for (OrderCartItem orderCartItem : listItem) {
                        List<PosCartItem.OptionsValue> options = orderCartItem.getOptions();
                        if (options != null && options.size() > 0) {
                            int noItems = options.size();
                            for (int i = 0; i < noItems; i++) {
                                PosCartItem.OptionsValue optionsValue = options.get(i);
                                if (optionsValue.values != null && optionsValue.values.size() > 0) {
                                    for (String value : optionsValue.values) {
                                        PosCartItem.OptionsValue newValue = orderCartItem.createOptionValue();
                                        newValue.id = optionsValue.id;
                                        newValue.code = optionsValue.id;
                                        newValue.value = value;
                                        options.add(newValue);
                                        noItems++;
                                    }
                                }
                            }

                            for (int i = 0; i < options.size(); i++) {
                                PosCartItem.OptionsValue optionsValueR = options.get(i);
                                if (optionsValueR.values != null && optionsValueR.values.size() > 0) {
                                    options.remove(optionsValueR);
                                    i--;
                                }
                            }
                        }
                    }

                    for (OrderCartItem orderCartItem : listItem) {
                        List<PosCartItem.OptionsValue> superAttribute = orderCartItem.getSuperAttribute();
                        if (superAttribute != null && superAttribute.size() > 0) {
                            int noItems = superAttribute.size();
                            for (int i = 0; i < noItems; i++) {
                                PosCartItem.OptionsValue optionsValue = superAttribute.get(i);
                                if (optionsValue.values != null && optionsValue.values.size() > 0) {
                                    for (String value : optionsValue.values) {
                                        PosCartItem.OptionsValue newValue = orderCartItem.createOptionValue();
                                        newValue.id = optionsValue.id;
                                        newValue.code = optionsValue.id;
                                        newValue.value = value;
                                        superAttribute.add(newValue);
                                        noItems++;
                                    }
                                }
                            }

                            for (int i = 0; i < superAttribute.size(); i++) {
                                PosCartItem.OptionsValue optionsValueR = superAttribute.get(i);
                                if (optionsValueR.values != null && optionsValueR.values.size() > 0) {
                                    superAttribute.remove(optionsValueR);
                                    i--;
                                }
                            }
                        }
                    }

                    for (OrderCartItem orderCartItem : listItem) {
                        List<PosCartItem.OptionsValue> bundleOption = orderCartItem.getBundleOption();
                        if (bundleOption != null && bundleOption.size() > 0) {
                            int noItems = bundleOption.size();
                            for (int i = 0; i < noItems; i++) {
                                PosCartItem.OptionsValue optionsValue = bundleOption.get(i);
                                if (optionsValue.values != null && optionsValue.values.size() > 0) {
                                    for (String value : optionsValue.values) {
                                        PosCartItem.OptionsValue newValue = orderCartItem.createOptionValue();
                                        newValue.id = optionsValue.id;
                                        newValue.code = optionsValue.id;
                                        newValue.value = value;
                                        bundleOption.add(newValue);
                                        noItems++;
                                    }
                                }
                            }

                            for (int i = 0; i < bundleOption.size(); i++) {
                                PosCartItem.OptionsValue optionsValueR = bundleOption.get(i);
                                if (optionsValueR.values != null && optionsValueR.values.size() > 0) {
                                    bundleOption.remove(optionsValueR);
                                    i--;
                                }
                            }
                        }
                    }

                    for (OrderCartItem orderCartItem : listItem) {
                        List<PosCartItem.OptionsValue> bundleOptionQty = orderCartItem.getBundleOptionQty();
                        if (bundleOptionQty != null && bundleOptionQty.size() > 0) {
                            int noItems = bundleOptionQty.size();
                            for (int i = 0; i < noItems; i++) {
                                PosCartItem.OptionsValue optionsValue = bundleOptionQty.get(i);
                                if (optionsValue.values != null && optionsValue.values.size() > 0) {
                                    for (String value : optionsValue.values) {
                                        PosCartItem.OptionsValue newValue = orderCartItem.createOptionValue();
                                        newValue.id = optionsValue.id;
                                        newValue.code = optionsValue.id;
                                        newValue.value = value;
                                        bundleOptionQty.add(newValue);
                                        noItems++;
                                    }
                                }
                            }

                            for (int i = 0; i < bundleOptionQty.size(); i++) {
                                PosCartItem.OptionsValue optionsValueR = bundleOptionQty.get(i);
                                if (optionsValueR.values != null && optionsValueR.values.size() > 0) {
                                    bundleOptionQty.remove(optionsValueR);
                                    i--;
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}
