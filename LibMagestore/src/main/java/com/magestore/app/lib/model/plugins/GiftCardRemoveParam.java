package com.magestore.app.lib.model.plugins;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface GiftCardRemoveParam extends Model {
    String getQuoteId();
    void setQuoteId(String strQuoteId);
    String getCode();
    void setCode(String strCode);
    String getCurrencyId();
    void setCurrencyId(String strCurrencyId);
    String getCustomerId();
    void setCustomerId(String strCustomerId);
    String getStoreId();
    void setStoreId(String strStoreId);
    String getTillId();
    void setTillId(String strTillId);
}
