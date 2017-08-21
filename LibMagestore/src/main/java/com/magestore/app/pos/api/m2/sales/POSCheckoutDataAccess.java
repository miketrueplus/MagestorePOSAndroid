package com.magestore.app.pos.api.m2.sales;

import android.util.Log;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.payment.PosAuthorizenet;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAuthorizenetParseModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosOrderParseModel;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCheckoutDataAccess extends POSAbstractDataAccess implements CheckoutDataAccess {
    static String CODE_PAYMENT_AUTHORIZENET = "authorizenet_directpost";
    private static final String PAYMENT_STORE_CREDIT_CODE = "storecredit";
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
    }

    @Override
    public boolean insert(Checkout... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECKOUT_CREATE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(models[0]);
            String result = rp.readResult2String();
            return true;
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
    public Checkout saveCart(Quote quote) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECK_OUT_SAVE_CART);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // set data
            quote.setCustomerId("");
            quote.setTillId("1");

            rp = statement.execute(quote);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);

            Checkout checkout = (Checkout) rp.doParse();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECKOUT_SAVE_QUOTE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // set data
            quoteParam.setCustomerId("");
            quoteParam.setTillId("1");

            rp = statement.execute(quoteParam);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);

            Checkout checkout = (Checkout) rp.doParse();
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
    public Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECKOUT_ADD_COUPON_TO_QUOTE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // set data
            quoteAddCouponParam.setCustomerId("");

            rp = statement.execute(quoteAddCouponParam);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);

            Checkout ck = (Checkout) rp.doParse();
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
    public Checkout saveShipping(Checkout checkout, String quoteId, String shippingCode) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECK_OUT_SAVE_SHIPPING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.quote_id = quoteId;
            checkoutEntity.shipping_method = shippingCode;

            rp = statement.execute(checkoutEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);

            Checkout ck = (Checkout) rp.doParse();
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
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECK_OUT_SAVE_PAYMENT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.quote_id = quoteId;
            checkoutEntity.payment_method = paymentCode;

            rp = statement.execute(checkoutEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCheckout.class);

            Checkout checkout = (Checkout) rp.doParse();
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
    public Model placeOrder(Checkout checkout, PlaceOrderParams placeOrderParams, List<CheckoutPayment> listCheckoutPayment) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECK_OUT_PLACE_ORDER);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // set data
            placeOrderParams.setCustomerId("");

            rp = statement.execute(placeOrderParams);
            rp.setParseImplement(getClassParseImplement());
            String json = rp.readResult2String();
//            if (placeOrderParams.getMethod().equals(CODE_PAYMENT_AUTHORIZENET) && json.contains("payment_infomation")) {
//                Gson2PosAuthorizenetParseModel implement = new Gson2PosAuthorizenetParseModel();
//                Gson gson = implement.createGson();
//                PosAuthorizenet authorizenet = gson.fromJson(json, PosAuthorizenet.class);
//                return (Authorizenet) authorizenet;
//            }
            Gson2PosOrderParseModel implement =  new Gson2PosOrderParseModel();
            Gson gson = implement.createGson();
            Order order = gson.fromJson(json, PosOrder.class);
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CHECK_OUT_SEND_EMAIL);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.email = email;
            checkoutEntity.increment_id = increment_id;
            rp = statement.execute(checkoutEntity);

            String json = StringUtil.truncateJson(rp.readResult2String());
            CheckoutEntity ck = new Gson().fromJson(json, CheckoutEntity.class);

            return ck != null ? ck.message : "false";
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
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            url = url + "?";
            connection = ConnectionFactory.generateConnection(getContext(), url, "", "");
//            connection = MagestoreConnection.getConnection(domain, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            Object o = new Object();
            o = params;
            rp = statement.execute(o);
            String respone = rp.readResult2String();
            if(respone.contains("Transaction ID")){
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public String approvedPaymentPayPal(String payment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_APPROVED_PAYMENT_PAYPAL);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.paymentId = payment_id;
            rp = statement.execute(checkoutEntity);

            String transaction_id = rp.readResult2String().trim().replace("\"", "");

            return transaction_id != null ? transaction_id : "";
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
    public String approvedAuthorizeIntergration(String quote_id, String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_APPROVED_PAYMENT_AUTHORIZE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.quote_id = quote_id;
            checkoutEntity.token = token;
            checkoutEntity.amount = amount;
            rp = statement.execute(checkoutEntity);

            String transaction_id = rp.readResult2String().trim().replace("\"", "");

            return transaction_id != null ? transaction_id : "";
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
    public String approvedStripe(String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_APPROVED_PAYMENT_STRIPE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            CheckoutEntity checkoutEntity = new CheckoutEntity();
            checkoutEntity.token = token;
            checkoutEntity.amount = amount;
            rp = statement.execute(checkoutEntity);

            String transaction_id = rp.readResult2String().trim().replace("\"", "");

            return transaction_id != null ? transaction_id : "";
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
    public String getAccessTokenPaypalHere() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_GET_ACCESS_TOKEN_PAYPAL_HERE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            String transaction_id = rp.readResult2String().trim().replace("\"", "");

            return transaction_id != null ? transaction_id : "";
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
    public boolean invoicesPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_INVOICE_PAYMENT_AUTHORIZE);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosOrder.class);
            Order order = (Order) rp.doParse();
            return (order != null) ? true : false;
        } catch (Exception e) {
            return false;
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
    public boolean cancelPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
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

            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosOrder.class);
            Order order = (Order) rp.doParse();
            return (order != null) ? true : false;
        } catch (Exception e) {
            return false;
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
}
