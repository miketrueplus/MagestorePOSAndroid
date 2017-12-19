package com.magestore.app.pos.model.registershift;

import com.google.gson.annotations.Expose;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

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
    String balance_title;
    String check_type_value;

    @Gson2PosExclude
    boolean check_open_shift;
    @Gson2PosExclude
    float float_amount;
    @Gson2PosExclude
    float base_float_amount;
    @Gson2PosExclude
    String param_shift_id;
    @Gson2PosExclude
    float amount;
    @Gson2PosExclude
    float base_amount;

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
    public float getBaseFloatAmount() {
        return base_float_amount;
    }

    @Override
    public void setBaseFloatAmount(float fBaseFloatAmount) {
        base_float_amount = fBaseFloatAmount;
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
    public float getBaseBalance() {
        return base_balance;
    }

    @Override
    public void setBaseBalance(float baseBalance) {
        base_balance = baseBalance;
    }

    @Override
    public float getBaseValue() {
//        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2) || ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
            return base_value;
//        } else {
//            return base_amount;
//        }
    }

    @Override
    public void setBaseValue(float baseValue) {
//        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2) || ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
            base_value = baseValue;
//        } else {
//            base_amount = baseValue;
//        }
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
//        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2) || ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
            return value;
//        } else {
//            return amount;
//        }
    }

    @Override
    public void setValue(float value) {
//        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2) || ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
            this.value = value;
//        } else {
//            this.amount = value;
//        }
    }

    @Override
    public String getLocationId() {
        return location_id;
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
    public String getShiftId() {
        return shift_id;
    }

    @Override
    public void setShiftId(String shiftId) {
        shift_id = shiftId;
    }

    @Override
    public String getTransactionCurrencyCode() {
        return transaction_currency_code;
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
    public String getBaseCurrencyCode() {
        return base_currency_code;
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
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2) || ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_1)) {
            if (type.toLowerCase().equals("remove")) {
                return "- " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(base_value));
            }
            return "+ " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(base_value));
        } else if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_ODOO)) {
            if (type.toLowerCase().equals("remove")) {
                return "- " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(0 - base_value));
            } else {
                if (base_value < 0) {
                    return "- " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(0 - base_value));
                } else {
                    return "+ " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(base_value));
                }
            }
        }
        else {
            if (base_amount >= 0) {
                return "+ " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(base_amount));
            } else {
                return "- " + ConfigUtil.formatPrice(ConfigUtil.convertToPrice(0 - base_amount));
            }
        }
    }

    @Override
    public String getParamShiftId() {
        return param_shift_id;
    }

    @Override
    public void setParamShiftId(String strParamShiftId) {
        param_shift_id = strParamShiftId;
    }
}
