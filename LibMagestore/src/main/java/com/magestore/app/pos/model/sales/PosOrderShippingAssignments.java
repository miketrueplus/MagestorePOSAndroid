package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderShippingAssignments;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShippingAssignments extends PosAbstractModel implements OrderShippingAssignments {
    PosOrderShipping shipping;

    @Override
    public PosOrderShipping getShipping() {
        return shipping;
    }
}
