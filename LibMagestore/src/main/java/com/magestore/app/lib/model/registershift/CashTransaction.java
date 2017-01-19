package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CashTransaction extends Model {
    String getCreatedAt();
    void setCreateAt(String strCreateAt);
    String getNote();
    String getOpenShiftTitle();
    void setOpenShiftTitle(String strOpenShiftTitle);
    float getFloatAmount();
    void setFloatAmount(float floatAmount);
    float getBalance();
    String getBalanceTitle();
    void setBalanceTitle(String strBalance);
    boolean getCheckOpenShift();
    void setCheckOpenShift(boolean checkOpenShift);
    float getValue();
    boolean getCheckNote();
}
