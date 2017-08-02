package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.databinding.CardStarPrintListContentBinding;

/**
 * Created by Johan on 8/1/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class StarPrintListItemPanel extends AbstractSimpleRecycleView<CartItem> {
    public StarPrintListItemPanel(Context context) {
        super(context);
    }

    public StarPrintListItemPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StarPrintListItemPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, CartItem item, int position) {
        CardStarPrintListContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setItem(item);
    }

    @Override
    protected void onClickItem(View view, CartItem item, int position) {

    }
}
