package com.magestore.app.pos.api.m1;

import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.CategoryDataAccess;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerComplainDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.resourcemodel.plugins.PluginsDataAccess;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m1.catalog.POSCategoryDataAccessM1;
import com.magestore.app.pos.api.m1.catalog.POSProductDataAccessM1;
import com.magestore.app.pos.api.m1.config.POSConfigDataAccessM1;
import com.magestore.app.pos.api.m1.customer.POSCustomerAddressDataAccessM1;
import com.magestore.app.pos.api.m1.customer.POSCustomerDataAccessM1;
import com.magestore.app.pos.api.m1.plugins.POSPluginsDataAccessM1;
import com.magestore.app.pos.api.m1.sales.POSCartDataAccessM1;
import com.magestore.app.pos.api.m1.sales.POSCheckoutDataAccessM1;
import com.magestore.app.pos.api.m1.sales.POSOrderDataAccessM1;
import com.magestore.app.pos.api.m1.user.POSUserDataAccessM1;

/**
 * Created by Johan on 8/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSDataAccessFactoryM1 extends DataAccessFactory {
    @Override
    public ProductDataAccess generateProductDataAccess() {
        return new POSProductDataAccessM1();
    }

    @Override
    public UserDataAccess generateUserDataAccess() {
        return new POSUserDataAccessM1();
    }

    @Override
    public OrderDataAccess generateOrderDataAccess() {
        return new POSOrderDataAccessM1();
    }

    @Override
    public CustomerDataAccess generateCustomerDataAccess() {
        return new POSCustomerDataAccessM1();
    }

    @Override
    public CustomerAddressDataAccess generateCustomerAddressDataAccess() {
        return new POSCustomerAddressDataAccessM1();
    }

    @Override
    public CustomerComplainDataAccess generateCustomerComplainDataAccess() {
        return null;
    }

    @Override
    public CategoryDataAccess generateCategoryDataAccess() {
        return new POSCategoryDataAccessM1();
    }

    @Override
    public CartDataAccess generateCartDataAccess() {
        return new POSCartDataAccessM1();
    }

    @Override
    public RegisterShiftDataAccess generateRegisterShiftDataAccess() {
        return null;
    }

    @Override
    public ConfigDataAccess generateConfigDataAccess() {
        return new POSConfigDataAccessM1();
    }

    @Override
    public PluginsDataAccess generatePluginsDataAccess() {
        return new POSPluginsDataAccessM1();
    }

    @Override
    public CheckoutDataAccess generateCheckoutDataAccess() {
        return new POSCheckoutDataAccessM1();
    }
}
