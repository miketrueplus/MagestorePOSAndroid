package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CashBox extends Model {
    int getQty();
    void setQty(int iQty);
    float getValue();
    void setValue(float fValue);
}
