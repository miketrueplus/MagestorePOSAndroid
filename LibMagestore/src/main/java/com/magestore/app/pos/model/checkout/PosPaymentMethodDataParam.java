package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/17/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPaymentMethodDataParam extends PosAbstractModel implements PaymentMethodDataParam {
    PaymentMethodAdditionalParam additional_data;
    String reference_number;
    float amount;
    float base_amount;
    float base_real_amount;
    String code;
    String is_pay_later;
    float real_amount;
    String title;
    String shift_id;

    @Override
    public void setReferenceNumber(String strReferenceNumber) {
        reference_number = strReferenceNumber;
    }

    @Override
    public void setAmount(float fAmount) {
        amount = fAmount;
    }

    @Override
    public void setBaseAmount(float fBaseAmount) {
        base_amount = fBaseAmount;
    }

    @Override
    public void setBaseRealAmount(float fBaseRealAmount) {
        base_real_amount = fBaseRealAmount;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
    }

    @Override
    public void setIsPayLater(String strIsPayLater) {
        is_pay_later = strIsPayLater;
    }

    @Override
    public void setRealAmount(float fRealAmount) {
        real_amount = fRealAmount;
    }

    @Override
    public void setTitle(String strTitle) {
        title = strTitle;
    }

    @Override
    public PaymentMethodAdditionalParam createAddition() {
        additional_data = new PaymentMethodAdditionalParam();
        return additional_data;
    }

    @Override
    public void setPaymentMethodAdditionalParam(PaymentMethodAdditionalParam paymentMethodAdditionalParam) {
        additional_data = paymentMethodAdditionalParam;
    }

    @Override
    public void setCCOwner(String owner) {
        additional_data.cc_owner = owner;
    }

    @Override
    public String getCCOwner() {
        return additional_data.cc_owner;
    }

    @Override
    public void setCCType(String type) {
        additional_data.cc_type = type;
    }

    @Override
    public String getCCType() {
        return additional_data.cc_type;
    }

    @Override
    public void setCCNumber(String number) {
        additional_data.cc_number = number;
    }

    @Override
    public String getCCNumber() {
        return additional_data.cc_number;
    }

    @Override
    public void setCCExpMonth(String month) {
        additional_data.cc_exp_month = month;
    }

    @Override
    public String getCCExpMonth() {
        return additional_data.cc_exp_month;
    }

    @Override
    public void setCCExpYear(String year) {
        additional_data.cc_exp_year = year;
    }

    @Override
    public String getCCExpYear() {
        return additional_data.cc_exp_year;
    }

    @Override
    public void setCID(String cid) {
        additional_data.cc_cid = cid;
    }

    @Override
    public String getCID() {
        return additional_data.cc_cid;
    }

    @Override
    public String getShiftId() {
        return shift_id;
    }

    @Override
    public void setShiftId(String strShiftId) {
        shift_id = strShiftId;
    }

    public class PaymentMethodAdditionalParam {
        String cc_owner;
        String cc_type;
        String cc_number;
        String cc_exp_month;
        String cc_exp_year;
        String cc_cid;
    }
}
