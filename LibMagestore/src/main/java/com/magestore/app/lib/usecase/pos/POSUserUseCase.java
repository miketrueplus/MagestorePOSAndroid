package com.magestore.app.lib.usecase.pos;

import android.util.Log;

import com.magestore.app.lib.entity.User;
import com.magestore.app.lib.entity.pos.PosUser;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.UserGateway;
import com.magestore.app.lib.gateway.pos.POSAPI;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.gateway.pos.POSGatewaySession;
import com.magestore.app.lib.usecase.UseCase;
import com.magestore.app.lib.usecase.UserUseCase;

import java.io.IOException;
import java.text.ParseException;

/**
 * Thực hiện các use case liên quan đến user account như đăng nhập
 * Created by Mike on 12/12/2016.
 */

public class POSUserUseCase extends AbstractUseCase implements UserUseCase {
    static final String strfalse = "false\n";
    public static POSGatewaySession session;

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
        GatewayFactory factory = GatewayFactory.getFactory(POSGatewayFactory.class);
        UserGateway userGateway = factory.generateUserGateway();
        String strBaseURL = userGateway.buildPOSBaseURL(domain);

        // Thực hiện đăng nhập
        String str = userGateway.login(strBaseURL, username, password);
        boolean success = (str != null && (!strfalse.equals(str)));

        // Lưu lại session ID
        if (success) {
            session.REST_SESSION_ID = str.trim().replace("\"", "");
            session.REST_BASE_URL = strBaseURL;

            //TODO: remov khi release
            Log.d("Login", session.REST_SESSION_ID);
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