package com.magestore.app.lib.context;

import android.app.Activity;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.controller.RootController;
import com.magestore.app.lib.model.config.Config;

/**
 * Created by Mike on 12/21/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class MagestoreContext {
    private Activity mActivity;
    private Controller mRootController;

    public void MagestoreContext() {

    }

    public Activity getActivity() {
        return mActivity;
    }

    public Controller getRootController() {
        return mRootController;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setController(Controller controller) {
        mRootController = controller;
    }
}