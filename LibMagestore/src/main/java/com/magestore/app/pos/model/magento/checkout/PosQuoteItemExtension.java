package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteItemExtension extends PosAbstractModel implements QuoteItemExtension {
    String key;
    String value;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String strKey) {
        key = strKey;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String strValue) {
        value = strValue;
    }
}
