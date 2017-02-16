package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardPaymentMethodContentBinding;

/**
 * Created by Johan on 2/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddPaymentPanel extends AbstractListPanel<CheckoutPayment> {
    CheckoutListController mCheckoutListController;

    public CheckoutAddPaymentPanel(Context context) {
        super(context);
    }

    public CheckoutAddPaymentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutAddPaymentPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách comment của khách hàng
        View v = inflate(getContext(), R.layout.panel_checkout_add_payment_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách comment
        setLayoutItem(R.layout.card_payment_method_content);

        // Chuẩn bị list danh sách comment
        mRecycleView = (RecyclerView) findViewById(R.id.checkout_add_payment_list);
        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 4));
    }

    @Override
    protected void bindItem(View view, final CheckoutPayment item, int position) {
        CardPaymentMethodContentBinding binding = DataBindingUtil.bind(view);
        binding.setPaymentMethod(item);
    }

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }
}
