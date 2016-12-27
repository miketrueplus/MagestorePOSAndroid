package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.ControllerListener;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.usecase.CustomerUseCase;
import com.magestore.app.lib.usecase.ProductUseCase;
import com.magestore.app.lib.usecase.UseCaseFactory;

import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class LoadCustomerController extends AsyncTaskAbstractController<Void, Void, List<Customer>> {
    public LoadCustomerController(ControllerListener listener) {
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
            CustomerUseCase customerUseCase = UseCaseFactory.generateCustomerUseCase(null, null);
            List<Customer> listCustomer = customerUseCase.retrieveCustomerList(30);
            return listCustomer;
        } catch (Exception e) {
            // ngừng thực hiện tiến trình
            cancel(e, true);
            return null;
        }
    }
}
