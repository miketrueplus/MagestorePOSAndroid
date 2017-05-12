package com.magestore.app.pos.model.checkout.payment;

import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.model.checkout.payment.AuthorizenetParams;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.sales.PosOrder;

/**
 * Created by Johan on 4/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosAuthorizenet extends PosAbstractModel implements Authorizenet {
    PosOrder order;
    String payment_model;
    String order_id;
    PosAuthorizenetParams payment_infomation;

    @Override
    public Order getOrder() {
        return (Order) order;
    }

    @Override
    public AuthorizenetParams getPaymentInformation() {
        return (AuthorizenetParams) payment_infomation;
    }

    @Override
    public String getPaymentModel() {
        return payment_model;
    }
}
