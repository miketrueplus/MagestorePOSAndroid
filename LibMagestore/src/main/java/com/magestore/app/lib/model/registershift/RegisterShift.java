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
    float getBaseTotalSales();
    String getOpenedAt();
    String getClosedAt();
    String getUpdateAt();
    String getOpenAndClose();
    List<SaleSummary> getSalesSummary();
    List<CashTransaction> getCashTransaction();
    ZreportSalesSummary getZrepostSalesSummary();
    String getOpenedNote();
    String getClosedNote();
    float getBalance();
    float getBaseBalance();
    String getStaffName();
    float getFloatAmount();
    float getBaseFloatAmount();
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
    float getBaseClosedAmount();
    float getCashSales();
    float getBaseCashSales();
    String getShiftCurrencyCode();
    String getStaffId();
    String getStatus();
    String getPosId();
    String getPosName();

    // param add cash transaction
    void setParamCash(CashTransaction cashTransaction);
    CashTransaction getParamCash();
}
