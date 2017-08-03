package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderWebposPayment extends Model {
    float getBasePaymentAmount();
    void setBasePaymentAmount(float fBasePaymentAmount);
    float getPaymentAmount();
    void setPaymentAmount(float fPaymentAmount);
    float getBaseDisplayAmount();
    void setBaseDisplayAmount(float fBaseDisplayAmount);
    float getDisplayAmount();
    void setDisplayAmount(float fDisplayAmount);
    String getMethod();
    void setMethod(String strMethod);
    String getMethodTitle();
    void setMethodTitle(String strMethodTitle);
    String getOrderId();
    void setOrderId(String strOrderId);
    String getReferenceNumber();
    void setReferenceNumber(String strReferenceNumber);
}
