package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCredit;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by baonguyen on 28/04/2017.
 */

public class PosOrderCredit extends PosAbstractModel implements OrderCredit {
    float amount;
    String customer_id;
    String order_increment_id;

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
