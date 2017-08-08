package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.sales.Order;

/**
 * Created by Johan on 8/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataPlaceOrder extends Model {
    Order getOrder();
}
