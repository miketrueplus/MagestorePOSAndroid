package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.databinding.CardPaymentMethodContentBinding;

/**
 * Created by Johan on 3/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderAddPaymentPanel extends AbstractSimpleRecycleView<CheckoutPayment> {
    OrderHistoryListController mOrderHistoryListController;

    public void setOrderHistoryListController(OrderHistoryListController mOrderHistoryListController) {
        this.mOrderHistoryListController = mOrderHistoryListController;
    }

    public OrderAddPaymentPanel(Context context) {
        super(context);
    }

    public OrderAddPaymentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderAddPaymentPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, CheckoutPayment item, int position) {
        CardPaymentMethodContentBinding binding = DataBindingUtil.bind(view);
        binding.setPaymentMethod(item);
        ImageView im_payment = (ImageView) view.findViewById(R.id.im_payment);
        if (item.getType().equals("1")) {
            im_payment.setImageResource(R.drawable.ic_credit_card);
        } else if (item.getCode().equals("cashforpos")) {
            im_payment.setImageResource(R.drawable.ic_cash);
        } else if (item.getCode().equals("ccforpos")) {
            im_payment.setImageResource(R.drawable.ic_credit_card);
        } else if (item.getCode().equals("codforpos")) {
            im_payment.setImageResource(R.drawable.ic_cod);
        } else {
            im_payment.setImageResource(R.drawable.ic_payment);
        }
    }

    @Override
    protected void onClickItem(View view, CheckoutPayment item, int position) {
        mOrderHistoryListController.onAddPaymentMethod(item);
    }
}
