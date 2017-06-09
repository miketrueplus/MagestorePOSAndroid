package com.magestore.app.pos.task;

import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.lib.task.TaskListener;

/**
 * Created by Johan on 6/9/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class AssignPosTask extends AsyncTaskAbstractTask<Void, Void, Boolean> {
    private String pos_id;

    /**
     * Khởi tạo với listener
     *
     * @param listener
     */
    public AssignPosTask(TaskListener<Void, Void, Boolean> listener, String pos_id) {
        super(listener);
        this.pos_id = pos_id;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            // điều chỉnh domain
            // Gọi use case đăng nhập
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            UserService userAccount = serviceFactory.generateUserService();
            return userAccount.requestAssignPos(pos_id);
        } catch (Exception e) {
            cancel(e, true);
            return false;
        }
    }
}
