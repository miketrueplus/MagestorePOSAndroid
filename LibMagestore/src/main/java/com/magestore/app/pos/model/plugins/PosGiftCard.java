package com.magestore.app.pos.model.plugins;

import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Johan on 4/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosGiftCard extends PosAbstractModel implements GiftCard {
    String quote_id;
    String code;
    float amount;
    @Gson2PosExclude
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
        return code;
    }

    @Override
    public void setCouponCode(String strCouponCode) {
        code = strCouponCode;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void setAmount(float fAmount) {
        amount = fAmount;
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
