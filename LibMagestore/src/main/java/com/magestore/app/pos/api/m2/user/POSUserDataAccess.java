package com.magestore.app.pos.api.m2.user;

import com.magestore.app.lib.BuildConfig;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.user.PosUser;

import java.io.IOException;
import java.text.ParseException;

/**
 * Thực thi các API kết nối đến Magestore Server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSUserDataAccess extends POSAbstractDataAccess implements UserDataAccess {
    private class LoginEntity {
        PosUser staff = new PosUser();
    }

    public POSUserDataAccess() {

    }

    /**
     * Dựng base url từ domain do user nhập
     * @param strDomain
     * @return
     */
    @Override
    public String buildPOSBaseURL(String strDomain) {
        if (strDomain == null) return BuildConfig.REST_BASE_URL;
        StringBuilder stringBuilder = new StringBuilder();
        if (BuildConfig.REST_BASE_URL.startsWith("http://"))
            stringBuilder.append("http://");
        else
            stringBuilder.append("https://");
        stringBuilder.append(strDomain);
        if (strDomain.indexOf("/") < 0) stringBuilder.append("/webpos");
        return stringBuilder.toString();
    }

    /**
     * Login, xem đúng user name và password
     * Nếu đúng trả lại session id, nếu k0 trả lại "false"
     * HTTP POST
     * URL =
     * Param =
     * @param domain
     * @param username
     * @param password
     * @return
     * @throws ParseException
     * @throws ConnectionException
     * @throws DataAccessException
     * @throws IOException
     */
    @Override
    public String login(String domain, String username, String password) throws ParseException, ConnectionException, DataAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
//            connection = MagestoreConnection.getConnection(domain, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_LOGIN);

            LoginEntity loginEntity = new LoginEntity();
            loginEntity.staff.setUserName(username);
            loginEntity.staff.setPasswords(password);
            rp = statement.execute(loginEntity);
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

//    @Override
//    public String login(String domain, String username, String password) throws ParseException, ConnectionException, DataAccessException, IOException {
//        LoginEntity loginEntity = new LoginEntity();
//        loginEntity.staff.setUserName(username);
//        loginEntity.staff.setPasswords(password);
//        return doApi2String(POSDataAccessSession.REST_LOGIN, loginEntity);
//    }
}
