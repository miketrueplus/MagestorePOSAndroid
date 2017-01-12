package com.magestore.app.pos.model.order;

import com.magestore.app.lib.model.order.OrderShippingAssignments;
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
