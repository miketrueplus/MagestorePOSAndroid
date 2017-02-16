package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.checkout.CheckoutPayment;

/**
 * Created by Johan on 2/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddPaymentListController extends AbstractListController<CheckoutPayment> {
    CheckoutListController mCheckoutListController;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    @Override
    public void bindItem(CheckoutPayment item) {
        super.bindItem(item);
        mCheckoutListController.addPaymentFromDialog(item);
    }
}
