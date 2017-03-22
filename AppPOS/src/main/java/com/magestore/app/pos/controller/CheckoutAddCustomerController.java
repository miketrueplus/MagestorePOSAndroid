package com.magestore.app.pos.controller;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.CheckoutAddCustomerPanel;

import java.io.IOException;
import java.text.ParseException;

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
        mCheckoutAddCustomerPanel.updateCustomerToOrder(addAddressDefaultToCustomer(item));
    }

    @Override
    public void onInsertPostExecute(Boolean success, Customer... models) {
        Customer customer = ((Customer) models[0]);
        customer.setAddressPosition(0);
        if (success && customer != null) {
            mCheckoutAddCustomerPanel.updateCustomerToOrder(addAddressDefaultToCustomer(customer));
        }
    }

    public Customer addAddressDefaultToCustomer(Customer item) {
        Customer newCustomer = item;
        Customer guest_customer = null;
        String userAddressDefault = getMagestoreContext().getActivity().getString(R.string.customer_default_address);
        try {
            guest_customer = mConfigService.getGuestCheckout();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mCustomerAddressService.addAddressDefaultToCustomer(newCustomer, guest_customer, userAddressDefault);
        return newCustomer;
    }
}
