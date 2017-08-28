package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 5/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface SessionParam extends Model {
    float getBalance();
    void setBalance(float fBalance);
    float getBaseBalance();
    void setBaseBalance(float fBaseBalance);
    float getBaseCashAdded();
    void setBaseCashAdded(float fBaseCashAdded);
    float getBaseCashLeft();
    void setBaseCashLeft(float fBaseCashLeft);
    float getBaseCashRemoved();
    void setBaseCashRemoved(float fBaseCashRemoved);
    float getBaseCashSale();
    void setBaseCashSale(float fBaseCashSale);
    float getBaseClosedAmount();
    void setBaseClosedAmount(float fBaseClosedAmount);
    String getBaseCurrencyCode();
    void setBaseCurrencyCode(String strBaseCurrencyCode);
    float getBaseFloatAmount();
    void setBaseFloatAmount(float fBaseFloatAmount);
    float getBaseTotalSales();
    void setBaseTotalSales(float fBaseTotalSales);
    float getCashAdded();
    void setCashAdded(float fCashAdded);
    float getCashLeft();
    void setCashLeft(float fCashLeft);
    float getCashRemoved();
    void setCashRemoved(float fCashRemoved);
    float getCashSale();
    void setCashSale(float fCashSale);
    float getCloseAmount();
    void setCloseAmount(float fCloseAmount);
    String getCloseAt();
    void setCloseAt(String strCloseAt);
    String getClosedNote();
    void setClosedNote(String strClosedNote);
    float getFloatAmount();
    void setFloatAmount(float fFloatAmount);
    String getLocationId();
    void setLocationId(String strLocationId);
    String getOpenedAt();
    void setOpenedAt(String strOpenedAt);
    String getOpenedNote();
    void setOpenedNote(String strOpenedNote);
    String getShiftCurrencyCode();
    void setShiftCurrencyCode(String strShiftCurrencyCode);
    String getShiftId();
    void setShiftId(String strShiftId);
    String getStaffId();
    void setStaffId(String strStaffId);
    String getStatus();
    void setStatus(String strStatus);
    float getTotalSales();
    void setTotalSales(float fTotalSales);
    String getPosId();
    void setPosId(String strPosId);
}
