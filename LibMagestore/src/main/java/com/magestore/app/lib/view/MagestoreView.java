package com.magestore.app.lib.view;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.item.ModelView;

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

    void showErrorMsgWithReload(String strMsg);

    void showErrorMsgWithReload(Exception exp);

    /**
     * Hiện thông báo lỗi
     * @param msg
     */
    void showErrorMsg(String msg);
    void showErrorMsg(Exception exp);

    void hideErrorMsg(Exception exp);

    /**
     * Hiển thị tiến trình
     * @param blnShow
     */
    void showProgress(boolean blnShow);

    void showProgressLoadRefresh(boolean show);

    void showProgressLoadingMore(boolean show);

    void hideAllProgressBar();

    void initModel();

    void initValue();

    // hiện thông báo lỗi
    void showWarning(String strMsg);
    void hideWarning();

    ModelView createModelView(Model model);
}
