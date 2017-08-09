package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 3/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteAddCouponParam extends Model {
    String getQuoteId();
    void setQuoteId(String strQuoteId);
    String getCouponCode();
    void setCouponCode(String strCouponCode);
    void setCurrencyId(String strCurrencyId);
    void setCustomerId(String strCustomerId);
    void setStoreId(String strStoreId);
    void setTillId(String strTillId);
}
