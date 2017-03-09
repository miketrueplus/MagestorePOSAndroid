package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.controller.CheckoutListController;

/**
 * Created by Johan on 3/9/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutPaymentCreditCardPanel extends AbstractDetailPanel<CheckoutPayment> {
    CheckoutListController mCheckoutListController;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutPaymentCreditCardPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentCreditCardPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentCreditCardPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
