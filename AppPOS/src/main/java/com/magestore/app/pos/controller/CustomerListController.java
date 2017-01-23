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
    static final int ACTION_CODE_GET_COMPLAIN = 0;

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
        doAction(ACTION_CODE_GET_COMPLAIN, null, item);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Customer... models) throws Exception {
        List<Complain> complains = mCustomerService.retrieveComplain(models[0].getID());
        models[0].setComplain(complains);
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Customer... models) {
        super.bindItem(models[0]);
    }
}