package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.databinding.CardOrderPaymentLaterContentBinding;

/**
 * Created by Johan on 5/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderPaymentLaterListPanel extends AbstractSimpleRecycleView<OrderWebposPayment> {
    public OrderPaymentLaterListPanel(Context context) {
        super(context);
    }

    public OrderPaymentLaterListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderPaymentLaterListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, OrderWebposPayment item, int position) {
        CardOrderPaymentLaterContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderWebposPayment(item);
    }

    @Override
    protected void onClickItem(View view, OrderWebposPayment item, int position) {

    }
}
