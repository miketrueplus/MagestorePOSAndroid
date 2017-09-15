package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface SaleSummary extends Model {
    String getPaymentMethod();
    void setPaymentMethod(String strPaymentMethod);
    String getMethodTitle();
    void setMethodTitle(String strMethodTitle);
    float getPaymentAmount();
    float getBasePaymentAmount();
    void setBasePaymentAmount(float fBasePaymentAmount);
}
