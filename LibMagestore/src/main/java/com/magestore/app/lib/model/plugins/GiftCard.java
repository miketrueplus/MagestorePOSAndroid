package com.magestore.app.lib.model.plugins;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface GiftCard extends Model {
    String getQuoteId();
    void setQuoteId(String strQuoteId);
    String getCouponCode();
    void setCouponCode(String strCouponCode);
    float getAmount();
    void setAmount(float fAmount);
    float getBalance();
    void setBalance(float fBalance);
}
