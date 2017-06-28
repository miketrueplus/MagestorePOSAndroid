package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.sales.PosOrderUpdateQtyParam;

import java.util.List;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderUpdateQtyParam extends Model {
    void setOrderId(String strOrderId);
    void setItems(List<OrderItemUpdateQtyParam> listItems);
    List<OrderItemUpdateQtyParam> getItems();
    void setTotalPaid(float fTotalPaid);
    float getTotalPaid();
}
