package com.magestore.app.lib.ui;

import com.magestore.app.lib.controller.Controller;

import java.util.ResourceBundle;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface PosUI {
    void showErrorMsg(String strMsg);
    void showErrorMsg(Exception ex);
    String getSharedValue(String strKey, String strDefault);
    void saveSharedValue(String strKey, String strValue);
    void showProgress(boolean show);
    void showUI(PosUI ui);
    void close();

//    void onPreController(Controller controller);
//    void onPostController(Controller controller);
//    void onCancelController(Controller controller);
//    void onProgressController(Controller controller);
}
