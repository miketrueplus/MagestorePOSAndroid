package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.registershift.SaleSummary;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.databinding.CardStarPrintListPaymentContentBinding;
import com.magestore.app.pos.databinding.CardStarPrintSessionListPaymentContentBinding;

/**
 * Created by Johan on 8/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrinSessiontListPaymentPanel extends AbstractSimpleRecycleView<SaleSummary> {
    public StarPrinSessiontListPaymentPanel(Context context) {
        super(context);
    }

    public StarPrinSessiontListPaymentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarPrinSessiontListPaymentPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, SaleSummary item, int position) {
        CardStarPrintSessionListPaymentContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setSaleSummary(item);
    }

    @Override
    protected void onClickItem(View view, SaleSummary item, int position) {

    }
}
