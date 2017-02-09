package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.pos.databinding.CardCheckoutPaymentContentBinding;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutPaymentListPanel extends AbstractSimpleListView<CheckoutPayment> {
    public CheckoutPaymentListPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, CheckoutPayment item, int position) {
        CardCheckoutPaymentContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setCheckoutPayment(item);
    }
}
