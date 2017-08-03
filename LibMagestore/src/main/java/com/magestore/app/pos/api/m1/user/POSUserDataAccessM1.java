package com.magestore.app.pos.api.m1.user;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.util.StringUtil;

import org.json.JSONArray;
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
            statement.prepareQuery(POSAPIM1.REST_LOGIN);

            Wrap wrap = new Wrap();
            wrap.staff = user;
            rp = statement.execute(wrap);

            String respone = rp.readResult2String();
            JSONObject json = new JSONObject(respone);
            String session_id = json.getString("session_id");
            JSONObject webpos_data = json.getJSONObject("webpos_data");
            JSONArray tax_class = webpos_data.getJSONArray("tax_class");

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
        return null;
    }

    @Override
    public void resetListPos() throws ParseException, ConnectionException, DataAccessException, IOException {

    }

    @Override
    public boolean requestAssignPos(String pos_id) throws ParseException, ConnectionException, DataAccessException, IOException {
        return false;
    }
}
