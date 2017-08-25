package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 3/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosSaveQuoteParam extends PosAbstractModel implements SaveQuoteParam {
    String quote_id;
    String store_id;
    String customer_id;
    String currency_id;
    String till_id;
    QuoteData quote_data;

    @Override
    public String getQuoteId() {
        return quote_id;
    }

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getCustomerId() {
        return customer_id;
    }

    @Override
    public void setCustomerId(String strCustomerId) {
        customer_id = strCustomerId;
    }

    @Override
    public String getCurrencyId() {
        return currency_id;
    }

    @Override
    public void setCurrencyId(String strCurrencyId) {
        currency_id = strCurrencyId;
    }

    @Override
    public String getTillId() {
        return till_id;
    }

    @Override
    public void setTillId(String strTillId) {
        till_id = strTillId;
    }

    @Override
    public QuoteData createQuoteData() {
        quote_data = new QuoteData();
        return quote_data;
    }

    @Override
    public QuoteData getQuoteData() {
        return quote_data;
    }

    @Override
    public void setQuoteData(QuoteData quoteData) {
        quote_data = quoteData;
    }

    @Override
    public String getDiscountName() {
        return quote_data.webpos_cart_discount_name;
    }

    @Override
    public void setDiscountName(String strDiscountName) {
        quote_data.webpos_cart_discount_name = strDiscountName;
    }

    @Override
    public String getDiscountType() {
        return quote_data.webpos_cart_discount_type;
    }

    @Override
    public void setDiscountType(String strDiscountType) {
        quote_data.webpos_cart_discount_type = strDiscountType;
    }

    @Override
    public float getDiscountValue() {
        return quote_data.webpos_cart_discount_value;
    }

    @Override
    public void setDiscountValue(float strDiscountValue) {
        quote_data.webpos_cart_discount_value = strDiscountValue;
    }

    public class QuoteData {
        String webpos_cart_discount_name;
        String webpos_cart_discount_type;
        float webpos_cart_discount_value;
    }
}
