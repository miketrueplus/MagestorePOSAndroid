package com.magestore.app.pos.api.odoo.user;

import com.google.gson.Gson;
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
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.parse.gson2pos.Gson2PosStoreParseImplement;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSUserDataAccessOdoo extends POSAbstractDataAccessM1 implements UserDataAccess {
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
            statement.prepareQuery(POSAPIOdoo.REST_CHECK_PLATFORM);

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
        return null;
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
