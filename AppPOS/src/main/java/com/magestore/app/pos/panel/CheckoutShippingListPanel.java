package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.Shipping;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.panel.AbstractListPanel;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutShippingListPanel extends AbstractListPanel<Shipping> {
    public CheckoutShippingListPanel(Context context) {
        super(context);
    }

    public CheckoutShippingListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutShippingListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, Shipping item, int position) {

    }
}
