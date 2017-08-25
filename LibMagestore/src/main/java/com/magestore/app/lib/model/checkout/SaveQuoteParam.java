package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.magento.checkout.PosSaveQuoteParam;

/**
 * Created by Johan on 3/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface SaveQuoteParam extends Model {
    String getQuoteId();
    void setQuoteId(String strQuoteId);
    String getStoreId();
    void setStoreId(String strStoreId);
    String getCustomerId();
    void setCustomerId(String strCustomerId);
    String getCurrencyId();
    void setCurrencyId(String strCurrencyId);
    String getTillId();
    void setTillId(String strTillId);
    PosSaveQuoteParam.QuoteData createQuoteData();
    PosSaveQuoteParam.QuoteData getQuoteData();
    void setQuoteData(PosSaveQuoteParam.QuoteData quoteData);
    String getDiscountName();
    void setDiscountName(String strDiscountName);
    String getDiscountType();
    void setDiscountType(String strDiscountType);
    float getDiscountValue();
    void setDiscountValue(float strDiscountValue);
}
