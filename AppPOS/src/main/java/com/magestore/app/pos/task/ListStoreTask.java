package com.magestore.app.pos.task;

import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.lib.task.TaskListener;

import java.util.List;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class ListStoreTask extends AsyncTaskAbstractTask<Void, Void, List<PointOfSales>> {
    /**
     * Khởi tạo với listener
     *
     * @param listener
     */
    public ListStoreTask(TaskListener listener) {
        super(listener);
    }

    @Override
    protected List<PointOfSales> doInBackground(Void... voids) {
        try {
            // điều chỉnh domain
            // Gọi use case đăng nhập
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            UserService userService = serviceFactory.generateUserService();
            userService.resetListPos();
            return userService.getListPos();
        } catch (Exception e) {
            cancel(e, true);
            return null;
        }
    }
}
