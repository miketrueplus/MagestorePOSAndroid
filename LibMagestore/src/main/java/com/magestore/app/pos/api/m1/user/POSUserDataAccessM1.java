package com.magestore.app.pos.api.m1.user;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.config.PosConfigTaxClass;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListPointOfSales;
import com.magestore.app.pos.parse.gson2pos.Gson2PosStoreParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 8/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSUserDataAccessM1 extends POSAbstractDataAccessM1 implements UserDataAccess {
    static final String strfalse = "false\n";

    private class Wrap {
        User staff;
    }

    private class POSListTaxClassDataAccess {
        List<PosConfigTaxClass> tax_class;
        List<PosCheckoutPayment> payment;
    }

    private class Pos {
        String pos_id;
        String staff_id;
        String location_id;
        String current_session_id;
    }

    private class POSCheckPlatformDataAccess {
        String platform;
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
            statement.prepareQuery(POSAPIM1.REST_CHECK_PLATFORM);

            rp = statement.execute();

            String respone = rp.readResult2String();
            Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
            Gson gson = implement.createGson();
            POSCheckPlatformDataAccess checkPlatformClass = gson.fromJson(respone, POSCheckPlatformDataAccess.class);
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
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_LOGIN);

            Wrap wrap = new Wrap();
            wrap.staff = user;
            rp = statement.execute(wrap);

            String respone = rp.readResult2String();
            if (respone == null) {
                return "";
            }
            if (strfalse.equals(respone) || respone.equals("false")) {
                return respone;
            }
            JSONObject json = new JSONObject(respone);
            String session_id = json.getString("session_id");
            if (json.has("webpos_data")) {
                JSONObject webpos_data = json.getJSONObject("webpos_data");
                Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
                Gson gson = implement.createGson();
                POSListTaxClassDataAccess taxClass = gson.fromJson(webpos_data.toString(), POSListTaxClassDataAccess.class);
                if (taxClass.tax_class != null && taxClass.tax_class.size() > 0) {
                    List<ConfigTaxClass> listTax = (List<ConfigTaxClass>) (List<?>) taxClass.tax_class;
                    ConfigUtil.setConfigTaxClass(listTax);
                }
                if (taxClass.payment != null && taxClass.payment.size() > 0) {
                    List<CheckoutPayment> listPayment = (List<CheckoutPayment>) (List<?>) taxClass.payment;
                    ConfigUtil.setListPayment(listPayment);
                }
            }

            if (json.has("webpos_config")) {
                JSONObject webpos_config = json.getJSONObject("webpos_config");

                if (webpos_config.has("tax/cart_display/price")) {
                    String tax_cart = webpos_config.getString("tax/cart_display/price");
                    boolean tax_cart_display = false;
                    if (tax_cart.equals("1")) {
                        tax_cart_display = true;
                    }
                    ConfigUtil.setTaxCartDisplay(tax_cart_display);
                }

                if (webpos_config.has("plugins_config")) {
                    JSONObject json_plugins = webpos_config.getJSONObject("plugins_config");
                    if (json_plugins.has("os_store_credit")) {
                        JSONObject json_store_credit = json_plugins.getJSONObject("os_store_credit");
                        if (json_store_credit.has("customercredit/general/enable")) {
                            String os_store_credit = json_store_credit.getString("customercredit/general/enable");
                            if (os_store_credit.equals("1")) {
                                ConfigUtil.setEnableStoreCredit(true);
                            }
                        }
                    }

                    if (json_plugins.has("os_reward_points")) {
                        JSONObject json_reward_point = json_plugins.getJSONObject("os_reward_points");
                        if (json_reward_point.has("rewardpoints/general/enable")) {
                            String os_reward_points = json_reward_point.getString("rewardpoints/general/enable");
                            if (os_reward_points.equals("1")) {
                                ConfigUtil.setEnableRewardPoint(true);
                            }
                        }
                    }

                    if (json_plugins.has("os_gift_card")) {
                        JSONObject json_gift_card = json_plugins.getJSONObject("os_gift_card");
                        if (json_gift_card.has("giftvoucher/general/active")) {
                            String os_gift_card = json_gift_card.getString("giftvoucher/general/active");
                            if (os_gift_card.equals("1")) {
                                ConfigUtil.setEnableGiftCard(true);
                            }
                        }
                    }
                }
            }

            if (!StringUtil.isNullOrEmpty(session_id)) {
                return session_id;
            } else {
                return null;
            }
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_REGISTER_SHIFTS_GET_LISTING_POS);
            paramBuilder = statement.getParamBuilder()
                    .setSortOrderASC("till_name")
                    .setFilter("user_id", ConfigUtil.getStaff().getID())
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListPointOfSales.class);
            Gson2PosListPointOfSales listPos = (Gson2PosListPointOfSales) rp.doParse();
            List<PointOfSales> mListPos = (List<PointOfSales>) (List<?>) (listPos.items);
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
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_POS_ASSIGN);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            Pos posEntity = new Pos();
            posEntity.pos_id = pos_id;
            posEntity.staff_id = ConfigUtil.getStaff().getID();
            posEntity.location_id = ConfigUtil.getLocationId();
            posEntity.current_session_id = POSDataAccessSessionM1.REST_SESSION_ID;

            rp = statement.execute(posEntity);
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
}
