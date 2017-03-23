package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutAddCustomerController;

/**
 * Created by Johan on 2/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddCustomerPanel extends AbstractDetailPanel<Checkout> {
    CustomerListPanel mCustomerListPanel;
    CheckoutAddCustomerController mCustomerListController;
    MagestoreContext mMagestoreContext;
    CustomerService mCustomerService;
    CheckoutListPanel mCheckoutListPanel;

    public CheckoutAddCustomerPanel(Context context) {
        super(context);
    }

    public CheckoutAddCustomerPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutAddCustomerPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMagestoreContext(MagestoreContext mMagestoreContext) {
        this.mMagestoreContext = mMagestoreContext;
    }

    public void setCustomerService(CustomerService mCustomerService) {
        this.mCustomerService = mCustomerService;
    }

    public void setCheckoutListPanel(CheckoutListPanel mCheckoutListPanel) {
        this.mCheckoutListPanel = mCheckoutListPanel;
    }

    public CheckoutAddCustomerController getCustomerListController() {
        return mCustomerListController;
    }

    @Override
    protected void initLayout() {
        View v = inflate(getContext(), R.layout.panel_checkout_add_customer, null);
        addView(v);

        mCustomerListPanel = (CustomerListPanel) v.findViewById(R.id.sales_customer_items);
        FloatingActionButton fab = (FloatingActionButton) mCustomerListPanel.findViewById(R.id.fab);
        fab.setVisibility(GONE);
        AutoCompleteTextView text_search_customer = (AutoCompleteTextView) mCustomerListPanel.findViewById(R.id.text_search_customer);
        text_search_customer.setFocusable(true);
        initModel();
    }

    @Override
    public void bindItem(Checkout item) {
        if(item == null){return;}
        super.bindItem(item);
    }

    @Override
    public void initModel() {
        // Tạo list controller
        mCustomerListController = new CheckoutAddCustomerController();
        mCustomerListController.setMagestoreContext(mMagestoreContext);
        mCustomerListController.setCustomerService(mCustomerService);
        mCustomerListController.setListPanel(mCustomerListPanel);
        mCustomerListController.setCheckoutListPanel(mCheckoutListPanel);
        mCustomerListController.setCheckoutAddCustomerPanel(this);

        // chuẩn bị model cho các panel
        mCustomerListPanel.initModel();
    }

    @Override
    public void initValue() {
        mCustomerListController.doRetrieve();
    }

    public void updateCustomerToOrder(Customer customer) {
        mCheckoutListPanel.updateCustomerToOrder(customer);
    }
}
