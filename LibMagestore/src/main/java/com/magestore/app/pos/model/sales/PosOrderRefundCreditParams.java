package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderRefundCreditParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by baonguyen on 28/04/2017.
 */

public class PosOrderRefundCreditParams extends PosAbstractModel implements OrderRefundCreditParams {
    float amount;
    String customer_id;
    String order_increment_id;
    String order_id;

    @Override
    public String getOrderId() {
        return order_id;
    }

    @Override
    public void setOrderId(String strOrderId) {
        order_id = strOrderId;
    }

    @Override
    public void setAmount(float fAmount) {
        this.amount = fAmount;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void setCustomerId(String id) {
        this.customer_id = id;
    }

    @Override
    public String getCustomerId() {
        return customer_id;
    }

    @Override
    public void setOrderIncrementId(String id) {
        this.order_increment_id = id;
    }

    @Override
    public String getOrderIncrementId() {
        return order_increment_id;
    }
}
