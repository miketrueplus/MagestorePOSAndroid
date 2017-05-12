package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceOrderIntegrationOrderData extends Model {
    String getKey();
    void setKey(String strKey);

    float getValue();
    void setValue(float fValue);
}
