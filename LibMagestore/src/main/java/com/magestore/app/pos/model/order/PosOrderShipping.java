package com.magestore.app.pos.model.order;

import com.magestore.app.lib.model.order.OrderShipping;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShipping extends PosAbstractModel implements OrderShipping {
    PosOrderShippingAddress address;
    String method;

    @Override
    public PosOrderShippingAddress getAddress() {
        return address;
    }

    @Override
    public String getMethod() {
        return method;
    }
}
