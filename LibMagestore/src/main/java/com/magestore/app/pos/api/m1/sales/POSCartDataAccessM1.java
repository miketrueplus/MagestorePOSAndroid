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
import com.magestore.app.lib.connection.http.MagestoreStatementAction;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.DataCheckout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.checkout.PosDataCheckout;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.plugins.PosGiftCard;
import com.magestore.app.pos.model.plugins.PosGiftCardRespone;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 8/9/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCartDataAccessM1 extends POSAbstractDataAccessM1 implements CartDataAccess {

    private class CartEntity {
        String currency_id = null;
        String customer_id = null;
        String item_id = null;
        String quote_id = null;
        String store_id = null;
        String till_id = null;
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
    public boolean delete(Checkout checkout, CartItem cartItem) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CART_DELETE_ITEM);

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            if (!StringUtil.isNullOrEmpty(ConfigUtil.getWebSiteId())) {
                paramBuilder.setParam("website_id", ConfigUtil.getWebSiteId());
            }

            CartEntity cartEntity = new CartEntity();
            cartEntity.quote_id = checkout.getQuoteId();
            cartEntity.item_id = cartItem.getItemId();
            cartEntity.currency_id = ConfigUtil.getCurrentCurrency().getCode();
            cartEntity.store_id = checkout.getStoreId();
            cartEntity.till_id = ConfigUtil.getPointOfSales() != null ? ConfigUtil.getPointOfSales().getID() : ConfigUtil.getPosId();
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
            cartEntity.customer_id = customer_id;

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(cartEntity);
            rp.setParseImplement(new Gson2PosCartParseModel());
            rp.setParseModel(PosDataCheckout.class);
            DataCheckout dataCheckout = (DataCheckout) rp.doParse();
            Checkout checkoutRespone = dataCheckout.getCheckout();

            CartItem cartItemResponse = null;
            if (checkoutRespone != null) {
                checkout.setTotals(checkoutRespone.getTotals());
                return true;
            } else {
                return false;
            }
        } catch (ConnectionException ex) {
            return false;
        } catch (IOException ex) {
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
    public void changeTax(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {

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
