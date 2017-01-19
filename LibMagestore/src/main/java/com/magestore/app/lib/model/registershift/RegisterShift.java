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
    String getStaffName();
    float getFloatAmount();
    boolean checkSaleSummary();
    boolean checkOpenNote();
    boolean checkCloseNote();
}
