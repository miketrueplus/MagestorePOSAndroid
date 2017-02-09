package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;


import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.pos.databinding.CardCheckoutShippingContentBinding;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutShippingListPanel extends AbstractSimpleListView<ShippingMethod> {

    public CheckoutShippingListPanel(Context context) {
        super(context);
    }

    public CheckoutShippingListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutShippingListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, ShippingMethod item, int position) {
        CardCheckoutShippingContentBinding mBinding = DataBindingUtil.getBinding(view);
        mBinding.setShippingMethod(item);
    }
}
