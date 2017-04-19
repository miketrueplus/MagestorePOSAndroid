package com.magestore.app.pos.model.checkout.payment;

import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.model.checkout.payment.AuthorizenetParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosAuthorizenet extends PosAbstractModel implements Authorizenet {
    String payment_model;
    String order_id;
    PosAuthorizenetParams payment_infomation;

    @Override
    public AuthorizenetParams getPaymentInformation() {
        return (AuthorizenetParams) payment_infomation;
    }
}
