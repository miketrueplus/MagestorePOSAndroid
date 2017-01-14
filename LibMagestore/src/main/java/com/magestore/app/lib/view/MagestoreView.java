package com.magestore.app.lib.view;

import com.magestore.app.lib.controller.Controller;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface MagestoreView<TController extends Controller> {
    void setController(TController controller);
    TController getController();
    void showErrorMsg(String msg);
    void showErrorMsg(Exception exp);
}
