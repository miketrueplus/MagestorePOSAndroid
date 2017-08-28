package com.magestore.app.pos.model.odoo;

import com.magestore.app.lib.model.ModelFactory;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.user.User;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosModelFactoryOdoo extends ModelFactory {
    @Override
    public User generateUser() {
        return null;
    }

    @Override
    public Product generateProduct() {
        return null;
    }

    @Override
    public Customer generateCustomer() {
        return null;
    }

    @Override
    public CustomerAddress generateCustomerAddress() {
        return null;
    }
}
