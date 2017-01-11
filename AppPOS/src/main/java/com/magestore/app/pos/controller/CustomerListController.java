package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.customer.CustomerService;

import java.util.List;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class CustomerListController extends AbstractListController<Customer> {
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
     * Trả lại customer service
     * @return
     */
    public CustomerService getCustomerService() {
        return mCustomerService;
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