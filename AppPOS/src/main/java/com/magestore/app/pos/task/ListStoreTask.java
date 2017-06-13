package com.magestore.app.pos.task;

import android.content.Context;

import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.lib.task.TaskListener;
import com.magestore.app.pos.R;

import java.util.List;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class ListStoreTask extends AsyncTaskAbstractTask<Void, Void, List<PointOfSales>> {
    private Context context;
    /**
     * Khởi tạo với listener
     *
     * @param listener
     */
    public ListStoreTask(Context context, TaskListener listener) {
        super(listener);
        this.context = context;
    }

    @Override
    protected List<PointOfSales> doInBackground(Void... voids) {
        try {
            // điều chỉnh domain
            // Gọi use case đăng nhập
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            UserService userService = serviceFactory.generateUserService();
            userService.resetListPos();
            List<PointOfSales> listPos = userService.getListPos();
            if (listPos != null) {
                if (listPos.size() > 0) {
                    PointOfSales pointOfSales = userService.createPointOfSales();
                    pointOfSales.setPosId("");
                    pointOfSales.setPosName(context.getString(R.string.select_pos));
                    if (!checkPos(listPos)) {
                        listPos.add(0, pointOfSales);
                    }
                    return listPos;
                } else {
                    return userService.getListPos();
                }
            } else {
                return userService.getListPos();
            }
        } catch (Exception e) {
            cancel(e, true);
            return null;
        }
    }

    private boolean checkPos(List<PointOfSales> listPos) {
        boolean isContains = false;
        for (PointOfSales pointOfSales : listPos) {
            if (pointOfSales.getID().equals("")) {
                isContains = true;
            }
        }
        return isContains;
    }
}
