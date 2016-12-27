package com.magestore.app.lib.controller;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Controller<Params, Progress, Result> {
    void setListener(ControllerListener<Params, Progress, Result> listener);
}
