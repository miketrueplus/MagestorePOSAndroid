package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 3/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteAddCouponParam extends PosAbstractModel implements QuoteAddCouponParam {
    String quote_id;
    String coupon_code;
    String currency_id;
    String customer_id;
    String store_id;
    String till_id;

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
    public void setCurrencyId(String strCurrencyId) {
        currency_id = strCurrencyId;
    }

    @Override
    public void setCustomerId(String strCustomerId) {
        customer_id = strCustomerId;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public void setTillId(String strTillId) {
        till_id = strTillId;
    }
}
