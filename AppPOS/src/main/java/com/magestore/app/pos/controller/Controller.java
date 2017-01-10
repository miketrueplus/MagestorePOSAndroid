package com.magestore.app.pos.controller;

import android.app.Activity;
import android.view.View;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.pos.ui.AbstractActivity;

/**
 * Controller quản lý model, dùng service để xử lý
 * và ra lệnh update view
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface Controller<TView> {
    void setView(TView view);
    void setMagestoreContext(MagestoreContext context);
    void setRoolController(Controller controller);
    void setParentController(Controller controller);
    void doStart();
    void doFinish();
    void doShowErrorMsg(Exception exp);
    void doShowProgress(boolean blnShow);
}
