package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderItemParams extends PosAbstractModel implements OrderItemParams {
    String orderItemId;
    String qty;
    String additionalData;

    @Override
    public void setOrderItemId(String strOrderItemId) {
        orderItemId = strOrderItemId;
    }

    @Override
    public String getOrderItemId() {
        return orderItemId;
    }

    @Override
    public float getQty() {
        return Float.parseFloat(qty);
    }

    @Override
    public void setQty(String strQty) {
        qty = strQty;
    }

    @Override
    public void setQty(float strQty) {
        qty = Float.toString(strQty);
    }

    @Override
    public String getAdditionalData() {
        return additionalData;
    }

    @Override
    public void setAdditionalData(String strAdditionalData) {
        additionalData = strAdditionalData;
    }
}
