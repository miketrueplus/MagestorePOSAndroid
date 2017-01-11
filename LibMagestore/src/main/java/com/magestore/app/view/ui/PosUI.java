package com.magestore.app.view.ui;

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

//    void onPreController(Task controller);
//    void onPostController(Task controller);
//    void onCancelController(Task controller);
//    void onProgressController(Task controller);
}
