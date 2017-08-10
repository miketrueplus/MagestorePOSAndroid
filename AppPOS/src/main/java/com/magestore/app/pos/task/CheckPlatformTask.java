package com.magestore.app.pos.task;

import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.lib.task.TaskListener;

/**
 * Created by Johan on 8/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckPlatformTask extends AsyncTaskAbstractTask<Void, Void, Boolean> {
    private String mDomain;
    private String mUserName;
    private String mPassword;
    /**
     * Khởi tạo với listener
     *
     * @param listener
     */
    public CheckPlatformTask(TaskListener listener, String domain, String username, String password) {
        super(listener);
        mDomain = domain;
        mUserName = username;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // điều chỉnh domain
            // Gọi use case đăng nhập
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            UserService userAccount = serviceFactory.generateUserService();
            return userAccount.checkPlatform(mDomain, mUserName, mPassword);
        } catch (Exception e) {
            cancel(e, true);
            return false;
        }
    }
}
