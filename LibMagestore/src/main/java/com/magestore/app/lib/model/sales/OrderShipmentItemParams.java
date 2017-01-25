package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShipmentItemParams extends Model {
    void setOrderItemId(String strOrderItemId);

    String getOrderItemId();

    int getQty();

    void setQty(int strQty);
}
