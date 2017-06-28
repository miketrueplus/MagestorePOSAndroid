package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderItemUpdateQtyParam extends PosAbstractModel implements OrderItemUpdateQtyParam {
    String entity_id;
    String qty;
    @Gson2PosExclude
    float total_invoice;

    @Override
    public void setEntityId(String strEntityId) {
        entity_id = strEntityId;
    }

    @Override
    public void setQty(String intQty) {
        qty = intQty;
    }

    @Override
    public void setTotalInvoice(float fTotalInvoice) {
        total_invoice = fTotalInvoice;
    }

    @Override
    public float getTotalInvoice() {
        return total_invoice;
    }

    @Override
    public void setQty(float intQty) {
        qty = Float.toString(intQty);
    }
}
