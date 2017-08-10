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
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.DataCheckout;
import com.magestore.app.lib.model.checkout.DataPlaceOrder;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.PosDataCheckout;
import com.magestore.app.pos.model.checkout.PosDataPlaceOrder;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosOrderParseModel;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 8/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCheckoutDataAccessM1 extends POSAbstractDataAccessM1 implements CheckoutDataAccess {

    private class CheckoutEntity {
        String quote_id = null;
        String shipping_method = null;
        String payment_method = null;
        String email = null;
        String increment_id = null;
        boolean error = false;
        String message = null;
        String paymentId = null;
        String token = null;
        float amount;
        String currency_id = null;
        String store_id = null;
        String till_id = null;
        String customer_id = null;
    }

    private class SendEmailEntity {
        String status = null;
        List<String> messages;
    }

    public class Gson2PosCartParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<PosCartItem>>() {
            }
                    .getType(), new CartConverter());
            return builder.create();
        }

        public class CartConverter implements JsonDeserializer<List<PosCartItem>> {

            @Override
            public List<PosCartItem> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                List<PosCartItem> listItem = new ArrayList<>();
                if (json.isJsonObject()) {
                    JsonObject object = json.getAsJsonObject();
                    if (object != null) {
                        for (Map.Entry<String, JsonElement> item : object.entrySet()) {
                            PosCartItem cartItem = gson.fromJson(item.getValue().getAsJsonObject(), PosCartItem.class);
                            if (cartItem != null) {
                                listItem.add(cartItem);
                            }
                        }
                    }
                }
                return listItem;
            }
        }
    }

    @Override
    public boolean insert(Checkout... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public Checkout saveCart(Quote quote) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECK_OUT_SAVE_CART);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute(quote);
            rp.setParseImplement(new Gson2PosCartParseModel());
            rp.setParseModel(PosDataCheckout.class);
            DataCheckout dataCheckout = (DataCheckout) rp.doParse();
            Checkout checkout = dataCheckout.getCheckout();

            return checkout;
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
    public Checkout saveQuote(SaveQuoteParam quoteParam) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECKOUT_SAVE_QUOTE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute(quoteParam);
            rp.setParseImplement(new Gson2PosCartParseModel());
            rp.setParseModel(PosDataCheckout.class);
            DataCheckout dataCheckout = (DataCheckout) rp.doParse();
            Checkout checkout = dataCheckout.getCheckout();
            return checkout;
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
    public Checkout addCouponToQuote(QuoteAddCouponParam quoteAddCouponParam) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECKOUT_ADD_COUPON_TO_QUOTE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute(quoteAddCouponParam);
            rp.setParseImplement(new Gson2PosCartParseModel());
            rp.setParseModel(PosDataCheckout.class);
            DataCheckout dataCheckout = (DataCheckout) rp.doParse();
            Checkout checkout = dataCheckout.getCheckout();
            return checkout;
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
    public Checkout saveShipping(Checkout checkout, String quoteId, String shippingCode) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECK_OUT_SAVE_SHIPPING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.quote_id = quoteId;
            checkoutEntity.shipping_method = shippingCode;
            checkoutEntity.currency_id = ConfigUtil.getCurrentCurrency().getCode();
            checkoutEntity.store_id = checkout.getStoreId();
            checkoutEntity.till_id = ConfigUtil.getPointOfSales().getID();
            String customer_id = "";
            if (checkout.getCustomer() != null) {
                if (StringUtil.isNullOrEmpty(checkout.getCustomerID())) {
                    if (!checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
                        customer_id = checkout.getCustomerID();
                    } else {
                        customer_id = checkout.getCustomerID();
                    }
                } else {
                    customer_id = checkout.getCustomerID();
                }
            } else {
                customer_id = "";
            }
            checkoutEntity.customer_id = customer_id;

            rp = statement.execute(checkoutEntity);
            rp.setParseImplement(new Gson2PosCartParseModel());
            rp.setParseModel(PosDataCheckout.class);
            DataCheckout dataCheckout = (DataCheckout) rp.doParse();
            Checkout ck = dataCheckout.getCheckout();
            return ck;
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
    public Checkout savePayment(String quoteId, String paymentCode) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public Model placeOrder(PlaceOrderParams placeOrderParams) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECK_OUT_PLACE_ORDER);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute(placeOrderParams);
            rp.setParseImplement(new Gson2PosOrderParseModel());
            rp.setParseModel(PosDataPlaceOrder.class);
            DataPlaceOrder dataPlaceOrder = (DataPlaceOrder) rp.doParse();
            Order order = dataPlaceOrder.getOrder();
            return order;
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
    public boolean addOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean removeOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public String sendEmail(String email, String increment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECK_OUT_SEND_EMAIL);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.email = email;
            checkoutEntity.increment_id = increment_id;
            rp = statement.execute(checkoutEntity);

            String json = StringUtil.truncateJson(rp.readResult2String());
            SendEmailEntity ck = new Gson().fromJson(json, SendEmailEntity.class);
            String message = "";
            if (ck != null) {
                if (ck.status.equals("1")) {
                    if (ck.messages != null && ck.messages.size() > 0) {
                        message = ck.messages.get(0);
                    }
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
    public boolean approvedAuthorizenet(String url, String params) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean invoicesPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean cancelPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public String approvedPaymentPayPal(String payment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public String approvedAuthorizeIntergration(String quote_id, String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public String approvedStripe(String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public String getAccessTokenPaypalHere() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    private boolean checkCustomerID(Customer customer, Customer guest_customer) {
        if (getCustomerId(customer).equals(getCustomerId(guest_customer))) {
            return false;
        }

        return true;
    }

    private String getCustomerId(Customer customer) {
        String customerId = "";
        String customerEmail = customer.getEmail();
        if (StringUtil.isNullOrEmpty(customer.getID())) {
            if (StringUtil.isNullOrEmpty(customerEmail)) {
                customerId = customerEmail;
            }
        } else {
            customerId = customer.getID();
        }
        return customerId;
    }
}
