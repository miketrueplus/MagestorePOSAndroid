package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.panel.AbstractDetailPanel;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutPaymentDetailPanel extends AbstractDetailPanel<PaymentMethod> {
    public CheckoutPaymentDetailPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
