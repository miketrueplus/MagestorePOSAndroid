package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.checkout.PosPaymentMethodDataParam;

/**
 * Created by Johan on 2/17/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PaymentMethodDataParam extends Model {
    void setReferenceNumber(String strReferenceNumber);

    void setAmount(float fAmount);

    void setBaseAmount(float fBaseAmount);

    void setBaseRealAmount(float fBaseRealAmount);

    void setCode(String strCode);

    void setIsPayLater(String strIsPayLater);

    void setRealAmount(float fRealAmount);

    void setTitle(String strTitle);

    PosPaymentMethodDataParam.PaymentMethodAdditionalParam createAddition();

    void setPaymentMethodAdditionalParam(PosPaymentMethodDataParam.PaymentMethodAdditionalParam paymentMethodAdditionalParam);

    void setCCOwner(String owner);

    String getCCOwner();

    void setCCType(String type);

    String getCCType();

    void setCCNumber(String number);

    String getCCNumber();

    void setCCExpMonth(String month);

    String getCCExpMonth();

    void setCCExpYear(String year);

    String getCCExpYear();

    void setCID(String cid);

    String getCID();

    String getShiftId();

    void setShiftId(String strShiftId);
}
