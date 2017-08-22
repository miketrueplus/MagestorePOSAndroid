package com.magestore.app.pos.api.m1.plugins;

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
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.DataCheckout;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.plugins.PluginsDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.PosDataCheckout;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.model.plugins.PosGiftCard;
import com.magestore.app.pos.model.plugins.PosGiftCardRespone;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 8/9/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSPluginsDataAccessM1 extends POSAbstractDataAccessM1 implements PluginsDataAccess {

    private class RewardPointParams {
        RewardData data;
        String currency_id = null;
        String customer_id = null;
        String store_id = null;
        String till_id = null;
        String quote_id = null;
    }

    private class GiftCardParams {
        String coupon_code;
        String currency_id = null;
        String customer_id = null;
        String store_id = null;
        String till_id = null;
        String quote_id = null;
    }

    private class RewardData {
        String rule_id = "rate";
        int use_point;
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
    public Checkout applyRewarPoint(Checkout checkout, RewardPoint rewardPoint) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PLUGIN_APPLY_REWARD_POINT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            RewardPointParams rewardPointParams = new RewardPointParams();
            rewardPointParams.currency_id = ConfigUtil.getCurrentCurrency().getCode();
            rewardPointParams.customer_id = rewardPoint.getCustomerId();
            rewardPointParams.store_id = checkout.getStoreId();
            rewardPointParams.till_id = ConfigUtil.getPointOfSales().getID();
            rewardPointParams.quote_id = rewardPoint.getQuoteId();
            RewardData rewardData = new RewardData();
            rewardData.use_point = rewardPoint.getAmount();
            rewardPointParams.data = rewardData;

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(rewardPointParams);
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
    public Checkout addGiftCard(Checkout checkout, GiftCard giftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PLUGIN_ADD_GIFTCARD);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            GiftCardParams giftCardParams = new GiftCardParams();
            giftCardParams.coupon_code = giftCard.getCouponCode();
            giftCardParams.currency_id = ConfigUtil.getCurrentCurrency().getCode();
            giftCardParams.customer_id = giftCard.getCustomerId();
            giftCardParams.store_id = checkout.getStoreId();
            giftCardParams.quote_id = giftCard.getQuoteId();
            giftCardParams.till_id = ConfigUtil.getPointOfSales().getID();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(giftCardParams);
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
    public Checkout removeGiftCard(Checkout checkout, GiftCardRemoveParam giftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_PLUGIN_REMOVE_GIFTCARD);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            GiftCardParams giftCardParams = new GiftCardParams();
            giftCardParams.coupon_code = giftCard.getCode();
            giftCardParams.currency_id = ConfigUtil.getCurrentCurrency().getCode();
            giftCardParams.customer_id = giftCard.getCustomerId();
            giftCardParams.store_id = checkout.getStoreId();
            giftCardParams.quote_id = giftCard.getQuoteId();
            giftCardParams.till_id = ConfigUtil.getPointOfSales().getID();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(giftCardParams);
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
}
