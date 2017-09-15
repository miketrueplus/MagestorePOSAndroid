package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.CashBox;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosCashBox extends PosAbstractModel implements CashBox{
    int qty;
    float value;

    @Override
    public int getQty() {
        return qty;
    }

    @Override
    public void setQty(int iQty) {
        qty = iQty;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float fValue) {
        value = fValue;
    }
}
