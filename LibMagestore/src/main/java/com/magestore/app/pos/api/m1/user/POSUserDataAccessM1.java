package com.magestore.app.pos.api.m1.user;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
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
    private class Wrap {
        User staff;
    }

    private class POSListTaxClassDataAccess {
        List<PosConfigTaxClass> tax_class;
    }

    private class Pos {
        String pos_id;
        String staff_id;
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
            JSONObject json = new JSONObject(respone);
            String session_id = json.getString("session_id");
            JSONObject webpos_data = json.getJSONObject("webpos_data");
            Gson2PosStoreParseImplement implement = new Gson2PosStoreParseImplement();
            Gson gson = implement.createGson();
            POSListTaxClassDataAccess taxClass = gson.fromJson(webpos_data.toString(), POSListTaxClassDataAccess.class);
            if (taxClass.tax_class != null && taxClass.tax_class.size() > 0) {
                List<ConfigTaxClass> listTax = (List<ConfigTaxClass>) (List<?>) taxClass.tax_class;
                ConfigUtil.setConfigTaxClass(listTax);
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
                    .setSortOrderASC("pos_name")
                    .setFilter("staff_id", ConfigUtil.getStaff().getID())
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
