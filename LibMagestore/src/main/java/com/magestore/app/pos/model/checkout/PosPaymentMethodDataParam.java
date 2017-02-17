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

    public class PaymentMethodAdditionalParam {

    }
}
