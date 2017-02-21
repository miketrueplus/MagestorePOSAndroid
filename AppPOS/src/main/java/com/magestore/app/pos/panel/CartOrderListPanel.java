package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;

/**
 * Created by Johan on 2/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CartOrderListPanel extends AbstractSimpleRecycleView<Checkout> {
    CheckoutListController mCheckoutListController;
    TextView checkout_name;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CartOrderListPanel(Context context) {
        super(context);
    }

    public CartOrderListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CartOrderListPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initLayout() {

    }

    @Override
    protected void bindItem(View view, Checkout item, int position) {
        checkout_name = (TextView) view.findViewById(R.id.checkout_name);
        String customer_name = item.getCustomer().getName();
        Customer guest = mCheckoutListController.getGuestCheckout();
        if (customer_name.length() > 0 && !guest.getName().equals(customer_name)) {
            String order_name = customer_name.substring(0, 1);
            checkout_name.setText(order_name);
        } else {
            int order_index = position + 1;
            checkout_name.setText(String.valueOf(order_index));
        }
    }

    @Override
    protected void onClickItem(View view, Checkout item, int position) {

    }

    public void notifyDataSetChanged(){
        notifyDataSetChanged();
    }
}
