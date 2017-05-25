package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderWebposPayment extends PosAbstractModel implements OrderWebposPayment {
    String payment_id;
    String order_id;
    float base_payment_amount = 0;
    float payment_amount = 0;
    float base_display_amount = 0;
    float display_amount = 0;
    String method;
    String method_title;

    @Override
    public String getID() {
        return payment_id;
    }

    @Override
    public float getBasePaymentAmount() {
        return base_payment_amount;
    }

    @Override
    public void setBasePaymentAmount(float fBasePaymentAmount) {
        base_payment_amount = fBasePaymentAmount;
    }

    @Override
    public float getPaymentAmount() {
        return payment_amount;
    }

    @Override
    public void setPaymentAmount(float fPaymentAmount) {
        payment_amount = fPaymentAmount;
    }

    @Override
    public float getBaseDisplayAmount() {
        return base_display_amount;
    }

    @Override
    public void setBaseDisplayAmount(float fBaseDisplayAmount) {
        base_display_amount = fBaseDisplayAmount;
    }

    @Override
    public float getDisplayAmount() {
        return display_amount;
    }

    @Override
    public void setDisplayAmount(float fDisplayAmount) {
        display_amount = fDisplayAmount;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setMethod(String strMethod) {
        method = strMethod;
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
    public String getOrderId() {
        return order_id;
    }

    @Override
    public void setOrderId(String strOrderId) {
        order_id = strOrderId;
    }
}
