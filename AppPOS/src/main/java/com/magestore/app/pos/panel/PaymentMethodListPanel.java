package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.sales.Payment;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardPaymentMethodContentBinding;

/**
 * Created by Mike on 2/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PaymentMethodListPanel extends AbstractSimpleRecycleView<CheckoutPayment> {
    CheckoutListController mCheckoutListController;
    public void setCheckoutListController(CheckoutListController controller) {
        mCheckoutListController = controller;
    }
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
    protected void bindItem(View view, CheckoutPayment item, final int position) {
        CardPaymentMethodContentBinding binding = DataBindingUtil.bind(view);
        binding.setPaymentMethod(item);
    }

    @Override
    protected void onClickItem(View view, CheckoutPayment item, int position) {
//        mList.remove(position);
//        getAdapter().notifyDataSetChanged();
//        if (mCheckoutListController != null) mCheckoutListController.onAddPaymentMethod(item);
//        getAdapter().notifyItemRemoved(position);
//        getAdapter().notifyItemRangeChanged(position, getAdapter().getItemCount());

        // Select payment method
        mCheckoutListController.onAddPaymentMethod(item);
    }
}
