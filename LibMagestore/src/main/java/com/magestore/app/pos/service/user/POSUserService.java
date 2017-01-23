package com.magestore.app.pos.service.user;

import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.model.user.PosUser;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;

/**
 * Thực hiện các use case liên quan đến user account như đăng nhập
 * Created by Mike on 12/12/2016.
 */

public class POSUserService extends AbstractService implements UserService {
    static final String strfalse = "false\n";
    public static POSDataAccessSession session;

    /**
     * Thực hiện đăng nhập, check đúng sai
     * @param domain
     * @param username
     * @param password
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public boolean doLogin(String domain, String username, String password) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Gọi user gateway
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        String strBaseURL = userGateway.buildPOSBaseURL(domain);

        // Thực hiện đăng nhập
        User user = new PosUser();
        user.setUserName(username);
        user.setPasswords(password);
        String str = userGateway.login(strBaseURL, user);
        boolean success = (str != null && (!strfalse.equals(str)));

        // Lưu lại session ID
        if (success) {
            session.REST_SESSION_ID = str.trim().replace("\"", "");
            session.REST_BASE_URL = strBaseURL;

            // Lấy config hệ thống và lưu lại
            ServiceFactory serviceFactory = ServiceFactory.getFactory(getContext());
            ConfigService configService = serviceFactory.generateConfigService();
            configService.retrieveConfig();
        }

        // Nếu username về đúng, login thành công
        return success;
    }

    /**
     * Logout khỏi user
     */
    public void doLogout() {
        session = null;
    }

    /**
     * Kiểm tra xem user đã login chưa
     * @return
     */
    public boolean isLogin() {
        return session != null;
    }
}