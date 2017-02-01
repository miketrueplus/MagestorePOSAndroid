package com.magestore.app.lib.view;

import com.magestore.app.lib.controller.Controller;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface MagestoreView<TController extends Controller> {
    /**
     * Chỉ định controller
     * @param controller
     */
    void setController(TController controller);
    TController getController();

    /**
     * Hiện thông báo lỗi
     * @param msg
     */
    void showErrorMsg(String msg);
    void showErrorMsg(Exception exp);

    /**
     * Hiển thị tiến trình
     * @param blnShow
     */
    void showProgress(boolean blnShow);
}
