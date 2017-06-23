package com.magestore.app.pos.model.checkout.payment;

import com.magestore.app.lib.model.checkout.payment.CreditCard;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 6/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosCreditCard extends PosAbstractModel implements CreditCard {
    String code;
    String regular;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
    }

    @Override
    public String getRegular() {
        return regular;
    }

    @Override
    public void setRegular(String strRegular) {
        regular = strRegular;
    }
}
