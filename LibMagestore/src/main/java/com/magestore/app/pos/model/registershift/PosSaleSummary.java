package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosSaleSummary extends PosAbstractModel implements SaleSummary {
    String payment_method;
    float payment_amount;
    float base_payment_amount;
    String method_title;

    @Override
    public String getPaymentMethod() {
        return payment_method;
    }

    @Override
    public void setPaymentMethod(String strPaymentMethod) {
        payment_method = strPaymentMethod;
    }

    @Override
    public String getMethodTitle() {
        return method_title;
    }

    @Override
    public void setMethodTitle(String strMethodTitle) {
        method_title = strMethodTitle;
    }

    @Override
    public float getPaymentAmount() {
        return payment_amount;
    }

    @Override
    public float getBasePaymentAmount() {
        return base_payment_amount;
    }

    @Override
    public void setBasePaymentAmount(float fBasePaymentAmount) {
        base_payment_amount = fBasePaymentAmount;
    }
}
