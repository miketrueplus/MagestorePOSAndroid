package com.magestore.app.lib.model;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceOrderIntegrationExtension extends Model {
    String getKey();
    void setKey(String strKey);

    float getValue();
    void setValue(float fValue);
}
