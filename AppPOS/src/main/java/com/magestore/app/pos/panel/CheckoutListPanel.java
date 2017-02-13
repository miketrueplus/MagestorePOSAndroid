package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.R;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.databinding.PanelCheckoutListBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class CheckoutListPanel extends AbstractListPanel<Checkout> {
    private PanelCheckoutListBinding mBinding;
    CheckoutAddCustomerPanel mCheckoutAddCustomerPanel;
    MagestoreDialog dialog;
    Checkout mCheckout;
    MagestoreContext mMagestoreContext;
    CustomerService mCustomerService;
    Toolbar toolbar_order;
    Customer mCustomer;
    CustomerAddNewPanel mCustomerAddNewPanel;
    Button btn_create_customer, btn_use_guest;
    FrameLayout fr_sales_new_customer;
    LinearLayout ll_add_new_customer, ll_new_shipping_address, ll_new_billing_address, ll_shipping_address;
    LinearLayout ll_billing_address, ll_short_shipping_address, ll_short_billing_address, ll_sales_add_customer;
    ImageButton btn_shipping_address, btn_billing_address;
    ImageButton btn_shipping_adrress_edit, btn_billing_adrress_edit;
    ImageButton btn_shipping_address_delete, btn_billing_address_delete;

    public CheckoutListPanel(Context context) {
        super(context);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    public void setMagestoreContext(MagestoreContext mMagestoreContext) {
        this.mMagestoreContext = mMagestoreContext;
    }

    public void setToolbarOrder(Toolbar toolbar_order) {
        this.toolbar_order = toolbar_order;
    }

    @Override
    protected void bindItem(View view, Checkout item, int position) {
        mCheckout = item;
    }

    @Override
    public void initLayout() {
        super.initLayout();
        mBinding = DataBindingUtil.bind(getView());

        ((Button) findViewById(R.id.btn_sales_order_checkout)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().doShowDetailPanel(true);
            }
        });

    }

    /**
     * Cập nhật bảng giá tổng
     */
    public void updateTotalPrice(Checkout checkout) {
        mBinding.setCheckout(checkout);
    }

    public void showPopUpAddCustomer() {
        if (mCheckoutAddCustomerPanel == null) {
            mCheckoutAddCustomerPanel = new CheckoutAddCustomerPanel(getContext());
            mCheckoutAddCustomerPanel.setMagestoreContext(mMagestoreContext);
            mCheckoutAddCustomerPanel.setCustomerService(mCustomerService);
            mCheckoutAddCustomerPanel.setCheckoutListPanel(this);
            mCheckoutAddCustomerPanel.bindItem(mCheckout);
            mCheckoutAddCustomerPanel.setController(mController);
            mCheckoutAddCustomerPanel.initModel();
            mCheckoutAddCustomerPanel.initValue();

            mCustomerAddNewPanel = (CustomerAddNewPanel) mCheckoutAddCustomerPanel.findViewById(R.id.sales_new_customer);
            mCustomerAddNewPanel.setController(mCheckoutAddCustomerPanel.getCustomerListController());
            mCustomerAddNewPanel.bindItem(mCustomer);

            initLayoutPanel();
        }

        if (dialog == null) {
            dialog = DialogUtil.dialog(getContext(), "", mCheckoutAddCustomerPanel);
            dialog.setGoneButtonSave(true);
        }

        dialog.show();

        if (mCustomer == null) {
            fr_sales_new_customer.setVisibility(GONE);
            ll_sales_add_customer.setVisibility(VISIBLE);
            dialog.getButtonSave().setVisibility(GONE);
            dialog.getDialogTitle().setText("");
        }

        actionPanel();
        actionDialog();
    }

    public void dismissDialogShowCustomer(Customer customer) {
        mCustomer = customer;
        ((TextView) toolbar_order.findViewById(R.id.text_customer_name)).setText(customer.getName());
        dialog.dismiss();
    }

    /**
     * khởi tạo layout
     */
    private void initLayoutPanel() {
        btn_create_customer = (Button) mCheckoutAddCustomerPanel.findViewById(R.id.btn_create_customer);
        btn_use_guest = (Button) mCheckoutAddCustomerPanel.findViewById(R.id.btn_use_guest);
        fr_sales_new_customer = (FrameLayout) mCheckoutAddCustomerPanel.findViewById(R.id.fr_sales_new_customer);
        ll_sales_add_customer = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_sales_add_customer);

        ll_add_new_customer = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_add_new_customer);

        ll_new_shipping_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_new_shipping_address);

        ll_new_billing_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_new_billing_address);

        ll_shipping_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_shipping_address);

        ll_billing_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_billing_address);

        ll_short_shipping_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_short_shipping_address);

        ll_short_billing_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_short_billing_address);

        btn_shipping_address = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_address);

        btn_billing_address = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_address);

        btn_shipping_adrress_edit = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_adrress_edit);

        btn_billing_adrress_edit = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_adrress_edit);

        btn_shipping_address_delete = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_address_delete);

        btn_billing_address_delete = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_address_delete);
    }

    private void actionPanel() {
        btn_use_guest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomer = null;
                dialog.dismiss();
            }
        });

        btn_create_customer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getButtonSave().setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                fr_sales_new_customer.setVisibility(VISIBLE);
                ll_sales_add_customer.setVisibility(GONE);
            }
        });
    }

    private void actionDialog() {
        ll_shipping_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_shipping_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                if (mCustomerAddNewPanel.getShippingAddress() != null) {
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
                if (mCustomerAddNewPanel.getBillingAddress() != null) {
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
                mCustomerAddNewPanel.deleteShippingAddress();
            }
        });

        btn_billing_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_short_billing_address.setVisibility(GONE);
                btn_billing_address.setVisibility(VISIBLE);
                mCustomerAddNewPanel.deleteBillingAddress();
            }
        });
    }

    private void onClickDialogSave() {
        if (ll_new_shipping_address.getVisibility() == VISIBLE) {
            if (mCustomerAddNewPanel.checkRequiedShippingAddress()) {
                mCustomerAddNewPanel.insertShippingAddress();
                ll_short_shipping_address.setVisibility(VISIBLE);
                mCustomerAddNewPanel.showShortShippingAddress();
                btn_shipping_address.setVisibility(GONE);
                if (mCustomerAddNewPanel.checkSameBillingAndShipping()) {
                    ll_short_billing_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.showShortBillingAddress();
                    btn_billing_address.setVisibility(GONE);
                }
                ll_add_new_customer.setVisibility(VISIBLE);
                ll_new_shipping_address.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
            }
        } else if (ll_new_billing_address.getVisibility() == VISIBLE) {
            if (mCustomerAddNewPanel.checkRequiedBillingAddress()) {
                mCustomerAddNewPanel.insertBillingAddress();
                ll_short_billing_address.setVisibility(VISIBLE);
                mCustomerAddNewPanel.showShortBillingAddress();
                btn_billing_address.setVisibility(GONE);
                ll_add_new_customer.setVisibility(VISIBLE);
                ll_new_shipping_address.setVisibility(GONE);
                ll_new_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
            }
        } else {
            if (mCustomerAddNewPanel.checkRequiedCustomer()) {
                Customer customer = mCustomerAddNewPanel.returnCustomer();
                mCheckoutAddCustomerPanel.getCustomerListController().doInsert(customer);
                dialog.dismiss();
            }
        }
    }

    private void onClickDialogCancel() {
        if (ll_new_shipping_address.getVisibility() == VISIBLE || ll_new_billing_address.getVisibility() == VISIBLE) {
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_shipping_address.getVisibility() == VISIBLE) {
                mCustomerAddNewPanel.deleteShippingAddress();
                btn_shipping_address.setVisibility(VISIBLE);
                ll_short_shipping_address.setVisibility(GONE);
            }
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_billing_address.getVisibility() == VISIBLE) {
                mCustomerAddNewPanel.deleteBillingAddress();
                btn_billing_address.setVisibility(VISIBLE);
                ll_short_billing_address.setVisibility(GONE);
            }
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else {
            if (fr_sales_new_customer.getVisibility() == VISIBLE) {
                dialog.getButtonSave().setVisibility(GONE);
                dialog.getDialogTitle().setText("");
                fr_sales_new_customer.setVisibility(GONE);
                ll_sales_add_customer.setVisibility(VISIBLE);
            }else {
                dialog.dismiss();
            }
        }
    }
}
