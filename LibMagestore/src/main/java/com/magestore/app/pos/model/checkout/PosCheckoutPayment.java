package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCheckoutPayment extends PosAbstractModel implements CheckoutPayment {
    String code;
    String title;
    int base_amount;
    int amount;
    int base_real_amount;
    int real_amount;
    int is_pay_later;
    String shift_id;

    class AdditionalData {
        String cc_owner;
        String cc_type;
        String cc_number;
        String cc_exp_month;
        String cc_exp_year;
        String cc_cid;
    }
    AdditionalData additional_data = new AdditionalData();

    @Override
    public void setCode(String strCode) {
        this.code = strCode;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public void setTitle(String strTitle) {
        this.title = strTitle;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setBaseAmount(int amount) {
        this.base_amount = amount;
    }

    @Override
    public int getBaseAmount() {
        return this.base_amount;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int getBaseRealAmount() {
        return this.base_real_amount;
    }

    @Override
    public void setBaseRealAmount(int amount) {
        this.base_real_amount = amount;
    }

    @Override
    public int getRealAmount() {
        return this.real_amount;
    }

    @Override
    public void setRealAmount(int amount) {
        this.real_amount = amount;
    }

    @Override
    public void setPaylater(int paylater) {
        this.is_pay_later = paylater;
    }

    @Override
    public int isPaylater() {
        return is_pay_later;
    }

    @Override
    public void setShiftID(String shiftID) {
        this.shift_id = shiftID;
    }

    @Override
    public String getShiftID() {
        return this.shift_id;
    }

    @Override
    public void setCCOwner(String owner) {
        this.additional_data.cc_owner = owner;
    }

    @Override
    public String getCCOwner() {
        return this.additional_data.cc_owner;
    }

    @Override
    public void setCCType(String type) {
        this.additional_data.cc_type = type;
    }

    @Override
    public String getCCType() {
        return this.additional_data.cc_type;
    }

    @Override
    public void setCCNumber(String number) {
        this.additional_data.cc_number = number;
    }

    @Override
    public String getCCNumber() {
        return this.additional_data.cc_number;
    }

    @Override
    public void setCCExpMonth(String month) {
        this.additional_data.cc_exp_month = month;
    }

    @Override
    public String getCCExpMonth() {
        return this.additional_data.cc_exp_month;
    }

    @Override
    public void setCCExpYear(String year) {
        this.additional_data.cc_exp_year = year;
    }

    @Override
    public String getCCExpYear() {
        return this.additional_data.cc_exp_year;
    }

    @Override
    public void setCID(String cid) {
        this.additional_data.cc_cid = cid;
    }

    @Override
    public String getCID() {
        return this.additional_data.cc_cid;
    }
}
