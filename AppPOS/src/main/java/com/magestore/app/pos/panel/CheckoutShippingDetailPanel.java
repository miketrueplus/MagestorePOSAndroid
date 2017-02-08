package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.panel.AbstractDetailPanel;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutShippingDetailPanel extends AbstractDetailPanel<CheckoutShipping> {
    public CheckoutShippingDetailPanel(Context context) {
        super(context);
    }

    public CheckoutShippingDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutShippingDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
