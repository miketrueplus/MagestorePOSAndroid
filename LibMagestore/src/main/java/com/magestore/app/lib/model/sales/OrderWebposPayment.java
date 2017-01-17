package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderWebposPayment extends Model {
    float getBasePaymentAmount();
    float getPaymentAmount();
    float getBaseDisplayAmount();
    float getDisplayAmount();
    String getMethod();
    String getMethodTitle();
    String getOrderId();
}
