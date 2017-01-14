package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShippingAddress extends Model {
    String getAddressType();

    String getEmail();
}
