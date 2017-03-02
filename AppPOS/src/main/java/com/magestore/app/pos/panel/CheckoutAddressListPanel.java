package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardCheckoutAddressContentBinding;

/**
 * Created by Johan on 2/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddressListPanel extends AbstractSimpleRecycleView<CustomerAddress> {
    CheckoutListController mCheckoutListController;
    int selectPos = 0;

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
        // Đặt các trường text vào danh sách
        CardCheckoutAddressContentBinding binding = DataBindingUtil.bind(view);
        binding.setCustomerAddress(item);

        LinearLayout ll_checkout_address = (LinearLayout) view.findViewById(R.id.ll_checkout_address);
        TextView txt_default_address = (TextView) view.findViewById(R.id.txt_default_address);

        if (position == 0) {
            txt_default_address.setText(getContext().getString(R.string.checkout_address_item_default));
        } else {
            txt_default_address.setText("");
        }

        if (selectPos == position) {
            ll_checkout_address.setBackgroundResource(R.drawable.checkout_address_border_item_select);
        } else {
            ll_checkout_address.setBackgroundResource(R.drawable.checkout_address_border_item);
        }
    }

    @Override
    protected void onClickItem(View view, CustomerAddress item, int position) {
        mCheckoutListController.changeShippingAddress(item);
        selectPos = position;
    }
}
