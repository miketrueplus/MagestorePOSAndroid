package com.magestore.app.pos.task;

import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.lib.service.ServiceFactory;

import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class LoadCustomerTask extends AsyncTaskAbstractTask<Void, Void, List<Customer>> {
    public LoadCustomerTask(ControllerListener listener) {
        super(listener);
    }

    /**
     * Thực hiện quá trình đăng nhập
     * @param params
     * @return
     */
    @Override
    protected List<Customer> doInBackground(Void... params) {
        try {
            ServiceFactory serviceFactory = ServiceFactory.getFactory(null);
            CustomerService customerService = serviceFactory.generateCustomerService();
            List<Customer> listCustomer = customerService.retrieveCustomerList(30);
            return listCustomer;
        } catch (Exception e) {
            // ngừng thực hiện tiến trình
            cancel(e, true);
            return null;
        }
    }
}
