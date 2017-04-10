package com.magestore.app.pos.api.m2;

import com.magestore.app.lib.resourcemodel.catalog.CategoryDataAccess;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerComplainDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.plugins.PluginsDataAccess;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.pos.api.m2.catalog.POSCategoryDataAccess;
import com.magestore.app.pos.api.m2.catalog.POSProductDataAccess;
import com.magestore.app.pos.api.m2.config.POSConfigDataAccess;
import com.magestore.app.pos.api.m2.customer.POSCustomerAddressDataAccess;
import com.magestore.app.pos.api.m2.customer.POSCustomerComplainDataAccess;
import com.magestore.app.pos.api.m2.customer.POSCustomerDataAccess;
import com.magestore.app.pos.api.m2.plugins.POSPluginsDataAccess;
import com.magestore.app.pos.api.m2.registershift.POSRegisterShiftDataAccess;
import com.magestore.app.pos.api.m2.sales.POSCartDataAccess;
import com.magestore.app.pos.api.m2.sales.POSCheckoutDataAccess;
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
    public CustomerAddressDataAccess generateCustomerAddressDataAccess() {
        return new POSCustomerAddressDataAccess();
    }

    @Override
    public CustomerComplainDataAccess generateCustomerComplainDataAccess() {
        return new POSCustomerComplainDataAccess();
    }

    @Override
    public CategoryDataAccess generateCategoryDataAccess() {
        return new POSCategoryDataAccess();
    }

    @Override
    public CartDataAccess generateCartDataAccess() {
        return new POSCartDataAccess();
    }

    @Override
    public RegisterShiftDataAccess generateRegisterShiftDataAccess() {
        return new POSRegisterShiftDataAccess();
    }

    @Override
    public ConfigDataAccess generateConfigDataAccess() {
        return new POSConfigDataAccess();
    }

    @Override
    public PluginsDataAccess generatePluginsDataAccess() {
        return new POSPluginsDataAccess();
    }

    @Override
    public CheckoutDataAccess generateCheckoutDataAccess() {
        return new POSCheckoutDataAccess();
    }
}