package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CheckoutTotals extends Model {
    String getCode();
    String getTitle();
    float getValue();
}
