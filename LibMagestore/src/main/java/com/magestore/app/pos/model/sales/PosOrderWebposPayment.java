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
    public float getPaymentAmount() {
        return payment_amount;
    }

    @Override
    public float getBaseDisplayAmount() {
        return base_display_amount;
    }

    @Override
    public float getDisplayAmount() {
        return display_amount;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getMethodTitle() {
        return method_title;
    }

    @Override
    public String getOrderId() {
        return order_id;
    }
}
