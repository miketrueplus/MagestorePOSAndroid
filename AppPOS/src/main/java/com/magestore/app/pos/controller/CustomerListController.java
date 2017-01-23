package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.model.customer.PosComplain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
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
     * @return
     * @throws Exception
     */
    @Override
    protected List<Customer> loadDataBackground(Void... params) throws Exception {
        List<Customer> listCustomer = mCustomerService.retrieveCustomerList(30);
        return listCustomer;
    }

    @Override
    public void bindItem(Customer item) {

//        List<Complain> complains = mCustomerService.retrieveComplain(item.getID());
        List<Complain> complains = new ArrayList<Complain>();
        complains.add(new PosComplain());
        complains.get(0).setContent("123");
        complains.add(new PosComplain());
        complains.get(1).setContent("456");
        item.setComplain(complains);

        super.bindItem(item);
    }
}