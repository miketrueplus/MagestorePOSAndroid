package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.CheckoutTotals;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosCheckoutToTals extends PosAbstractModel implements CheckoutTotals {
    String code;
    String title;
    float value;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String strTitle) {
        title = strTitle;
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
