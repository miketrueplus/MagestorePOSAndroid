package com.magestore.app.pos.model.magento.registershift;

import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 5/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosSessionParam extends PosAbstractModel implements SessionParam {
    float balance;
    float base_balance;
    float base_cash_added;
    float base_cash_left;
    float base_cash_removed;
    float base_cash_sale;
    float base_closed_amount;
    String base_currency_code;
    float base_float_amount;
    float base_total_sales;
    float cash_added;
    float cash_left;
    float cash_removed;
    float cash_sale;
    float closed_amount;
    String closed_at;
    String closed_note;
    float float_amount;
    String location_id;
    String opened_at;
    String opened_note;
    String shift_currency_code;
    String shift_id;
    String staff_id;
    String status;
    float total_sales;
    String pos_id;

    @Override
    public void setBalance(float fBalance) {
        balance = fBalance;
    }

    @Override
    public void setBaseBalance(float fBaseBalance) {
        base_balance = fBaseBalance;
    }

    @Override
    public void setBaseCashAdded(float fBaseCashAdded) {
        base_cash_added = fBaseCashAdded;
    }

    @Override
    public void setBaseCashLeft(float fBaseCashLeft) {
        base_cash_left = fBaseCashLeft;
    }

    @Override
    public void setBaseCashRemoved(float fBaseCashRemoved) {
        base_cash_removed = fBaseCashRemoved;
    }

    @Override
    public void setBaseCashSale(float fBaseCashSale) {
        base_cash_sale = fBaseCashSale;
    }

    @Override
    public void setBaseClosedAmount(float fBaseClosedAmount) {
        base_closed_amount = fBaseClosedAmount;
    }

    @Override
    public void setBaseCurrencyCode(String strBaseCurrencyCode) {
        base_currency_code = strBaseCurrencyCode;
    }

    @Override
    public void setBaseFloatAmount(float fBaseFloatAmount) {
        base_float_amount = fBaseFloatAmount;
    }

    @Override
    public void setBaseTotalSales(float fBaseTotalSales) {
        base_total_sales = fBaseTotalSales;
    }

    @Override
    public void setCashAdded(float fCashAdded) {
        cash_added = fCashAdded;
    }

    @Override
    public void setCashLeft(float fCashLeft) {
        cash_left = fCashLeft;
    }

    @Override
    public void setCashRemoved(float fCashRemoved) {
        cash_removed = fCashRemoved;
    }

    @Override
    public void setCashSale(float fCashSale) {
        cash_sale = fCashSale;
    }

    @Override
    public void setCloseAmount(float fCloseAmount) {
        closed_amount = fCloseAmount;
    }

    @Override
    public void setCloseAt(String strCloseAt) {
        closed_at = strCloseAt;
    }

    @Override
    public void setClosedNote(String strClosedNote) {
        closed_note = strClosedNote;
    }

    @Override
    public void setFloatAmount(float fFloatAmount) {
        float_amount = fFloatAmount;
    }

    @Override
    public void setLocationId(String strLocationId) {
        location_id = strLocationId;
    }

    @Override
    public void setOpenedAt(String strOpenedAt) {
        opened_at = strOpenedAt;
    }

    @Override
    public void setOpenedNote(String strOpenedNote) {
        opened_note = strOpenedNote;
    }

    @Override
    public void setShiftCurrencyCode(String strShiftCurrencyCode) {
        shift_currency_code = strShiftCurrencyCode;
    }

    @Override
    public void setShiftId(String strShiftId) {
        shift_id = strShiftId;
    }

    @Override
    public void setStaffId(String strStaffId) {
        staff_id = strStaffId;
    }

    @Override
    public void setStatus(String strStatus) {
        status = strStatus;
    }

    @Override
    public void setTotalSales(float fTotalSales) {
        total_sales = fTotalSales;
    }

    @Override
    public void setPosId(String strPosId) {
        pos_id = strPosId;
    }
}
