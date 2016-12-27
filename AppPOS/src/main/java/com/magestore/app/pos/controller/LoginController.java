package com.magestore.app.pos.controller;

import android.content.Intent;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.User;
import com.magestore.app.lib.usecase.UseCaseFactory;
import com.magestore.app.lib.usecase.UserUseCase;
import com.magestore.app.lib.usecase.pos.POSUserUseCase;
import com.magestore.app.pos.R;
import com.magestore.app.pos.SalesListActivity;
import com.magestore.app.pos.ui.LoginUI;

/**
 * Điều khiển tác vụ login với các tương tác giao diện
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class LoginController extends AsyncTaskAbstractController<Void, Void, Boolean> {
    private String mUserName;
    private String mPassword;
    private String mDomain;

    /**
     * Khởi tạo controller
     * @param listener
     * @param domain
     * @param username
     * @param password
     */
    public LoginController(ControllerListener listener, String domain, String username, String password) {
        super(listener);
        mDomain = domain;
        mUserName = username;
        mPassword = password;
    }

    /**
     * Thực hiện quá trình đăng nhập
     * @param params
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            // Gọi use case đăng nhập
            UserUseCase userAccount = UseCaseFactory.generateUserUseCase(null, null);
            return userAccount.doLogin(mDomain, mUserName, mPassword);
        } catch (Exception e) {
            cancel(e, true);
            return false;
        }
    }
}
