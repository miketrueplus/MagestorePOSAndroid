package com.magestore.app.lib.model.order;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.order.PosOrderShippingAddress;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShipping extends Model {
    PosOrderShippingAddress getAddress();
    String getMethod();
}
