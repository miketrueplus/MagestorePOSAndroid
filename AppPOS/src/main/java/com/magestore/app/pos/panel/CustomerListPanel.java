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
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CustomerListController;
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
    LinearLayout ll_add_new_customer, ll_new_shipping_address, ll_new_billing_address, ll_shipping_address;
    LinearLayout ll_billing_address, ll_short_shipping_address, ll_short_billing_address;
    ImageButton btn_shipping_address, btn_billing_address;
    ImageButton btn_shipping_adrress_edit, btn_billing_adrress_edit;
    ImageButton btn_shipping_address_delete, btn_billing_address_delete;
    MagestoreDialog dialog;
    CustomerAddNewPanel customerAddNewPanel;

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
        customerAddNewPanel = new CustomerAddNewPanel(getContext());
        customerAddNewPanel.setController(mController);
        customerAddNewPanel.bindItem(mCustomer);

        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.customer_add_new), customerAddNewPanel);
        dialog.show();

        initLayoutDialog(dialog);

        ll_shipping_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_shipping_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                if (customerAddNewPanel.getShippingAddress() != null) {
                    dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                }
            }
        });

        ll_billing_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_shipping_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                if (customerAddNewPanel.getBillingAddress() != null) {
                    dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                }
            }
        });

        dialog.getButtonCancel().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDialogCancel();
            }
        });

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDialogSave();
            }
        });

        btn_shipping_adrress_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_shipping_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
            }
        });

        btn_billing_adrress_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_shipping_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
            }
        });

        btn_shipping_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_short_shipping_address.setVisibility(GONE);
                btn_shipping_address.setVisibility(VISIBLE);
                customerAddNewPanel.deleteShippingAddress();
            }
        });

        btn_billing_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_short_billing_address.setVisibility(GONE);
                btn_billing_address.setVisibility(VISIBLE);
                customerAddNewPanel.deleteBillingAddress();
            }
        });
    }

    /**
     *  khởi tạo layout
     *
     * @param dialog
     */
    private void initLayoutDialog(MagestoreDialog dialog) {
        ll_add_new_customer = (LinearLayout) dialog.findViewById(R.id.ll_add_new_customer);

        ll_new_shipping_address = (LinearLayout) dialog.findViewById(R.id.ll_new_shipping_address);

        ll_new_billing_address = (LinearLayout) dialog.findViewById(R.id.ll_new_billing_address);

        ll_shipping_address = (LinearLayout) dialog.findViewById(R.id.ll_shipping_address);

        ll_billing_address = (LinearLayout) dialog.findViewById(R.id.ll_billing_address);

        ll_short_shipping_address = (LinearLayout) dialog.findViewById(R.id.ll_short_shipping_address);

        ll_short_billing_address = (LinearLayout) dialog.findViewById(R.id.ll_short_billing_address);

        btn_shipping_address = (ImageButton) dialog.findViewById(R.id.btn_shipping_address);

        btn_billing_address = (ImageButton) dialog.findViewById(R.id.btn_billing_address);

        btn_shipping_adrress_edit = (ImageButton) dialog.findViewById(R.id.btn_shipping_adrress_edit);

        btn_billing_adrress_edit = (ImageButton) dialog.findViewById(R.id.btn_billing_adrress_edit);

        btn_shipping_address_delete = (ImageButton) dialog.findViewById(R.id.btn_shipping_address_delete);

        btn_billing_address_delete = (ImageButton) dialog.findViewById(R.id.btn_billing_address_delete);
    }

    private void onClickDialogSave(){
        if (ll_new_shipping_address.getVisibility() == VISIBLE) {
            if (customerAddNewPanel.checkRequiedShippingAddress()) {
                customerAddNewPanel.insertShippingAddress();
                ll_short_shipping_address.setVisibility(VISIBLE);
                customerAddNewPanel.showShortShippingAddress();
                btn_shipping_address.setVisibility(GONE);
                if (customerAddNewPanel.checkSameBillingAndShipping()) {
                    ll_short_billing_address.setVisibility(VISIBLE);
                    customerAddNewPanel.showShortBillingAddress();
                    btn_billing_address.setVisibility(GONE);
                }
                ll_add_new_customer.setVisibility(VISIBLE);
                ll_new_shipping_address.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
            }
        } else if (ll_new_billing_address.getVisibility() == VISIBLE) {
            if (customerAddNewPanel.checkRequiedBillingAddress()) {
                customerAddNewPanel.insertBillingAddress();
                ll_short_billing_address.setVisibility(VISIBLE);
                customerAddNewPanel.showShortBillingAddress();
                btn_billing_address.setVisibility(GONE);
                ll_add_new_customer.setVisibility(VISIBLE);
                ll_new_shipping_address.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
            }
        } else {
            if (customerAddNewPanel.checkRequiedCustomer()) {
                Customer customer = customerAddNewPanel.returnCustomer();
                ((CustomerListController) mController).doInsert(customer);
                dialog.dismiss();
            }
        }
    }

    private void onClickDialogCancel(){
        if (ll_new_shipping_address.getVisibility() == VISIBLE || ll_new_billing_address.getVisibility() == VISIBLE) {
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_shipping_address.getVisibility() == VISIBLE) {
                customerAddNewPanel.deleteShippingAddress();
                btn_shipping_address.setVisibility(VISIBLE);
                ll_short_shipping_address.setVisibility(GONE);
            }
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_billing_address.getVisibility() == VISIBLE) {
                customerAddNewPanel.deleteBillingAddress();
                btn_billing_address.setVisibility(VISIBLE);
                ll_short_billing_address.setVisibility(GONE);
            }
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else {
            dialog.dismiss();
        }
    }
}
