package com.magestore.app.lib.controller;

/**
 * Created by Mike on 12/25/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface ControllerListener<Params, Progress, Result> {
    void onPreController(Controller controller);
    void onPostController(Controller controller, Result result);
    void onCancelController(Controller controller, Exception exp);
    void onProgressController(Controller controller, Progress ...progress);
}
