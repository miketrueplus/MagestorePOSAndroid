package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.sales.PosOrderShippingAddress;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShipping extends Model {
    PosOrderShippingAddress getAddress();
    String getMethod();
}
