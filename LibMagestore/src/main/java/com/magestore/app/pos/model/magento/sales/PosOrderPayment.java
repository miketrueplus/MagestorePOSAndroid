package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderPayment;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Quản lý order payment
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderPayment extends PosAbstractModel implements OrderPayment {
    String account_status;
    List<String> additional_information;
    float amount_ordered;
    float amount_paid;
    float base_amount_ordered;
    float base_amount_paid;
    float base_shipping_amount;
    float base_shipping_captured;
    String cc_last4;
    String entity_id;
    String method;
    String parent_id;
    float shipping_amount;
    float shipping_captured;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public List<String> getAdditionalInformation() {
        return additional_information;
    }

    @Override
    public float getAmountOrdered() {
        return amount_ordered;
    }

    @Override
    public float getAmountPaid() {
        return amount_paid;
    }

    @Override
    public float getBaseAmountOrdered() {
        return base_amount_ordered;
    }

    @Override
    public float getBaseAmountPaid() {
        return base_amount_paid;
    }

    @Override
    public float getBaseShippingAmount() {
        return base_shipping_amount;
    }

    @Override
    public float getBaseShippingCaptured() {
        return base_shipping_captured;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public float getShippingAmount() {
        return shipping_amount;
    }

    @Override
    public float getShippingCaptured() {
        return shipping_captured;
    }
}
