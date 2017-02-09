package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.sales.Payment;
import com.magestore.app.lib.view.AbstractSimpleListView;

/**
 * Created by Mike on 2/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PaymentMethodListPanel extends AbstractSimpleListView<PaymentMethod> {
    public PaymentMethodListPanel(Context context) {
        super(context);
    }

    public PaymentMethodListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaymentMethodListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, PaymentMethod item, int position) {
        super.bindItem(view, item, position);
    }
}
