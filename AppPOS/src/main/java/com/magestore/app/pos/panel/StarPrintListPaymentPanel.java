package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.pos.databinding.CardStarPrintListContentBinding;
import com.magestore.app.pos.databinding.CardStarPrintListPaymentContentBinding;

/**
 * Created by Johan on 8/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrintListPaymentPanel extends AbstractSimpleListView<OrderWebposPayment> {
    public StarPrintListPaymentPanel(Context context) {
        super(context);
    }

    public StarPrintListPaymentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarPrintListPaymentPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, OrderWebposPayment item, int position) {
        CardStarPrintListPaymentContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setOrderPayment(item);
    }

    @Override
    public void showErrorMsgWithReload(String strMsg) {

    }

    @Override
    public void showErrorMsgWithReload(Exception exp) {

    }
}
