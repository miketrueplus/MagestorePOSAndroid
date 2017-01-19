package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosCashTransaction extends PosAbstractModel implements CashTransaction {
    String transaction_id;
    String shift_id;
    String location_id;
    String order_id;
    float value;
    float base_value;
    float balance;
    float base_balance;
    String created_at;
    String note;
    String type;
    String base_currency_code;
    String transaction_currency_code;
    String indexeddb_id;
    String updated_at;
    String open_shift_title;
    float float_amount;
    boolean check_open_shift;
    String balance_title;

    @Override
    public String getID() {
        return transaction_id;
    }

    @Override
    public String getCreatedAt() {
        return created_at;
    }

    @Override
    public void setCreateAt(String strCreateAt) {
        this.created_at = strCreateAt;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public String getOpenShiftTitle() {
        return open_shift_title;
    }

    @Override
    public void setOpenShiftTitle(String strOpenShiftTitle) {
        this.open_shift_title = strOpenShiftTitle;
    }

    @Override
    public float getFloatAmount() {
        return float_amount;
    }

    @Override
    public void setFloatAmount(float floatAmount) {
        this.float_amount = floatAmount;
    }

    @Override
    public float getBalance() {
        return balance;
    }

    @Override
    public String getBalanceTitle() {
        return balance_title;
    }

    @Override
    public void setBalanceTitle(String strBalance) {
        balance_title = strBalance;
    }

    @Override
    public boolean getCheckOpenShift() {
        return check_open_shift;
    }

    @Override
    public void setCheckOpenShift(boolean checkOpenShift) {
        this.check_open_shift = checkOpenShift;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public boolean getCheckNote() {
        if (note != null && !note.equals("")) {
            return true;
        }
        return false;
    }
}
