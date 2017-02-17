package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

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
}
