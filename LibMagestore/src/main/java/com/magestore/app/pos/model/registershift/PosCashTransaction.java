package com.magestore.app.pos.model.registershift;

import com.google.gson.annotations.Expose;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.util.ConfigUtil;

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
    String balance_title;
    String check_type_value;

    @Expose(serialize = true, deserialize = true)
    boolean check_open_shift;

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
    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public void setBaseBalance(float baseBalance) {
        base_balance = baseBalance;
    }

    @Override
    public void setBaseValue(float baseValue) {
        base_value = baseValue;
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
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void setLocationId(String locationId) {
        location_id = locationId;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public void setShiftId(String shiftId) {
        shift_id = shiftId;
    }

    @Override
    public void setTransactionCurrencyCode(String transactionCurrencyCode) {
        transaction_currency_code = transactionCurrencyCode;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setBaseCurrencyCode(String baseCurrencyCode) {
        base_currency_code = baseCurrencyCode;
    }

    @Override
    public boolean getCheckNote() {
        if (note != null && !note.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public String getCheckTypeValue() {
        if (type.toLowerCase().equals("remove")) {
            return "- " + ConfigUtil.formatPrice(value);
        }
        return "+ " + ConfigUtil.formatPrice(value);
    }
}
