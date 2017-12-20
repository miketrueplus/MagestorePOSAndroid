package com.magestore.app.pos.api.odoo.user;

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
import com.magestore.app.lib.model.registershift.DataPointOfSales;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.registershift.PosDataPointOfSales;
import com.magestore.app.pos.model.registershift.PosPointOfSales;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListPointOfSales;
import com.magestore.app.pos.parse.gson2pos.Gson2PosStoreParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSUserDataAccessOdoo extends POSAbstractDataAccessOdoo implements UserDataAccess {
    private class POSCheckPlatformDataAccess {
        String platform;
        String website_id;
    }

    private class Wrap {
        UserEntity staff;
    }

    private class UserEntity {
        String email;
        String password;
    }

    private class LoginRespone {
        String Token;
    }

    public class Gson2PosParseModel extends Gson2PosAbstractParseImplement {
        @Override
        protected Gson createGson() {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization();
            builder.registerTypeAdapter(new TypeToken<List<PosPointOfSales>>() {
            }
                    .getType(), new PosConverter());
            return builder.create();
        }

        private String POS_ID = "id";
        private String POS_NAME = "name";
        private String POS_SESSION_USENAME = "pos_session_username";
        private String POS_TAX_INCL = "iface_tax_included";
        private String POS_RECEIPT_HEADER = "receipt_header";
        private String POS_CASH_CONTROL = "cash_control";
        private String POS_TIP_PRODUCT_ID = "tip_product_id";
        private String POS_CURRENT_SESSION_STATE = "current_session_state";
        private String POS_CREATE_DATE = "create_date";
        private String POS_PRINT_AUTO = "iface_print_auto";
        private String POS_INVOICE = "iface_invoicing";
        private String POS_RECEIPT_FOOTER = "receipt_footer";
        private String POS_CASH_DRAWER = "iface_cashdrawer";
        private String POS_DISCOUNT = "iface_discount";
        private String POS_DISCOUNT_PC = "discount_pc";
        private String POS_DISCOUNT_PRODUCT_ID = "discount_product_id";

        public class PosConverter implements JsonDeserializer<List<PosPointOfSales>> {
            @Override
            public List<PosPointOfSales> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<PosPointOfSales> listPos = new ArrayList<>();
                if (json.isJsonArray()) {
                    JsonArray arr_pos = json.getAsJsonArray();
                    if (arr_pos != null && arr_pos.size() > 0) {
                        for (JsonElement el_pos : arr_pos) {
                            JsonObject obj_pos = el_pos.getAsJsonObject();
                            PosPointOfSales pos = new PosPointOfSales();
                            String id = obj_pos.remove(POS_ID).getAsString();
                            pos.setPosId(id);
                            String name = obj_pos.remove(POS_NAME).getAsString();
                            pos.setPosName(name);
                            boolean cash_control = obj_pos.remove(POS_CASH_CONTROL).getAsBoolean();
                            pos.setCashControl(cash_control);
                            if (obj_pos.has(POS_DISCOUNT)) {
                                boolean pos_discount = obj_pos.remove(POS_DISCOUNT).getAsBoolean();
                                pos.setIfaceDiscount(pos_discount);
                            }
                            if (obj_pos.has(POS_DISCOUNT_PC)) {
                                float pos_discount_pc = obj_pos.remove(POS_DISCOUNT_PC).getAsFloat();
                                pos.setDiscountPC(pos_discount_pc);
                            }
                            if (obj_pos.has(POS_DISCOUNT_PRODUCT_ID)) {
                                String pos_discount_product_id = obj_pos.remove(POS_DISCOUNT_PRODUCT_ID).getAsString();
                                if (!StringUtil.checkJsonData(pos_discount_product_id)) {
                                    pos.setDiscountProductId(pos_discount_product_id);
                                }
                            }
                            listPos.add(pos);
                        }
                    }
                }
                return listPos;
            }
        }
    }

    @Override
    public String checkPlatform(String domain, String username, String password) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = ConnectionFactory.generateConnection(getContext(), domain, username, password);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CHECK_PLATFORM);

            rp = statement.execute();

            String respone = rp.readResult2String();
            Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
            Gson gson = implement.createGson();
            POSCheckPlatformDataAccess checkPlatformClass = gson.fromJson(respone, POSCheckPlatformDataAccess.class);
            ConfigUtil.setWebSiteId(checkPlatformClass.website_id);
            return checkPlatformClass.platform;
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
    public String login(String domain, String proxyUser, String proxyPassword, User user) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = ConnectionFactory.generateConnection(getContext(), domain, proxyUser, proxyPassword);
//            connection = MagestoreConnection.getConnection(domain, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_LOGIN);

            Wrap wrap = new Wrap();
            UserEntity userEntity = new UserEntity();
            userEntity.email = user.getUserName();
            userEntity.password = user.getPasswords();
            wrap.staff = userEntity;

            rp = statement.execute(wrap);
            String respone = rp.readResult2String();
            Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
            Gson gson = implement.createGson();
            LoginRespone loginRespone = gson.fromJson(respone, LoginRespone.class);
            return loginRespone.Token;
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
    public void doLogout() throws ParseException, ConnectionException, DataAccessException, IOException {

    }

    @Override
    public List<Store> retrieveStore() throws ParseException, ConnectionException, DataAccessException, IOException {
        return null;
    }

    @Override
    public List<Store> getListStore() throws ParseException, ConnectionException, DataAccessException, IOException {
        return null;
    }

    @Override
    public List<PointOfSales> retrievePos() throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_REGISTER_SHIFTS_GET_LISTING_POS);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            paramBuilder = statement.getParamBuilder()
                    .setSortOrderASC("name");

            rp = statement.execute();
            rp.setParseImplement(new Gson2PosParseModel());
            rp.setParseModel(PosDataPointOfSales.class);
            DataPointOfSales listPos = (DataPointOfSales) rp.doParse();
            List<PointOfSales> mListPos = listPos.getItems();
            return mListPos;
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        } finally {
//            // đóng result reading
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
    public void resetListPos() throws ParseException, ConnectionException, DataAccessException, IOException {

    }

    @Override
    public boolean requestAssignPos(String pos_id) throws ParseException, ConnectionException, DataAccessException, IOException {
        // TODO: fake assign pos success
        return true;
    }

    // TODO fake data
    private List<PointOfSales> getPOSFake() {
        PointOfSales pointOfSales = new PosPointOfSales();
        pointOfSales.setPosId("2");
        pointOfSales.setPosName("Cash Drawer 02");
        pointOfSales.setStoreId("1");
        pointOfSales.setLocationId("1");
        List<PointOfSales> list = new ArrayList<>();
        list.add(pointOfSales);
        return list;
    }
}
