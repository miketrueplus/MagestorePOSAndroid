package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCheckoutPayment extends PosAbstractModel implements CheckoutPayment {
    String code;
    String title;
    float base_amount;
    float amount;
    float base_real_amount;
    float real_amount;
    String is_pay_later;
    String shift_id;
    String information;
    String is_default;
    String is_reference_number;
    String type;

    class AdditionalData {
        String cc_owner;
        String cc_type;
        String cc_number;
        String cc_exp_month;
        String cc_exp_year;
        String cc_cid;
    }

    @Gson2PosExclude
    boolean check_reference;

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
    public void setBaseAmount(float amount) {
        this.base_amount = amount;
    }

    @Override
    public float getBaseAmount() {
        return this.base_amount;
    }

    @Override
    public float getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public float getBaseRealAmount() {
        return this.base_real_amount;
    }

    @Override
    public void setBaseRealAmount(float amount) {
        this.base_real_amount = amount;
    }

    @Override
    public float getRealAmount() {
        return this.real_amount;
    }

    @Override
    public void setRealAmount(float amount) {
        this.real_amount = amount;
    }

    @Override
    public void setPaylater(String paylater) {
        this.is_pay_later = paylater;
    }

    @Override
    public String isPaylater() {
        return is_pay_later;
    }

    @Override
    public String getIsDefault() {
        return is_default;
    }

    @Override
    public void setIsDefault(String strIsDefault) {
        is_default = strIsDefault;
    }

    @Override
    public String getIsReferenceNumber() {
        return is_reference_number;
    }

    @Override
    public void setIsReferenceNumber(String strIsReferenceNumber) {
        is_reference_number = strIsReferenceNumber;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String strType) {
        type = strType;
    }

    @Override
    public String getInformation() {
        return information;
    }

    @Override
    public void setInformation(String strInformation) {
        information = strInformation;
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

    @Override
    public boolean checkReference() {
        if (getIsReferenceNumber().equals("1")) {
            return true;
        }
        return false;
    }
}
