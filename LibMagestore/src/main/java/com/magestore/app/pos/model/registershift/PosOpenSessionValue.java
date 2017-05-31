package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 5/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOpenSessionValue extends PosAbstractModel implements OpenSessionValue {
    float value;
    int amount;
    float subtotal;


    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float fValue) {
        value = fValue;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int iAmount) {
        amount = iAmount;
    }

    @Override
    public float getSubtotal() {
        return subtotal;
    }

    @Override
    public void setSubtotal(float fSubtotal) {
        subtotal = fSubtotal;
    }
}
