package com.magestore.app.pos.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardCustomerListContentBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

/**
 * Panel giao diện quản lý danh sách khách hàng
 * Created by Mike on 12/29/2016.
 * Magestore
 * mike@trueplus.vn
 */
public class CustomerListPanel extends AbstractListPanel<Customer> {
    Customer mCustomer;

    /**
     * Khởi tạo
     *
     * @param context
     */
    public CustomerListPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     */
    public CustomerListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CustomerListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, Customer item, int position) {
        CardCustomerListContentBinding binding = DataBindingUtil.bind(view);
        binding.setCustomer(item);
        mCustomer = item;
    }

    @Override
    public void initLayout() {
        // Xử lý sự kiện floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionButtonAddNewCustomer();
            }
        });
    }

    private void actionButtonAddNewCustomer() {
        CustomerAddNewPanel customerAddNewPanel = new CustomerAddNewPanel(getContext());
        customerAddNewPanel.setController(mController);
        customerAddNewPanel.bindItem(mCustomer);

        final MagestoreDialog dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.customer_add_new), customerAddNewPanel);
        dialog.show();

        final LinearLayout ll_add_new_customer = (LinearLayout) dialog.findViewById(R.id.ll_add_new_customer);

        final LinearLayout ll_new_shipping_address = (LinearLayout) dialog.findViewById(R.id.ll_new_shipping_address);

        final LinearLayout ll_new_billing_address = (LinearLayout) dialog.findViewById(R.id.ll_new_billing_address);

        dialog.getButtonCancel().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_new_shipping_address.getVisibility() == VISIBLE || ll_new_billing_address.getVisibility() == VISIBLE) {
                    ll_add_new_customer.setVisibility(VISIBLE);
                    ll_new_shipping_address.setVisibility(GONE);
                    ll_new_billing_address.setVisibility(GONE);
                } else {
                    dialog.dismiss();
                }
            }
        });
    }
}
