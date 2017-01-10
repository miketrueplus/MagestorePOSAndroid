package com.magestore.app.lib.resourcemodel.user;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface UserDataAccess extends DataAccess {
    /**
     * Thực hiện login với username và password
     * @param domain
     * @param username
     * @param password
     * @return Trả lại user id đăng nhập
     * @throws DataAccessException
     */
    String login(String domain, String username, String password) throws ParseException, ConnectionException, DataAccessException, IOException;
    String buildPOSBaseURL(String strDomain);
}