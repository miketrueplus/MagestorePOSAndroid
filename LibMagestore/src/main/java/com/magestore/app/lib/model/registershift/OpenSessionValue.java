package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 5/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OpenSessionValue extends Model {
    float getValue();
    void setValue(float fValue);
    int getAmount();
    void setAmount(int iAmount);
    float getSubtotal();
    void setSubtotal(float fSubtotal);
}
