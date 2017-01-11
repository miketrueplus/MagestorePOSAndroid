package com.magestore.app.pos.model;

import com.magestore.app.lib.model.ModelFactory;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.pos.model.catalog.PosProduct;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.user.PosUser;

/**
 * Khởi tạo các model
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class PosModelFactory extends ModelFactory {

    @Override
    public User generateUser() {
        return new PosUser();
    }

    @Override
    public Product generateProduct() {
        return new PosProduct();
    }

    @Override
    public Customer generateCustomer() {
        return new PosCustomer();
    }

    @Override
    public CustomerAddress generateCustomerAddress() {
        return new PosCustomerAddress();
    }
}
