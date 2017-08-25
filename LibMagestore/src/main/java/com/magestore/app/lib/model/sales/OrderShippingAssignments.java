package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.magento.sales.PosOrderShipping;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShippingAssignments extends Model {
    PosOrderShipping getShipping();
}
