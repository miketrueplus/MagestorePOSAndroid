package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderItemUpdateQtyParam extends Model {
    void setEntityId(String strEntityId);
    void setQty(float intQty);
    void setQty(String intQty);
}
