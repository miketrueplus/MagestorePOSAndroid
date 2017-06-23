package com.magestore.app.lib.model.checkout.payment;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 6/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CreditCard extends Model {
    String getCode();
    void setCode(String strCode);
    String getRegular();
    void setRegular(String strRegular);
}
