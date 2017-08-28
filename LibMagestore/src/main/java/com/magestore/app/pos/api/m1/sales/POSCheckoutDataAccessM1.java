package com.magestore.app.pos.api.m1.sales;

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
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.DataCheckout;
import com.magestore.app.lib.model.checkout.DataPlaceOrder;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.checkout.PosDataCheckout;
import com.magestore.app.pos.model.checkout.PosDataPlaceOrder;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.plugins.PosGiftCard;
import com.magestore.app.pos.model.plugins.PosGiftCardRespone;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosOrderParseModel;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 8/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCheckoutDataAccessM1 extends POSAbstractDataAccessM1 implements CheckoutDataAccess {
    private static final String PAYMENT_STORE_CREDIT_CODE = "storecredit";
    private static final String PAYMENT_STRIPE_CODE = "stripe_integration";
    private static final String PAYMENT_AUTHORIZE = "authorizenet_integration";

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

    private class QuoteParam {
        String quote_id;
        String store_id;
        String customer_id;
        String currency_id;
        String till_id;
        List<QuoteItemParam> items;
        QuoteCustomer customer;
        String shipping_method;
    }

    private class QuoteItemParam {
        String id;
        String item_id;
        String qty;
        String qty_to_ship;
        int use_discount;
        String amount;
        String custom_price;
        String is_custom_sale;

        List<QuoteItemExtension> extension_data;

        List<PosCartItem.OptionsValue> options;
        List<PosCartItem.OptionsValue> super_attribute;
        Map<String, Object> bundle_option;
        Map<String, String> bundle_option_qty;
    }

    public class Gson2PosCartParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<PosGiftCardRespone>() {
            }
                    .getType(), new GiftCardConverter());
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

        public class GiftCardConverter implements JsonDeserializer<PosGiftCardRespone> {

            @Override
            public PosGiftCardRespone deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                PosGiftCardRespone respone = new PosGiftCardRespone();
                List<GiftCard> listGiftcard = new ArrayList<>();
                JsonObject obj = json.getAsJsonObject();
                if (obj.has("used_codes_app")) {
                    JsonArray arr_gift = obj.getAsJsonArray("used_codes_app");
                    for (JsonElement el_gift : arr_gift) {
                        JsonObject obj_gift = el_gift.getAsJsonObject();
                        PosGiftCard giftCard = gson.fromJson(obj_gift, PosGiftCard.class);
                        listGiftcard.add(giftCard);
                    }
                }
                respone.setUsedCodes(listGiftcard);
                return respone;
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

            // set data
            quote.setTillId(ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId());

            rp = statement.execute(setQuoteParam(quote));
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

    private QuoteParam setQuoteParam(Quote quote) {
        QuoteParam quoteParam = new QuoteParam();
        quoteParam.currency_id = quote.getCurrencyId();
        quoteParam.customer = quote.getCustomer();
        quoteParam.customer_id = quote.getCustomerId();
        quoteParam.quote_id = quote.getID();
        quoteParam.store_id = quote.getStoreId();
        quoteParam.till_id = quote.getTillId();
        quoteParam.shipping_method = quote.getShippingMethod();
        List<QuoteItemParam> listQuoteItemParam = new ArrayList<>();
        if (quote.getItems() != null && quote.getItems().size() > 0) {
            for (QuoteItems item : quote.getItems()) {
                QuoteItemParam quoteItemParam = new QuoteItemParam();
                quoteItemParam.id = item.getID();
                quoteItemParam.item_id = item.getItemId();
                quoteItemParam.qty = item.getQty() + "";
                quoteItemParam.qty_to_ship = item.getQtyToShip() + "";
                quoteItemParam.use_discount = item.getUserDiscount();
                quoteItemParam.amount = item.getAmount();
                quoteItemParam.custom_price = item.getCustomPrice();
                if (!StringUtil.isNullOrEmpty(item.getCustomSale())) {
                    quoteItemParam.is_custom_sale = item.getCustomSale();
                }

                quoteItemParam.extension_data = item.getExtensionData();

                quoteItemParam.options = item.getOptions();
                quoteItemParam.super_attribute = item.getSuperAttribute();
                if (item.getBundleOption() != null && item.getBundleOption().size() > 0) {
                    Map<String, List<String>> mapBundleOption = new HashMap<>();
                    for (PosCartItem.OptionsValue option : item.getBundleOption()) {
                        if (mapBundleOption.containsKey(option.code)) {
                            List<String> listValue = mapBundleOption.get(option.code);
                            listValue.add(option.value);
                        } else {
                            List<String> listValue = new ArrayList<>();
                            listValue.add(option.value);
                            mapBundleOption.put(option.code, listValue);
                        }
                    }
                    Map<String, Object> mapBundleObj = new HashMap<>();
                    for (String key : mapBundleOption.keySet()) {
                        List<String> listValue = mapBundleOption.get(key);
                        if (listValue != null) {
                            if (listValue.size() > 1) {
                                mapBundleObj.put(key, listValue);
                            } else if (listValue.size() == 1) {
                                mapBundleObj.put(key, listValue.get(0));
                            }
                        }
                    }
                    quoteItemParam.bundle_option = mapBundleObj;
                }
                if (item.getBundleOptionQty() != null && item.getBundleOptionQty().size() > 0) {
                    Map<String, String> mapBundleOptionQty = new HashMap<>();
                    for (PosCartItem.OptionsValue option : item.getBundleOptionQty()) {
                        mapBundleOptionQty.put(option.code, option.value);
                    }
                    quoteItemParam.bundle_option_qty = mapBundleOptionQty;
                }
                listQuoteItemParam.add(quoteItemParam);
            }
        }
        quoteParam.items = listQuoteItemParam;
        return quoteParam;
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

            // set data
            quoteParam.setTillId(ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId());

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
    public Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECKOUT_ADD_COUPON_TO_QUOTE);

            // set data
            quoteAddCouponParam.setStoreId(checkout.getStoreId());
            quoteAddCouponParam.setCurrencyId(ConfigUtil.getCurrentCurrency().getCode());
            quoteAddCouponParam.setTillId(ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute(quoteAddCouponParam);
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
            checkoutEntity.till_id = ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId();
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
    public void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        List<CartItem> listCartNew = newCheckout.getCartItem();
        List<CartItem> listCartOld = oldCheckout.getCartItem();
        for (CartItem cartNew : listCartNew) {
            for (CartItem cartOld : listCartOld) {
                if (cartOld.getItemId().equals(cartNew.getOfflineItemId()) || cartOld.getItemId().equals(cartNew.getItemId())) {
                    cartOld.setItemId(cartNew.getItemId());
                    cartOld.setIsSaveCart(true);
                    cartOld.setPrice(cartNew.getPrice());
                    cartOld.setUnitPrice(cartNew.getPrice());
                    cartOld.getProduct().setItemId(cartNew.getItemId());
                    cartOld.getProduct().setIsSaveCart(true);
                    cartOld.setIsVirtual(cartNew.getIsVirtual());
                    cartOld.setQuantity(cartNew.getQuantity());
                    if (cartOld.isCustomPrice()) {
                        cartOld.setPriceShowView(ConfigUtil.convertToBasePrice(cartNew.getOriginalCustomPrice()));
                    } else {
                        cartOld.setPriceShowView(ConfigUtil.convertToBasePrice(cartNew.getOriginalPrice()));
                    }
                }
            }
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CHECK_OUT_PLACE_ORDER);

            // set data
            placeOrderParams.setStoreId(checkout.getStoreId());
            placeOrderParams.setTillId(ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId());
            placeOrderParams.setCurrencyId(ConfigUtil.getCurrentCurrency().getCode());

            if (listCheckoutPayment != null && listCheckoutPayment.size() == 1) {
                String paymentCode = listCheckoutPayment.get(0).getCode();
                String paymentType = listCheckoutPayment.get(0).getType();
                if (paymentType.equals("2") || paymentCode.equals(PAYMENT_STRIPE_CODE) || paymentCode.equals(PAYMENT_AUTHORIZE) || paymentCode.equals(PAYMENT_STORE_CREDIT_CODE)) {
                    placeOrderParams.setMethod("multipaymentforpos");
                } else {
                    placeOrderParams.setMethod(paymentCode);
                }
            }

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
            String message = "false";
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
            if (respone.contains("Transaction ID")) {
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
    public boolean invoicesPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_INVOICE_PAYMENT_AUTHORIZE);
            statement.setParam(POSAPIM1.PARAM_ORDER_ID, orderID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ORDER_CANCEL);
            statement.setParam(POSAPIM1.PARAM_ORDER_ID, orderID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
    public String approvedPaymentPayPal(String payment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_APPROVED_PAYMENT_PAYPAL);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_APPROVED_PAYMENT_AUTHORIZE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_APPROVED_PAYMENT_STRIPE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_GET_ACCESS_TOKEN_PAYPAL_HERE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
