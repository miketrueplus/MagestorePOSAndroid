package com.magestore.app.lib.model.directory;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Currency extends Model {
    String getCode();
    void setCode(String strCode);
    String getCurrencyName();
    void setCurrenyName(String strCurrencyName);
    String getCurrencySymbol();
    void setCurrencySymbol(String strCurrencySymbol);
    String getIsDefault();
    void setIsDefault(String strIsDefault);
    float getCurrencyRate();
    void setCurrencyRate(float fCurrencyRate);
}
