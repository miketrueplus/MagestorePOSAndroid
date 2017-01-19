package com.magestore.app.lib.adapter;

import com.magestore.app.lib.model.Model;

import java.lang.reflect.InvocationTargetException;

/**
 * Chuyển đổi từ Model sang 1 view và ngược lại
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface AdapterView {
    void setModel(Model model);
    void fromView() throws InvocationTargetException, IllegalAccessException;
    void toView() throws InvocationTargetException, IllegalAccessException;
}
