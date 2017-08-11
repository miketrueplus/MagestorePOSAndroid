package com.magestore.app.lib.model.plugins;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface RewardPoint extends Model {
    int getBalance();
    String getQuoteId();
    void setQuoteId(String strQuoteId);
    void setAmount(int intAmount);
    int getMaxPoints();
    void setMaxPoins(int iMaxpoints);
    int getAmount();
    String getCurrencyId();
    void setCurrencyId(String strCurrencyId);
    String getCustomerId();
    void setCustomerId(String strCustomerId);
    String getStoreId();
    void setStoreId(String strStoreId);
    String getTillId();
    void setTillId(String strTillId);
}
