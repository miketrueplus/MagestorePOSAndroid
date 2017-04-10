package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.pos.model.PosAbstractModel;
import java.util.List;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderUpdateQtyParam extends PosAbstractModel implements OrderUpdateQtyParam {
    String order_id;
    List<OrderItemUpdateQtyParam> items;

    @Override
    public void setOrderId(String strOrderId) {
        order_id = strOrderId;
    }

    @Override
    public void setItems(List<OrderItemUpdateQtyParam> listItems) {
        items = listItems;
    }
}
