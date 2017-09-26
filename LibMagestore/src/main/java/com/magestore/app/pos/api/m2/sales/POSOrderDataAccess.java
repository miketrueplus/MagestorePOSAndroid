package com.magestore.app.pos.api.m2.sales;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedTreeMap;
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
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.model.sales.OrderRefundCreditParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundGiftCard;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderTakePaymentParam;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.model.sales.PosOrderAttributes;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListPaymentMethod;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListProduct;
import com.magestore.app.pos.parse.gson2pos.Gson2PosOrderUpdateParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSOrderDataAccess extends POSAbstractDataAccess implements OrderDataAccess {
    public class Gson2PosOrderParseModel extends Gson2PosAbstractParseImplement {
        @Override
        public Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
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
    }

    private static List<CheckoutPayment> mListPayment;

    private class OrderEntity {
        String email = null;

        OrderStatus statusHistory;

        Model entity;

        OrderCommentParams comment;
    }

    private class OrderRefundEntity {
        LinkedTreeMap entity;
    }

    private class OrderRefundItemParam {
        float qty;
        String order_item_id;
        String additional_data;
    }

    private class OrderRefundComment {
        String comment;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

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
    public List<Order> retrieve(int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

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
    public List<Order> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

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
    public List<Order> retrieve(String searchString, int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterLike("increment_id", finalSearchString)
                    .setFilterLike("customer_email", finalSearchString)
                    .setFilterLike("customer_firstname", finalSearchString)
                    .setFilterLike("customer_lastname", finalSearchString)
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

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
    public List<Order> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterLike("increment_id", finalSearchString)
                    .setFilterLike("customer_email", finalSearchString)
                    .setFilterLike("customer_firstname", finalSearchString)
                    .setFilterLike("customer_lastname", finalSearchString)
                    .setSortOrderDESC("created_at")
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

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

    /**
     * Trả về boolean thành công hay không
     *
     * @param email
     * @return boolean thành công hay không
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_EMAIL);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderId);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                    .setParam(POSAPI.PARAM_ORDER_ID, orderId);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.email = email;

            rp = statement.execute(orderEntity);
            if (rp.readResult2String().equals("false")) {
                return "false";
            }
            String message = "false";
            JSONObject object = new JSONObject(rp.readResult2String());
            String obj_message = object.getString("message");
            if (!StringUtil.isNullOrEmpty(obj_message)) {
                message = obj_message;
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

    /**
     * Trả về Order
     *
     * @param orderStatus
     * @param orderId
     * @return Order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_COMMENTS);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderId);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                    .setParam(POSAPI.PARAM_ORDER_ID, orderId);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.statusHistory = orderStatus;

            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
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

    /**
     * Trả về Order
     *
     * @param shipmentParams
     * @return Order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_SHIPMENT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.entity = shipmentParams;

            // TODO: log params request
            Gson gson = new Gson();
            String json = gson.toJson(orderEntity);
            Log.e("JSON", json.toString());

            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
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
    public boolean orderRefundByCredit(OrderRefundCreditParams orderRefundCreditParams) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_BY_CREDIT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute(orderRefundCreditParams);
            String respone = StringUtil.truncateJson(rp.readResult2String());
            return true;
        } catch (Exception e) {
            return true;
//            throw new DataAccessException(e);
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
    public boolean orderRefundByGiftCard(Order order, OrderRefundGiftCard orderRefundGiftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_BY_GIFTCARD);

            orderRefundGiftCard.setOrderId(order.getIncrementId());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute(orderRefundGiftCard);
            String respone = StringUtil.truncateJson(rp.readResult2String());
            return true;
        } catch (Exception e) {
            return true;
//            throw new DataAccessException(e);
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
    public Order orderRefund(OrderRefundParams refundParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_REFUND);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // set data
            refundParams.setIncrementId(null);
            refundParams.setInvoiceId(null);

            OrderRefundEntity orderRefundEntity = new OrderRefundEntity();
            List<OrderRefundItemParam> listOrderRefundItemParams = new ArrayList<>();
            if (refundParams.getItems() != null && refundParams.getItems().size() > 0) {
                for (OrderItemParams item: refundParams.getItems()) {
                    OrderRefundItemParam itemRefund = new OrderRefundItemParam();
                    itemRefund.qty = item.getQty();
                    itemRefund.order_item_id = item.getOrderItemId();
                    itemRefund.additional_data = item.getAdditionalData();
                    listOrderRefundItemParams.add(itemRefund);
                }
            }
            List<OrderRefundComment> listOrderRefundComments = new ArrayList<>();
            if (refundParams.getComments() != null && refundParams.getComments().size() > 0) {
                for (OrderCommentParams comment: refundParams.getComments()) {
                    OrderRefundComment commentRefund = new OrderRefundComment();
                    commentRefund.comment = comment.getComment();
                    listOrderRefundComments.add(commentRefund);
                }
            }

            LinkedTreeMap data = new LinkedTreeMap();
            data.put("order_id", refundParams.getOrderId());
            data.put("base_currency_code", refundParams.getBaseCurrencyCode());
            data.put("store_currency_code", refundParams.getStoreCurrencyCode());
            data.put("shipping_amount", refundParams.getShippingAmount());
            data.put("email_sent", refundParams.getEmailSent());
            data.put("items", listOrderRefundItemParams);
            data.put("comments", listOrderRefundComments);
            data.put("adjustment_negative", refundParams.getAdjustmentNegative());
            data.put("adjustment_positive", refundParams.getAdjustmentPositive());
            orderRefundEntity.entity = data;

            rp = statement.execute(orderRefundEntity);
            rp.setParseImplement(getClassParseImplement());
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
    public Order orderInvoiceUpdateQty(OrderUpdateQtyParam orderUpdateQtyParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_INVOICE_UPDATE_QTY);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute(orderUpdateQtyParam);
            String json = StringUtil.truncateJson(rp.readResult2String());
            Gson2PosOrderUpdateParseImplement implement = new Gson2PosOrderUpdateParseImplement();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_INVOICE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.entity = invoiceParams;

            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_CANCEL);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);


            OrderEntity orderEntity = new OrderEntity();
            orderEntity.comment = cancelParams;

            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
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
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setFilterIn("entity_id", Ids)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListProduct.class);
            Gson2PosListProduct listProduct = (Gson2PosListProduct) rp.doParse();
            List<Product> list = (List<Product>) (List<?>) (listProduct.items);
            return list;
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
    public List<CheckoutPayment> retrievePaymentMethod() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        if (mListPayment != null) {
            return mListPayment;
        }

        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_PAYMENT_METHOD_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListPaymentMethod.class);
            Gson2PosListPaymentMethod listPayment = (Gson2PosListPaymentMethod) rp.doParse();
            List<CheckoutPayment> list = (List<CheckoutPayment>) (List<?>) (listPayment.items);
            mListPayment = new ArrayList<>();
            if (list != null && list.size() > 0) {
                mListPayment.addAll(list);
            }
            return mListPayment;
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
    public Order orderTakePayment(OrderTakePaymentParam orderTakePaymentParam, String orderID, Order order) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_TAKE_PAYMENT);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            orderTakePaymentParam.setShiftId(null);

            rp = statement.execute(orderTakePaymentParam);
            rp.setParseImplement(getClassParseImplement());
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
