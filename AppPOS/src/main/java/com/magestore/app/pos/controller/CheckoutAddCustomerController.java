package com.magestore.app.pos.controller;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.panel.CheckoutAddCustomerPanel;

/**
 * Created by Johan on 2/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddCustomerController extends CustomerListController {
    CheckoutAddCustomerPanel mCheckoutAddCustomerPanel;

    public void setCheckoutAddCustomerPanel(CheckoutAddCustomerPanel mCheckoutAddCustomerPanel) {
        this.mCheckoutAddCustomerPanel = mCheckoutAddCustomerPanel;
    }

    @Override
    public void bindItem(Customer item) {
        mCheckoutAddCustomerPanel.dismissDialogShowCustomer(item);
    }
}
