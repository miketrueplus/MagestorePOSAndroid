package com.magestore.app.pos.model.checkout;

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
}
