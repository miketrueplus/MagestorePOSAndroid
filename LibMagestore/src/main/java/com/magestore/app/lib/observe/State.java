package com.magestore.app.lib.observe;

import android.view.View;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.view.MagestoreView;

import java.util.Map;

/**
 * Các state để truyền giữa các controller
 * Created by Mike on 2/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface State<TController extends Controller> {
    public TController getController();
    public String getStateCode();
    void setController(TController controller);
    void setStateCode(String code);
}