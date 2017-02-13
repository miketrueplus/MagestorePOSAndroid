package com.magestore.app.lib.observe;

import com.magestore.app.lib.controller.Controller;

/**
 * Created by Mike on 2/12/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class GenericState<TController extends Controller> implements State<TController> {
    TController mController;
    String mStateCode;

    public GenericState(TController controller, String stateCode) {
        setController(controller);
        setStateCode(stateCode);
    }



    public TController getController() {
        return mController;
    }

    public String getStateCode() {
        return mStateCode;
    }

    @Override
    public void setController(TController controller) {
        mController = controller;
    }

    @Override
    public void setStateCode(String code) {
        mStateCode = code;
    }
}
