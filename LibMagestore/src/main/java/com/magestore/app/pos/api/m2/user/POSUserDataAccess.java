package com.magestore.app.pos.api.m2.user;

import com.google.gson.Gson;
import com.magestore.app.lib.BuildConfig;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.user.PosUser;

import java.io.IOException;
import java.io.ObjectInput;
import java.text.ParseException;

/**
 * Thực thi các API kết nối đến Magestore Server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSUserDataAccess extends POSAbstractDataAccess implements UserDataAccess {
    // wrap object lại và chuyênr thành json
    private class Wrap {User staff;};

    public POSUserDataAccess() {

    }

    /**
     * Login, xem đúng user name và password
     * Nếu đúng trả lại session id, nếu k0 trả lại "false"
     * HTTP POST
     * URL =
     * Param =
     * @param domain
     * @return
     * @throws ParseException
     * @throws ConnectionException
     * @throws DataAccessException
     * @throws IOException
     */
    @Override
    public String login(String domain, String proxyUser, String proxyPassword, final User user) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = ConnectionFactory.generateConnection(getContext(), domain, proxyUser, proxyPassword);
//            connection = MagestoreConnection.getConnection(domain, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_LOGIN);

            Wrap wrap = new Wrap();
            wrap.staff = user;
            rp = statement.execute(wrap);
            return rp.readResult2String();
        }
        catch (Exception ex) {
            throw new DataAccessException(ex);
        }
        finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            // đóng statement
            if (statement != null)statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }
}
