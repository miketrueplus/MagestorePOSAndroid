package com.magestore.app.pos.controller;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.CheckoutAddCustomerPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Johan on 2/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddCustomerController extends CustomerListController {
    CheckoutAddCustomerPanel mCheckoutAddCustomerPanel;
    CheckoutListPanel mCheckoutListPanel;

    public void setCheckoutListPanel(CheckoutListPanel mCheckoutListPanel) {
        this.mCheckoutListPanel = mCheckoutListPanel;
    }

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
            mList.add(0, customer);
            bindList(mList);
            mCheckoutAddCustomerPanel.notifiDataListCustomer();
            mCheckoutAddCustomerPanel.scrollToTop();
        }
        if (mCheckoutListPanel != null) {
            mCheckoutListPanel.showToastMessage(0);
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

    @Override
    public void onUpdatePostExecute(Boolean success, Customer oldModel, Customer newModels) {
        super.onUpdatePostExecute(success, oldModel, newModels);
        if (mCheckoutListPanel != null) {
            mCheckoutListPanel.updateCustomerToOrder(newModels);
            mCheckoutListPanel.showToastMessage(1);
        }
    }
}
