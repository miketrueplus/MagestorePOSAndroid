package com.magestore.app.lib.gateway;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.entity.User;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface UserGateway extends Gateway {
    /**
     * Thực hiện login với username và password
     * @param domain
     * @param username
     * @param password
     * @return Trả lại user id đăng nhập
     * @throws GatewayException
     */
    String login(String domain, String username, String password) throws ParseException, ConnectionException, GatewayException, IOException;
    String buildPOSBaseURL(String strDomain);
}