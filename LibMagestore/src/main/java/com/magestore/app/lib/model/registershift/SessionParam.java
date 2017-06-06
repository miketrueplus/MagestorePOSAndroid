package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 5/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface SessionParam extends Model {
    void setBalance(float fBalance);
    void setBaseBalance(float fBaseBalance);
    void setBaseCashAdded(float fBaseCashAdded);
    void setBaseCashLeft(float fBaseCashLeft);
    void setBaseCashRemoved(float fBaseCashRemoved);
    void setBaseCashSale(float fBaseCashSale);
    void setBaseClosedAmount(float fBaseClosedAmount);
    void setBaseCurrencyCode(String strBaseCurrencyCode);
    void setBaseFloatAmount(float fBaseFloatAmount);
    void setBaseTotalSales(float fBaseTotalSales);
    void setCashAdded(float fCashAdded);
    void setCashLeft(float fCashLeft);
    void setCashRemoved(float fCashRemoved);
    void setCashSale(float fCashSale);
    void setCloseAmount(float fCloseAmount);
    void setCloseAt(String strCloseAt);
    void setClosedNote(String strClosedNote);
    void setFloatAmount(float fFloatAmount);
    void setLocationId(String strLocationId);
    void setOpenedAt(String strOpenedAt);
    void setOpenedNote(String strOpenedNote);
    void setShiftCurrencyCode(String strShiftCurrencyCode);
    void setShiftId(String strShiftId);
    void setStaffId(String strStaffId);
    void setStatus(String strStatus);
    void setTotalSales(float fTotalSales);
    void setPosId(String strPosId);
}
