package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 3/29/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceOrderExtensionParam extends Model {
    String getKey();
    void setKey(String strKey);
    String getValue();
    void setValue(String strValue);
}
