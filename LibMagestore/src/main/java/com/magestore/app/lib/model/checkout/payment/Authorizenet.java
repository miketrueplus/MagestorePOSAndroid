package com.magestore.app.lib.model.checkout.payment;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.sales.Order;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Authorizenet extends Model {
    Order getOrder();
    AuthorizenetParams getPaymentInformation();
    String getPaymentModel();
}
