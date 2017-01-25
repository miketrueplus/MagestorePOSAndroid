package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.model.customer.PosComplain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerListController extends AbstractListController<Customer> {
    // đánh dấu actrion
    static final int ACTION_CODE_GET_COMPLAIN = 0;
    static final int ACTION_CODE_INPUT_COMPLAIN = 1;

    /**
     * Service xử lý các vấn đề liên quan đến customer
     */
    CustomerService mCustomerService;

    // input complain được input
    Complain mInputComplain;

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
        List<Customer> listCustomer = mCustomerService.retrieveCustomerList(1, 30);
        return listCustomer;
    }

    public void doLoadComplain(Customer customer) {
        doAction(ACTION_CODE_GET_COMPLAIN, null, null, customer);
    }

    public void doInputNewComplain(String newComplain) {
        Complain complain = mCustomerService.createComplain();
        complain.setContent(newComplain);
        doAction(ACTION_CODE_INPUT_COMPLAIN, null, null, mItem, complain);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wrapper, Model... models) throws Exception {
        if (actionType == ACTION_CODE_GET_COMPLAIN) {
            mCustomerService.retrieveComplain(((Customer) models[0]));
            return true;
        }
        if (actionType == ACTION_CODE_INPUT_COMPLAIN) {
            mCustomerService.insertComplain((Customer) models[0], (Complain) models[1]);
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wrapper, Model... models) {
        if (success && actionType == ACTION_CODE_GET_COMPLAIN) {
            super.bindItem((Customer) models[0]);
        }
        if (success && actionType == ACTION_CODE_INPUT_COMPLAIN) {
            super.bindItem((Customer) models[0]);
        }
    }

    @Override
    public void bindItem(Customer item) {
        super.bindItem(item);
        doAction(ACTION_CODE_GET_COMPLAIN, null, null, item);
    }
}