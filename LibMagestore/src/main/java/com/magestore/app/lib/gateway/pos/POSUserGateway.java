package com.magestore.app.lib.gateway.pos;

import android.os.Build;

import com.magestore.app.lib.BuildConfig;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;
import com.magestore.app.lib.entity.User;
import com.magestore.app.lib.entity.pos.PosUser;
import com.magestore.app.lib.gateway.GatewayException;
import com.magestore.app.lib.gateway.UserGateway;
import com.magestore.app.lib.parse.ParseImplement;
import com.magestore.app.lib.parse.gson2pos.Gson2PosAbstractParseImplement;

import java.io.IOException;
import java.text.ParseException;

/**
 * Thực thi các API kết nối đến Magestore Server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSUserGateway extends POSAbstractGateway implements UserGateway {
    private class LoginEntity {
        PosUser staff = new PosUser();
    }

    protected POSUserGateway() {

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
     * @throws GatewayException
     * @throws IOException
     */
    @Override
    public String login(String domain, String username, String password) throws ParseException, ConnectionException, GatewayException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        try {
            // Khởi tạo connection
            connection = MagestoreConnection.getConnection(domain, POSGatewaySession.REST_USER_NAME, POSGatewaySession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_LOGIN);

            LoginEntity loginEntity = new LoginEntity();
            loginEntity.staff.setUserName(username);
            loginEntity.staff.setPasswords(password);
            rp = statement.execute(loginEntity);
            return rp.readResult2String();
        }
        catch (Exception ex) {
            throw new GatewayException(ex);
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
//    public String login(String domain, String username, String password) throws ParseException, ConnectionException, GatewayException, IOException {
//        LoginEntity loginEntity = new LoginEntity();
//        loginEntity.staff.setUserName(username);
//        loginEntity.staff.setPasswords(password);
//        return doApi2String(POSGatewaySession.REST_LOGIN, loginEntity);
//    }
}
