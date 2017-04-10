package com.magestore.app.pos.model.plugins;

import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosGiftCard extends PosAbstractModel implements GiftCard {
    String quote_id;
    String coupon_code;
    float balance;

    @Override
    public String getQuoteId() {
        return quote_id;
    }

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public String getCouponCode() {
        return coupon_code;
    }

    @Override
    public void setCouponCode(String strCouponCode) {
        coupon_code = strCouponCode;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public void setBalance(float fBalance) {
        balance = fBalance;
    }
}
