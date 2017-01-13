package com.magestore.app.pos.api.m2;

import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.pos.api.m2.catalog.POSProductDataAccess;
import com.magestore.app.pos.api.m2.config.POSConfigDataAccess;
import com.magestore.app.pos.api.m2.customer.POSCustomerDataAccess;
import com.magestore.app.pos.api.m2.sales.POSOrderDataAccess;
import com.magestore.app.pos.api.m2.user.POSUserDataAccess;

/**
 * Khởi tạo các gateway factory
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSDataAccessFactory extends DataAccessFactory {

    @Override
    public ProductDataAccess generateProductDataAccess() {
        return new POSProductDataAccess();
    }

    @Override
    public UserDataAccess generateUserDataAccess() {
        return new POSUserDataAccess();
    }

    @Override
    public OrderDataAccess generateOrderDataAccess() {
        return new POSOrderDataAccess();
    }

    @Override
    public CustomerDataAccess generateCustomerDataAccess() {
        return new POSCustomerDataAccess();
    }

    @Override
    public ConfigDataAccess generateConfigDataAccess() {
        return new POSConfigDataAccess();
    }
}