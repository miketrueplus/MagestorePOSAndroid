package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardCustomerAddressContentBinding;

/**
 * Created by Johan on 2/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddressListPanel extends AbstractSimpleRecycleView<CustomerAddress> {
    CheckoutListController mCheckoutListController;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutAddressListPanel(Context context) {
        super(context);
    }

    public CheckoutAddressListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutAddressListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void bindItem(View view, CustomerAddress item, int position) {
        RelativeLayout rl_action_address = (RelativeLayout) view.findViewById(R.id.rl_action_address);
        rl_action_address.setVisibility(GONE);

        // Đặt các trường text vào danh sách
        CardCustomerAddressContentBinding binding = DataBindingUtil.bind(view);
        binding.setCustomerAddress(item);
    }

    @Override
    protected void onClickItem(View view, CustomerAddress item, int position) {
        mCheckoutListController.changeShippingAddress(item);
    }
}
