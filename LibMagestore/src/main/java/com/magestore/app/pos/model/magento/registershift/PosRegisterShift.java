package com.magestore.app.pos.model.magento.registershift;

import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.model.registershift.ZreportSalesSummary;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.util.ConfigUtil;

import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosRegisterShift extends PosAbstractModel implements RegisterShift {
    String entity_id;
    String shift_id;
    String staff_id;
    String location_id;
    float float_amount;
    float base_float_amount;
    float closed_amount;
    float base_closed_amount;
    float cash_left;
    float base_cash_left;
    float total_sales;
    float base_total_sales;
    float base_balance;
    float balance;
    float cash_sale;
    float base_cash_sale;
    float cash_added;
    float base_cash_added;
    float cash_removed;
    float base_cash_removed;
    String opened_at;
    String closed_at;
    String opened_note;
    String closed_note;
    String status;
    String base_currency_code;
    String shift_currency_code;
    String indexeddb_id;
    String updated_at;
    String staff_name;
    List<PosSaleSummary> sale_summary;
    List<PosCashTransaction> cash_transaction;
    PosZreportSalesSummary zreport_sales_summary;
    String pos_id;
    String pos_name;
    String store_id;

    @Gson2PosExclude
    boolean last_seven_day;
    @Gson2PosExclude
    boolean less_seven_day;

    // param request add cash transaction
    PosCashTransaction param_cash;

    public PosRegisterShift(){

    }

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public float getTotalSales() {
        return total_sales;
    }

    @Override
    public float getBaseTotalSales() {
        return base_total_sales;
    }

    @Override
    public String getOpenedAt() {
        return opened_at;
    }

    @Override
    public String getClosedAt() {
        return closed_at;
    }

    @Override
    public String getUpdateAt() {
        return updated_at;
    }

    @Override
    public String getOpenAndClose() {
        return ConfigUtil.formatTime(opened_at) + " - " + ConfigUtil.formatTime(closed_at);
    }

    @Override
    public List<SaleSummary> getSalesSummary() {
        return (List<SaleSummary>) (List<?>) sale_summary;
    }

    @Override
    public List<CashTransaction> getCashTransaction() {
        return (List<CashTransaction>) (List<?>) cash_transaction;
    }

    @Override
    public ZreportSalesSummary getZrepostSalesSummary() {
        return zreport_sales_summary;
    }

    @Override
    public String getOpenedNote() {
        return opened_note;
    }

    @Override
    public String getClosedNote() {
        return closed_note;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public float getBaseBalance() {
        return base_balance;
    }

    @Override
    public String getStaffName() {
        return staff_name;
    }

    @Override
    public float getFloatAmount() {
        return float_amount;
    }

    @Override
    public float getBaseFloatAmount() {
        return base_float_amount;
    }

    @Override
    public String getBaseCurrencyCode() {
        return base_currency_code;
    }

    @Override
    public boolean checkSaleSummary() {
        if (sale_summary != null && sale_summary.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkOpenNote() {
        if (opened_note != null && !opened_note.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCloseNote() {
        if (closed_note != null && !closed_note.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkStatus() {
        if (status.equals("0")) {
            return true;
        }
        return false;
    }

    @Override
    public void setParamCash(CashTransaction cashTransaction) {
        param_cash = (PosCashTransaction) cashTransaction;
    }

    @Override
    public CashTransaction getParamCash() {
        return param_cash;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getLocationId() {
        return location_id;
    }

    @Override
    public String getShiftId() {
        return shift_id;
    }

    @Override
    public float getCashAdded() {
        return cash_added;
    }

    @Override
    public float getBaseCashAdded() {
        return base_cash_added;
    }

    @Override
    public float getCashLeft() {
        return cash_left;
    }

    @Override
    public float getBaseCashLeft() {
        return base_cash_left;
    }

    @Override
    public float getCashRemoved() {
        return cash_removed;
    }

    @Override
    public float getBaseCashRemoved() {
        return base_cash_removed;
    }

    @Override
    public float getClosedAmount() {
        return closed_amount;
    }

    @Override
    public float getBaseClosedAmount() {
        return base_closed_amount;
    }

    @Override
    public float getCashSales() {
        return cash_sale;
    }

    @Override
    public float getBaseCashSales() {
        return base_cash_sale;
    }

    @Override
    public String getShiftCurrencyCode() {
        return shift_currency_code;
    }

    @Override
    public String getStaffId() {
        return staff_id;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getPosId() {
        return pos_id;
    }

    @Override
    public String getPosName() {
        return pos_name;
    }

    @Override
    public boolean getLastSevenDay() {
        return last_seven_day;
    }

    @Override
    public void setLastSevenDay(boolean bLessSevenDay) {
        last_seven_day = bLessSevenDay;
    }

    @Override
    public boolean getLessSevenDay() {
        return less_seven_day;
    }

    @Override
    public void setLessSevenDay(boolean bLessSevenDay) {
        less_seven_day = bLessSevenDay;
    }
}
