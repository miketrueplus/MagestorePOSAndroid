package com.magestore.app.pos.task;

import com.magestore.app.lib.task.TaskListener;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;

/**
 * Điều khiển tác vụ login với các tương tác giao diện
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class LoginTask extends AsyncTaskAbstractTask<Void, Void, Boolean> {
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
    public LoginTask(TaskListener listener, String domain, String username, String password) {
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
            // điều chỉnh domain
            // Gọi use case đăng nhập
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            UserService userAccount = serviceFactory.generateUserService();
            return userAccount.doLogin(mDomain, "", "", mUserName, mPassword);
        } catch (Exception e) {
            cancel(e, true);
            return false;
        }
    }
}
