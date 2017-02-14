package com.magestore.app.lib.service.user;

import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 12/20/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface UserService extends Service {
    boolean doLogin(String domain, String proxyUser, String proxyPass, String username, String password) throws InstantiationException, IllegalAccessException, IOException, ParseException;
    void doLogout();
    boolean isLogin();
}
