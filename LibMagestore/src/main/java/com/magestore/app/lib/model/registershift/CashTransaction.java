package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CashTransaction extends Model {
    String getCreatedAt();
    void setCreateAt(String strCreateAt);
    String getNote();
    String getOpenShiftTitle();
    void setOpenShiftTitle(String strOpenShiftTitle);
    float getFloatAmount();
    void setFloatAmount(float floatAmount);
    float getBaseFloatAmount();
    void setBaseFloatAmount(float fBaseFloatAmount);
    float getBalance();
    void setBalance(float balance);
    float getBaseBalance();
    void setBaseBalance(float baseBalance);
    float getBaseValue();
    void setBaseValue(float baseValue);
    String getBalanceTitle();
    void setBalanceTitle(String strBalance);
    boolean getCheckOpenShift();
    void setCheckOpenShift(boolean checkOpenShift);
    float getValue();
    void setValue(float value);
    String getLocationId();
    void setLocationId(String locationId);
    void setNote(String note);
    String getShiftId();
    void setShiftId(String shiftId);
    String getTransactionCurrencyCode();
    void setTransactionCurrencyCode(String transactionCurrencyCode);
    void setType(String type);
    String getType();
    String getBaseCurrencyCode();
    void setBaseCurrencyCode(String baseCurrencyCode);
    boolean getCheckNote();
    String getCheckTypeValue();
    String getParamShiftId();
    void setParamShiftId(String strParamShiftId);
}
