package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.R;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.controller.CheckoutListController;
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
    FloatingActionButton bt_sales_discount, bt_custom_sales;
    FloatingActionMenu bt_sales_menu;
    Button btn_create_customer, btn_use_guest, btn_sales_order_checkout;
    FrameLayout fr_sales_new_customer;
    LinearLayout ll_add_new_customer, ll_new_shipping_address, ll_new_billing_address, ll_shipping_address, ll_sales_shipping, ll_add_new_address;
    LinearLayout ll_billing_address, ll_short_shipping_address, ll_short_billing_address, ll_sales_add_customer, ll_action_checkout;
    ImageView btn_shipping_address, btn_billing_address;
    ImageButton btn_shipping_adrress_edit, btn_billing_adrress_edit;
    ImageButton btn_shipping_address_delete, btn_billing_address_delete;
    RelativeLayout rl_add_checkout, rl_remove_checkout, rl_sales_total, cart_background_loading;
    public static int NO_TYPE = -1;
    public static int CHANGE_CUSTOMER = 0;
    public static int CREATE_NEW_CUSTOMER = 1;
    public static int CREATE_NEW_ADDRESS = 2;
    public static int CHECKOUT_ADD_NEW_ADDRESS = 3;
    int typeCustomer;
    int other_type;

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

        btn_sales_order_checkout = (Button) findViewById(R.id.btn_sales_order_checkout);
        ll_action_checkout = (LinearLayout) findViewById(R.id.ll_action_checkout);
        ll_sales_shipping = (LinearLayout) findViewById(R.id.ll_sales_shipping);
        rl_sales_total = (RelativeLayout) findViewById(R.id.rl_sales_total);
        rl_add_checkout = (RelativeLayout) findViewById(R.id.rl_add_checkout);
        rl_remove_checkout = (RelativeLayout) findViewById(R.id.rl_remove_checkout);
        bt_sales_menu = (FloatingActionMenu) findViewById(R.id.bt_sales_menu);
        bt_sales_discount = (FloatingActionButton) findViewById(R.id.bt_sales_discount);
        bt_custom_sales = (FloatingActionButton) findViewById(R.id.bt_custom_sales);
        cart_background_loading = (RelativeLayout) findViewById(R.id.cart_background_loading);
        initValue();
    }

    @Override
    public void initValue() {
        btn_sales_order_checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String place_order = getContext().getString(R.string.sales_place_holder);
                if (btn_sales_order_checkout.getText().equals(place_order)) {
                    ((CheckoutListController) getController()).doInputPlaceOrder();
                } else {
                    ((CheckoutListController) getController()).doInputSaveCart();
                }
            }
        });

        rl_add_checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckoutListController) mController).addNewOrder();
            }
        });

        rl_remove_checkout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckoutListController) mController).checkItemInOrder()) {
                    com.magestore.app.util.DialogUtil.confirm(getContext(),
                            R.string.checkout_delete_order,
                            R.string.title_confirm_delete,
                            R.string.confirm,
                            R.string.no,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ((CheckoutListController) mController).removeOrder();
                                }
                            });
                }
            }
        });

        bt_sales_discount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutDiscountPanel mCheckoutDiscountPanel = new CheckoutDiscountPanel(getContext());
                mCheckoutDiscountPanel.setCheckoutListController(((CheckoutListController) mController));
                mCheckoutDiscountPanel.initValue();
                MagestoreDialog dialog = DialogUtil.dialog(getContext(), "", mCheckoutDiscountPanel);
                dialog.setDialogTitle(getContext().getString(R.string.checkout_discount_all));
                dialog.setDialogSave(getContext().getString(R.string.apply));
                dialog.show();
            }
        });

        bt_custom_sales.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckoutListController) getController()).onShowCustomSale();
            }
        });
    }

    public void useDefaultGuestCheckout(Customer customer) {
        // config guest checkout default add to order
        mCustomer = customer;
        updateCustomerToOrder(mCustomer);
    }

    public void showSalesShipping(boolean isShow) {
        if (isShow) {
//            ll_sales_total.setVisibility(VISIBLE);
            ll_sales_shipping.setVisibility(VISIBLE);
        } else {
//            ll_sales_total.setVisibility(GONE);
            ll_sales_shipping.setVisibility(GONE);
        }
    }

    public void changeActionButton(boolean ischange) {
        if (ischange) {
            btn_sales_order_checkout.setText(getContext().getString(R.string.sales_place_holder));
        } else {
            btn_sales_order_checkout.setText(getContext().getString(R.string.checkout));
        }
    }

    public void showButtonDiscount(boolean isShow){
        bt_sales_discount.setVisibility(isShow ? VISIBLE : GONE);
    }

    /**
     * Cập nhật bảng giá tổng
     */
    public void updateTotalPrice(Checkout checkout) {
        mBinding.setCheckout(checkout);
    }

    public void showPopUpAddCustomer(int type, int other_type) {
        // TODO: check cơ chế customer dialog
        String guest_id = ((CheckoutListController) mController).getGuestCheckout().getID();
        if (mCustomer != null && mCustomer.getID().equals(guest_id)) {
            type = CHANGE_CUSTOMER;
        }
        this.other_type = other_type;
        typeCustomer = type;
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
            initLayoutPanel();
            if (type == CHANGE_CUSTOMER) {
                mCustomerAddNewPanel.bindItem(null);
                ll_add_new_address.setVisibility(GONE);
            } else {
                mCustomerAddNewPanel.bindItem(mCustomer);
                ll_add_new_address.setVisibility(VISIBLE);
            }
        } else {
            if (mCustomer != null) {
                if (type == CHANGE_CUSTOMER) {
                    mCustomerAddNewPanel.bindItem(null);
                    ll_add_new_address.setVisibility(VISIBLE);
                } else {
                    mCustomerAddNewPanel.bindItem(mCustomer);
                    ll_add_new_address.setVisibility(VISIBLE);
                }
            } else {
                ll_add_new_address.setVisibility(GONE);
            }
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
        } else {
            if (type == CHANGE_CUSTOMER) {
                fr_sales_new_customer.setVisibility(GONE);
                ll_sales_add_customer.setVisibility(VISIBLE);
                dialog.getButtonSave().setVisibility(GONE);
                dialog.getDialogTitle().setText("");
            } else {
                fr_sales_new_customer.setVisibility(VISIBLE);
                ll_sales_add_customer.setVisibility(GONE);
                dialog.getButtonSave().setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(mCustomer.getName());
            }
        }

        actionPanel();
        actionDialog();
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
        btn_shipping_address = (ImageView) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_address);
        btn_billing_address = (ImageView) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_address);
        btn_shipping_adrress_edit = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_adrress_edit);
        btn_billing_adrress_edit = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_adrress_edit);
        btn_shipping_address_delete = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_shipping_address_delete);
        btn_billing_address_delete = (ImageButton) mCheckoutAddCustomerPanel.findViewById(R.id.btn_billing_address_delete);
        ll_add_new_address = (LinearLayout) mCheckoutAddCustomerPanel.findViewById(R.id.ll_add_new_address);
    }

    private void actionPanel() {
        btn_use_guest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomer = null;
                mCustomer = ((CheckoutListController) getController()).getGuestCheckout();
                updateCustomerToOrder(mCustomer);
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
                mCustomerAddNewPanel.bindItem(null);
                mCustomerAddNewPanel.deleteBillingAddress();
                mCustomerAddNewPanel.deleteShippingAddress();
                typeCustomer = CREATE_NEW_CUSTOMER;
                ll_add_new_address.setVisibility(GONE);
            }
        });

        ll_add_new_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.getButtonSave().setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new_address));
                typeCustomer = CREATE_NEW_ADDRESS;
                ll_add_new_customer.setVisibility(GONE);
                mCustomerAddNewPanel.deleteBillingAddress();
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_new_shipping_address.setVisibility(GONE);
                dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
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
                if (mCustomer == null) {
                    if (mCustomerAddNewPanel.getShippingAddress() != null) {
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    }
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    }
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
                if (mCustomer == null) {
                    if (mCustomerAddNewPanel.getBillingAddress() != null) {
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    }
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    }
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
                if (mCustomer == null) {
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                    dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_shipping_address));
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    } else {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_edit_shipping_address));
                    }
                }
            }
        });

        btn_billing_adrress_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_new_billing_address.setVisibility(VISIBLE);
                ll_add_new_customer.setVisibility(GONE);
                ll_new_shipping_address.setVisibility(GONE);
                if (mCustomer == null) {
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                    dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                } else {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_billing_address));
                        dialog.getButtonCancel().setText(getContext().getString(R.string.delete));
                    } else {
                        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_edit_billing_address));
                    }
                }
            }
        });

        btn_shipping_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCustomer != null) {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        ll_short_shipping_address.setVisibility(GONE);
                        btn_shipping_address.setVisibility(VISIBLE);
                        mCustomerAddNewPanel.deleteShippingAddress();
                    } else {
                        ((CheckoutListController) getController()).doInputDeleteAddress(0, mCustomer, mCustomerAddNewPanel.getChangeshippingAddress());
                    }
                } else {
                    ll_short_shipping_address.setVisibility(GONE);
                    btn_shipping_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.deleteShippingAddress();
                }
            }
        });

        btn_billing_address_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCustomer != null) {
                    if (typeCustomer == CREATE_NEW_CUSTOMER) {
                        ll_short_billing_address.setVisibility(GONE);
                        btn_billing_address.setVisibility(VISIBLE);
                        mCustomerAddNewPanel.deleteBillingAddress();
                    } else {
                        ((CheckoutListController) getController()).doInputDeleteAddress(1, mCustomer, mCustomerAddNewPanel.getChangebillingAddress());
                    }
                } else {
                    ll_short_billing_address.setVisibility(GONE);
                    btn_billing_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.deleteBillingAddress();
                }
            }
        });
    }

    private void onClickDialogSave() {
        if (ll_new_shipping_address.getVisibility() == VISIBLE) {
            mCustomerAddNewPanel.insertShippingAddress();
            if (mCustomer != null) {
                if (!mCustomerAddNewPanel.checkRequiedShippingAddress()) {
                    return;
                }
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    if (!mCustomerAddNewPanel.checkSameBillingAndShipping()) {
                        return;
                    }
                    ll_short_billing_address.setVisibility(VISIBLE);
                    mCustomerAddNewPanel.showNewShortBillingAddress();
                    btn_billing_address.setVisibility(GONE);
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                } else {
                    ((CheckoutListController) getController()).doInputEditAddress(0, mCustomer, mCustomerAddNewPanel.getChangeshippingAddress(), mCustomerAddNewPanel.getShippingAddress());
                }
            } else {
                if (!mCustomerAddNewPanel.checkRequiedShippingAddress()) {
                    return;
                }
                if (!mCustomerAddNewPanel.checkSameBillingAndShipping()) {
                    return;
                }
                ll_short_billing_address.setVisibility(VISIBLE);
                mCustomerAddNewPanel.showShortBillingAddress();
                btn_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            }
            btn_shipping_address.setVisibility(GONE);
            ll_short_shipping_address.setVisibility(VISIBLE);
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            if (typeCustomer == CREATE_NEW_CUSTOMER) {
                mCustomerAddNewPanel.showNewShortShippingAddress();
            } else {
                mCustomerAddNewPanel.showShortShippingAddress();
            }
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else if (ll_new_billing_address.getVisibility() == VISIBLE) {
            mCustomerAddNewPanel.insertBillingAddress();
            if (mCustomer != null) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    if (!mCustomerAddNewPanel.checkRequiedBillingAddress()) {
                        return;
                    }
                    dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
                } else if (typeCustomer == CREATE_NEW_ADDRESS) {
                    int type_new_address = 0;
                    if (other_type == CHECKOUT_ADD_NEW_ADDRESS) {
                        type_new_address = 1;
                    }
                    ((CheckoutListController) getController()).doInputNewAddress(mCustomer, mCustomerAddNewPanel.getBillingAddress(), type_new_address);
                    if (other_type == CHECKOUT_ADD_NEW_ADDRESS) {
                        dialog.dismiss();
                        return;
                    }
                } else {
                    ((CheckoutListController) getController()).doInputEditAddress(1, mCustomer, mCustomerAddNewPanel.getChangebillingAddress(), mCustomerAddNewPanel.getBillingAddress());
                }
            } else {
                if (!mCustomerAddNewPanel.checkRequiedBillingAddress()) {
                    return;
                }
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            }
            btn_billing_address.setVisibility(GONE);
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            ll_short_billing_address.setVisibility(VISIBLE);
            if (typeCustomer != CREATE_NEW_ADDRESS) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    mCustomerAddNewPanel.showNewShortBillingAddress();
                } else {
                    mCustomerAddNewPanel.showShortBillingAddress();
                }
            }
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else {
            Customer customer = mCustomerAddNewPanel.returnCustomer();
            if (mCustomer != null) {
                if (typeCustomer == CREATE_NEW_CUSTOMER) {
                    if (!mCustomerAddNewPanel.checkRequiedCustomer()) {
                        return;
                    }
                    mCheckoutAddCustomerPanel.getCustomerListController().doInsert(customer);
                } else {
                    CustomerAddress shippingAddress = mCustomerAddNewPanel.getChangeshippingAddress();
                    CustomerAddress billingAddress = mCustomerAddNewPanel.getChangebillingAddress();
                    mCustomer.getAddress().remove(shippingAddress);
                    mCustomer.getAddress().add(0, shippingAddress);
                    if (shippingAddress.getID().equals(billingAddress.getID())) {
                        mCustomer.setUseOneAddress(true);
                    } else {
                        mCustomer.setUseOneAddress(false);
                        mCustomer.getAddress().remove(billingAddress);
                        mCustomer.getAddress().add(1, billingAddress);
                    }
                    if (checkChangeCustomer(customer)) {
                        mCheckoutAddCustomerPanel.getCustomerListController().doUpdate(mCustomer, customer);
                    }
                }
                dialog.dismiss();
            } else {
                if (!mCustomerAddNewPanel.checkRequiedCustomer()) {
                    return;
                }
                mCheckoutAddCustomerPanel.getCustomerListController().doInsert(customer);
                dialog.dismiss();
            }
        }
    }

    private boolean checkChangeCustomer(Customer customer) {
        if (mCustomer.getFirstName().equals(customer.getFirstName())) {
            return true;
        }
        if (mCustomer.getLastName().equals(customer.getLastName())) {
            return true;
        }
        if (mCustomer.getEmail().equals(customer.getEmail())) {
            return true;
        }
        if (mCustomer.getGroupID().equals(customer.getGroupID())) {
            return true;
        }
        return false;
    }

    private void onClickDialogCancel() {
        if (ll_new_shipping_address.getVisibility() == VISIBLE || ll_new_billing_address.getVisibility() == VISIBLE) {
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_shipping_address.getVisibility() == VISIBLE) {
                mCustomerAddNewPanel.deleteShippingAddress();
                btn_shipping_address.setVisibility(VISIBLE);
                ll_short_shipping_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            } else if (ll_new_shipping_address.getVisibility() == VISIBLE) {
                btn_shipping_address.setVisibility(GONE);
                ll_short_shipping_address.setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(mCustomer.getName());
            }
            if (dialog.getButtonCancel().getText().toString().equals(getContext().getString(R.string.delete)) && ll_new_billing_address.getVisibility() == VISIBLE) {
                mCustomerAddNewPanel.deleteBillingAddress();
                btn_billing_address.setVisibility(VISIBLE);
                ll_short_billing_address.setVisibility(GONE);
                dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new));
            } else if (ll_new_billing_address.getVisibility() == VISIBLE) {
                if (other_type == CHECKOUT_ADD_NEW_ADDRESS) {
                    dialog.dismiss();
                    return;
                }
                btn_billing_address.setVisibility(GONE);
                ll_short_billing_address.setVisibility(VISIBLE);
                dialog.getDialogTitle().setText(mCustomer.getName());
            }
            ll_add_new_customer.setVisibility(VISIBLE);
            ll_new_shipping_address.setVisibility(GONE);
            ll_new_billing_address.setVisibility(GONE);
            dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
        } else {
            if (fr_sales_new_customer.getVisibility() == VISIBLE) {
                if (mCustomer != null) {
                    dialog.dismiss();
                } else {
                    dialog.getButtonSave().setVisibility(GONE);
                    dialog.getDialogTitle().setText("");
                    fr_sales_new_customer.setVisibility(GONE);
                    ll_sales_add_customer.setVisibility(VISIBLE);
                }
            } else {
                dialog.dismiss();
            }
        }
    }

    public void updateCustomerToOrder(Customer customer) {
        mCustomer = customer;
        ((TextView) toolbar_order.findViewById(R.id.text_customer_name)).setText(customer.getName());
        ((CheckoutListController) getController()).bindCustomer(customer);
        if (dialog != null)
            dialog.dismiss();
    }

    public void changeCustomerInToolBar(Customer customer) {
        mCustomer = customer;
        ((TextView) toolbar_order.findViewById(R.id.text_customer_name)).setText(customer.getName());
    }

    public void updateAddress(int typeAction, int typeAddress, CustomerAddress customerAddress) {
        if (typeAction == 0) {
            mCustomerAddNewPanel.updateAddress(typeAddress, mCustomer.getAddress(), customerAddress);
        } else {
            mCustomerAddNewPanel.updateAddress(typeAddress, mCustomer.getAddress(), mCustomer.getAddress().get(0));
        }
    }

    public void updateCheckoutAddress() {
        updateCustomerToOrder(mCustomer);
    }

    public void checkoutAddNewAddress() {
        showPopUpAddCustomer(NO_TYPE, CHECKOUT_ADD_NEW_ADDRESS);
        dialog.getButtonSave().setVisibility(VISIBLE);
        dialog.getDialogTitle().setText(getContext().getString(R.string.customer_add_new_address));
        typeCustomer = CREATE_NEW_ADDRESS;
        ll_add_new_customer.setVisibility(GONE);
        mCustomerAddNewPanel.deleteBillingAddress();
        ll_new_billing_address.setVisibility(VISIBLE);
        ll_new_shipping_address.setVisibility(GONE);
        dialog.getButtonCancel().setText(getContext().getString(R.string.cancel));
    }

    public void showLoading(boolean isShow){
        cart_background_loading.setVisibility(isShow ? VISIBLE : GONE);
    }
}
