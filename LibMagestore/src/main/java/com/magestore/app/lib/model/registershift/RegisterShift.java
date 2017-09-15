package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface RegisterShift extends Model {
    float getTotalSales();
    void setTotalSales(float fTotalSales);
    float getBaseTotalSales();
    void setBaseTotalSales(float fBaseTotalSales);
    String getOpenedAt();
    void setOpenedAt(String strOpenedAt);
    String getClosedAt();
    void setClosedAt(String strCloseAt);
    String getUpdateAt();
    String getOpenAndClose();
    List<SaleSummary> getSalesSummary();
    void setSalesSummary(List<SaleSummary> listSaleSummary);
    List<CashTransaction> getCashTransaction();
    void setCashTransaction(List<CashTransaction> listCashTransaction);
    ZreportSalesSummary getZrepostSalesSummary();
    String getOpenedNote();
    String getClosedNote();
    float getBalance();
    void setBalance(float fBalance);
    float getBaseBalance();
    void setBaseBalance(float fBaseBalance);
    String getStaffName();
    float getFloatAmount();
    void setFloatAmount(float fFloatAmount);
    float getBaseFloatAmount();
    void setBaseFloatAmount(float fBaseFloatAmount);
    String getBaseCurrencyCode();
    boolean checkSaleSummary();
    boolean checkOpenNote();
    boolean checkCloseNote();
    boolean checkStatus();
    String getLocationId();
    String getShiftId();
    float getCashAdded();
    float getBaseCashAdded();
    float getCashLeft();
    float getBaseCashLeft();
    float getCashRemoved();
    float getBaseCashRemoved();
    float getClosedAmount();
    void setClosedAmount(float fClosedAmount);
    float getBaseClosedAmount();
    void setBaseClosedAmount(float fBaseClosedAmount);
    float getCashSales();
    float getBaseCashSales();
    void setBaseCashSales(float fBaseCashSales);
    String getShiftCurrencyCode();
    String getStaffId();
    String getStatus();
    void setStatus(String strStatus);
    String getPosId();
    void setPosId(String strPosId);
    String getPosName();
    void setPosName(String strPosName);

    boolean getLastSevenDay();
    void setLastSevenDay(boolean bLessSevenDay);

    boolean getLessSevenDay();
    void setLessSevenDay(boolean bLessSevenDay);

    // param add cash transaction
    void setParamCash(CashTransaction cashTransaction);
    CashTransaction getParamCash();

    String getStoreId();
    void setStoreId(String strStoreId);
}
