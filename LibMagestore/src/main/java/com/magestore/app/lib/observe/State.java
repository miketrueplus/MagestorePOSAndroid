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
    // get state code
    void setStateCode(String code);
    public String getStateCode();

    // get controller
    void setController(TController controller);
    public TController getController();

    // get tag
    Object getTag(String key);
    void setTag(String key, Object value);
}