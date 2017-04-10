package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderItemUpdateQtyParam extends PosAbstractModel implements OrderItemUpdateQtyParam {
    String entity_id;
    int qty;

    @Override
    public void setEntityId(String strEntityId) {
        entity_id = strEntityId;
    }

    @Override
    public void setQty(int intQty) {
        qty = intQty;
    }
}
