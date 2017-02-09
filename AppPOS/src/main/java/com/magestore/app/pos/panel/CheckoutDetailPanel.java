package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractDetailPanel;

/**
 * Created by Mike on 2/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutDetailPanel extends AbstractDetailPanel<Checkout> {
    public CheckoutDetailPanel(Context context) {
        super(context);
    }

    public CheckoutDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
