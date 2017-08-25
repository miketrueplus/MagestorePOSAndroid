package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationOrderData;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceOrderIntegrationOrderData extends PosAbstractModel implements PlaceOrderIntegrationOrderData {
    String key;
    float value;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String strKey) {
        key = strKey;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float fValue) {
        value = fValue;
    }
}
