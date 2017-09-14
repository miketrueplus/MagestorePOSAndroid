package com.magestore.app.pos.api.odoo.sales;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.DataOrder;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundCreditParams;
import com.magestore.app.lib.model.sales.OrderRefundGiftCard;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderTakePaymentParam;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosDataOrder;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.model.sales.PosOrderBillingAddress;
import com.magestore.app.pos.model.sales.PosOrderStatus;
import com.magestore.app.pos.model.sales.PosOrderWebposPayment;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSOrderDataAccessOdoo extends POSAbstractDataAccessOdoo implements OrderDataAccess {

    public class Gson2PosOrderParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<PosOrder>>() {
            }
                    .getType(), new OrderConverter());
            return builder.create();
        }

        public class OrderConverter implements JsonDeserializer<List<PosOrder>> {
            @Override
            public List<PosOrder> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<PosOrder> listOrder = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_order = json.getAsJsonArray();
                    if (arr_order != null && arr_order.size() > 0) {
                        float total_subtotal = 0;
                        float total_discount = 0;
                        for (JsonElement el_order : arr_order) {
                            JsonObject obj_order = el_order.getAsJsonObject();
                            PosOrder order = new PosOrder();
                            listOrder.add(mappingOrder(order, total_subtotal, total_discount, obj_order));
                        }
                    }
                }
                return listOrder;
            }
        }
    }

    private String ORDER_ID = "id";
    private String ORDER_NAME = "name";
    private String ORDER_CREATE_AT = "create_date";
    private String ORDER_STATUS = "state";
    private String ORDER_NOTE = "note";
    private String ORDER_GRAND_TOTAL = "amount_total";
    private String ORDER_TOTAL_PAID = "amount_paid";
    private String ORDER_TOTAL_TAX = "amount_tax";
    private String ORDER_TOTAL_CHANGE = "amount_return";
    private String ORDER_STAFF_NAME = "create_name";
    private String ORDER_STAFF_ID = "create_uid";
    private String ORDER_ITEMS = "lines";
    private String PAYMENT_DETAIL = "statement_detail";
    private String PAYMENT_ID = "journal_id";
    private String PAYMENT_NAME = "journal_name";
    private String PAYMENT_AMOUNT = "amount";
    private String PRODUCT_ID = "product_id";
    private String PRODUCT_NAME = "product_name";
    private String PRODUC_UNIT_PRICE = "price_unit";
    private String PRODUCT_SUBTOTAL = "price_subtotal";
    private String PRODUCT_SUTOTAL_INCl = "price_subtotal_incl";
    private String PRODUCT_DISCOUNT = "discount";
    private String PRODUCT_QTY = "qty";
    private String CUSTOMER_DETAIL = "partner_id";
    private String CUSTOMER_ID = "id";
    private String CUSTOMER_EMAIL = "email";
    private String CUSTOMER_NAME = "name";
    private String CUSTOMER_PHONE = "phone";
    private String CUSTOMER_STATE_ID = "state_id";
    private String CUSTOMER_STATE_NAME = "state_name";
    private String CUSTOMER_STATE_CODE = "state_code";
    private String CUSTOMER_COMPANY = "company_name";
    private String CUSTOMER_POSCODE = "zip";
    private String CUSTOMER_COUNTRY_ID = "country_id";
    private String CUSTOMER_STREET = "street";
    private String CUSTOMER_STREET2 = "street2";
    private PosOrder mappingOrder(PosOrder order, float total_subtotal, float total_discount, JsonObject obj_order) {
        String id = obj_order.remove(ORDER_ID).getAsString();
        order.setID(id);
        String name = obj_order.remove(ORDER_NAME).getAsString();
        order.setIncrementId(name);
        String create_at = obj_order.remove(ORDER_CREATE_AT).getAsString();
        order.setCreateAt(create_at);
        String status = obj_order.remove(ORDER_STATUS).getAsString();
        String order_status = "";
        if (status.equals(StringUtil.STATUS_PAID)) {
            order_status = StringUtil.STATUS_PENDING;
        } else if (status.equals(StringUtil.STATUS_INVOICED)) {
            order_status = StringUtil.STATUS_COMPLETE;
        } else if (status.equals(StringUtil.STATUS_CANCEL)) {
            order_status = StringUtil.STATUS_CANCELLED;
        } else if (status.equals(StringUtil.STATUS_DONE)) {
            order_status = StringUtil.STATUS_CLOSED;
        }
        order.setStatus(order_status);
        float grand_total = obj_order.remove(ORDER_GRAND_TOTAL).getAsFloat();
        order.setGrandTotal(grand_total);
        order.setBaseGrandTotal(ConfigUtil.convertToBasePrice(grand_total));
        float total_paid = obj_order.remove(ORDER_TOTAL_PAID).getAsFloat();
        order.setTotalPaid(total_paid);
        order.setBaseTotalPaid(ConfigUtil.convertToBasePrice(total_paid));
        float tax_amount = obj_order.remove(ORDER_TOTAL_TAX).getAsFloat();
        order.setTaxAmount(tax_amount);
        order.setBaseTaxAmount(ConfigUtil.convertToBasePrice(tax_amount));
        float total_change = obj_order.remove(ORDER_TOTAL_CHANGE).getAsFloat();
        order.setWebposChange(total_change);
        order.setWebposBaseChange(ConfigUtil.convertToBasePrice(total_change));
        if (obj_order.has(ORDER_STAFF_NAME)) {
            String staff_name = obj_order.remove(ORDER_STAFF_NAME).getAsString();
            order.setWebposStaffName(staff_name);
        }
        if (obj_order.has(ORDER_STAFF_ID)) {
            String staff_id = obj_order.remove(ORDER_STAFF_ID).getAsString();
            order.setWebposStaffId(staff_id);
        }
        List<OrderWebposPayment> listPayment = new ArrayList<>();
        JsonArray arr_payment = obj_order.getAsJsonArray(PAYMENT_DETAIL);
        if (arr_payment != null && arr_payment.size() > 0) {
            for (JsonElement el_payment : arr_payment) {
                JsonObject obj_payment = el_payment.getAsJsonObject();
                PosOrderWebposPayment payment = new PosOrderWebposPayment();
                String payment_id = obj_payment.remove(PAYMENT_ID).getAsString();
                String payment_name = obj_payment.remove(PAYMENT_NAME).getAsString();
                float payment_amount = obj_payment.remove(PAYMENT_AMOUNT).getAsFloat();
                payment.setID(payment_id);
                payment.setMethodTitle(StringUtil.checkJsonData(payment_name) ? payment_name : "");
                payment.setBasePaymentAmount(ConfigUtil.convertToBasePrice(payment_amount));
                listPayment.add(payment);
            }
        }
        if (obj_order.get(CUSTOMER_DETAIL).isJsonObject()) {
            JsonObject obj_customer = obj_order.get(CUSTOMER_DETAIL).getAsJsonObject();
            String customer_id = obj_customer.remove(CUSTOMER_ID).getAsString();
            String customer_email = obj_customer.remove(CUSTOMER_EMAIL).getAsString();
            String customer_name = obj_customer.remove(CUSTOMER_NAME).getAsString();
            String customer_phone = obj_customer.remove(CUSTOMER_PHONE).getAsString();
            String state_id = obj_customer.remove(CUSTOMER_STATE_ID).getAsString();
            String state_name = obj_customer.remove(CUSTOMER_STATE_NAME).getAsString();
            String state_code = obj_customer.remove(CUSTOMER_STATE_CODE).getAsString();
            String company = obj_customer.remove(CUSTOMER_COMPANY).getAsString();
            String poscode = obj_customer.remove(CUSTOMER_POSCODE).getAsString();
            order.setCustomerId(customer_id);
            order.setCustomerEmail(StringUtil.checkJsonData(customer_email) ? customer_email : "");
            order.setCustomerFirstName(StringUtil.checkJsonData(customer_name) ? customer_name : "");
            PosOrderBillingAddress billingAddress = new PosOrderBillingAddress();
            billingAddress.setID(customer_id);
            billingAddress.setEmail(StringUtil.checkJsonData(customer_email) ? customer_email : "");
            billingAddress.setFirstName(StringUtil.checkJsonData(customer_name) ? customer_name : "");
            billingAddress.setTelephone(StringUtil.checkJsonData(customer_phone) ? customer_phone : "");
            billingAddress.setRegion(StringUtil.checkJsonData(state_name) ? state_name : "");
            billingAddress.setRegionCode(StringUtil.checkJsonData(state_code) ? state_code : "");
            billingAddress.setRegionId(StringUtil.checkJsonData(state_id) ? state_id : "");
            billingAddress.setCompany(StringUtil.checkJsonData(company) ? company : "");
            billingAddress.setPostCode(StringUtil.checkJsonData(poscode) ? poscode : "");
        }
        order.setWebposOrderPayments(listPayment);
        String note = obj_order.remove(ORDER_NOTE).getAsString();
        List<OrderStatus> listComment = new ArrayList<>();
        PosOrderStatus comment = new PosOrderStatus();
        comment.setComment(StringUtil.checkJsonData(note) ? note : "");
        listComment.add(comment);
        order.setOrderStatus(listComment);
        List<CartItem> listItem = new ArrayList<>();
        JsonArray arr_item = obj_order.getAsJsonArray(ORDER_ITEMS);
        if (arr_item != null && arr_item.size() > 0) {
            for (JsonElement el_item : arr_item) {
                JsonObject obj_item = el_item.getAsJsonObject();
                float unit_price = obj_item.remove(PRODUC_UNIT_PRICE).getAsFloat();
                if (unit_price > 0) {
                    PosCartItem cartItem = new PosCartItem();
                    if (obj_item.has(PRODUCT_ID)) {
                        String item_id = obj_item.remove(PRODUCT_ID).getAsString();
                        cartItem.setID(item_id);
                    }
                    if (obj_item.has(PRODUCT_NAME)) {
                        String item_name = obj_item.remove(PRODUCT_NAME).getAsString();
                        cartItem.setName(item_name);
                    }
                    float qty = obj_item.remove(PRODUCT_QTY).getAsFloat();
                    cartItem.setQuantity(qty);
                    cartItem.setQtyOrdered(qty);
                    if (order.getStatus().equals(StringUtil.STATUS_COMPLETE)) {
                        cartItem.setQtyInvoiced(qty);
                    }
                    cartItem.setOriginalPrice(unit_price);
                    // TODO: thiếu + tax
                    cartItem.setBasePriceInclTax(ConfigUtil.convertToBasePrice(unit_price));
                    float product_subtotal = obj_item.remove(PRODUCT_SUBTOTAL).getAsFloat();
                    cartItem.setBaseSubtotal(ConfigUtil.convertToBasePrice(product_subtotal));
                    float product_subtotal_incl = obj_item.remove(PRODUCT_SUTOTAL_INCl).getAsFloat();
                    cartItem.setRowTotal(product_subtotal_incl);
                    cartItem.setBaseRowTotalInclTax(ConfigUtil.convertToBasePrice(product_subtotal_incl));
                    float product_discount_percent = obj_item.remove(PRODUCT_DISCOUNT).getAsFloat();
                    // TODO: thiếu + tax của từng sản phẩm
                    float product_discount = ((unit_price * qty) * product_discount_percent) / 100;
                    cartItem.setBaseDiscountAmount(ConfigUtil.convertToBasePrice(product_discount));
                    cartItem.setDiscountAmount(product_discount);
                    total_subtotal += product_subtotal;
                    listItem.add(cartItem);
                } else {
                    total_discount = unit_price;
                }
            }
        }
        order.setDiscountAmount(total_discount);
        order.setBaseDiscountAmount(ConfigUtil.convertToBasePrice(total_discount));
        order.setBaseSubtotal(ConfigUtil.convertToBasePrice(total_subtotal));
        order.setOrderItem(listItem);
        return order;
    }

    private class StatusEntity {
        String order_id;
        String note;
    }

    private class InvoiceEntity {
        String order_id;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            return listOrder.getTotalCount();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("create_date");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
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
    public List<Order> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("name", finalSearchString)
                    .setFilterOrLike("partner_id", finalSearchString)
                    .setFilterOrLike("user_id", finalSearchString)
                    .setFilterOrLike("state", finalSearchString)
                    .setSortOrderDESC("create_date");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
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

    @Override
    public List<Order> retrieve(int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("create_date");

            if (!StringUtil.isNullOrEmpty(status))
                paramBuilder.setFilterIn("state", status);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
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
    public List<Order> retrieve(String searchString, int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("name", finalSearchString)
                    .setFilterOrLike("partner_id", finalSearchString)
                    .setFilterOrLike("user_id", finalSearchString)
                    .setFilterOrLike("state", finalSearchString)
                    .setSortOrderDESC("create_date");

            if (!StringUtil.isNullOrEmpty(status))
                paramBuilder.setFilterIn("state", status);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
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
    public String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_COMMENTS);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            StatusEntity statusEntity = new StatusEntity();
            statusEntity.order_id = orderId;
            statusEntity.note = orderStatus.getComment();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(statusEntity);
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list.get(0);
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
    public Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public boolean orderRefundByCredit(OrderRefundCreditParams orderRefundCreditParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return false;
    }

    @Override
    public boolean orderRefundByGiftCard(Order order, OrderRefundGiftCard orderRefundGiftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return false;
    }

    @Override
    public Order orderRefund(OrderRefundParams refundParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderInvoiceUpdateQty(OrderUpdateQtyParam orderUpdateQtyParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderInvoice(OrderInvoiceParams invoiceParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_INVOICE);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            InvoiceEntity invoiceEntity = new InvoiceEntity();
            invoiceEntity.order_id = invoiceParams.getOrderId();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(invoiceEntity);
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list.get(0);
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
    public Order orderCancel(OrderCommentParams cancelParams, String orderID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
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
    public Order orderTakePayment(OrderTakePaymentParam orderTakePaymentParam, String orderID, Order order) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }
}
