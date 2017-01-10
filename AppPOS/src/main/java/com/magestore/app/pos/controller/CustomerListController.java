package com.magestore.app.pos.controller;

import android.view.View;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.panel.CustomerListPanel;
import com.magestore.app.pos.ui.AbstractActivity;

import java.util.List;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class CustomerListController extends AbstractPosListController<Customer> {
    /**
     * Service xử lý các vấn đề liên quan đến customer
     */
    CustomerService mCustomerService;

    /**
     * Thiết lập service
     * @param service
     */
    public void setCustomerService(CustomerService service) {
        mCustomerService = service;
    }

    /**
     * Thực hiện load danh sách khách hàng
//     * @param params
     * @return
     * @throws Exception
     */
    @Override
    protected List<Customer> loadDataBackground(Void... params) throws Exception {
        List<Customer> listCustomer = mCustomerService.retrieveCustomerList(30);
        return listCustomer;
    }
}