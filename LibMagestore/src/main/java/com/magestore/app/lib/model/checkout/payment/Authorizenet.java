package com.magestore.app.lib.model.checkout.payment;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Authorizenet extends Model {
    AuthorizenetParams getPaymentInformation();
}
