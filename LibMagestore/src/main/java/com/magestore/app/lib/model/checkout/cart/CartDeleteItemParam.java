package com.magestore.app.lib.model.checkout.cart;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CartDeleteItemParam extends Model {
    void setQuoteId(String strOrderId);
    void setItemId(String strItemId);
}
