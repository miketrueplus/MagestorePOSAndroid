package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderShipmentItemParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShipmentItemParams extends PosAbstractModel implements OrderShipmentItemParams {
    String orderItemId;
    int qty;


    @Override
    public void setOrderItemId(String strOrderItemId) {
        orderItemId = strOrderItemId;
    }

    @Override
    public String getOrderItemId() {
        return orderItemId;
    }

    @Override
    public int getQty() {
        return qty;
    }

    @Override
    public void setQty(int strQty) {
        qty = strQty;
    }
}
