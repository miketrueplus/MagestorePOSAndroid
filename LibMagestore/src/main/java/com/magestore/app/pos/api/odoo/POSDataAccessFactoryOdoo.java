package com.magestore.app.pos.api.odoo;

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
import com.magestore.app.pos.api.odoo.catalog.POSCategoryDataAccessOdoo;
import com.magestore.app.pos.api.odoo.catalog.POSProductDataAccessOdoo;
import com.magestore.app.pos.api.odoo.config.POSConfigDataAccessOdoo;
import com.magestore.app.pos.api.odoo.sales.POSCheckoutDataAccessOdoo;
import com.magestore.app.pos.api.odoo.sales.POSOrderDataAccessOdoo;
import com.magestore.app.pos.api.odoo.user.POSUserDataAccessOdoo;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSDataAccessFactoryOdoo extends DataAccessFactory {
    @Override
    public ProductDataAccess generateProductDataAccess() {
        return new POSProductDataAccessOdoo();
    }

    @Override
    public UserDataAccess generateUserDataAccess() {
        return new POSUserDataAccessOdoo();
    }

    @Override
    public OrderDataAccess generateOrderDataAccess() {
        return new POSOrderDataAccessOdoo();
    }

    @Override
    public CustomerDataAccess generateCustomerDataAccess() {
        return null;
    }

    @Override
    public CustomerAddressDataAccess generateCustomerAddressDataAccess() {
        return null;
    }

    @Override
    public CustomerComplainDataAccess generateCustomerComplainDataAccess() {
        return null;
    }

    @Override
    public CategoryDataAccess generateCategoryDataAccess() {
        return new POSCategoryDataAccessOdoo();
    }

    @Override
    public CartDataAccess generateCartDataAccess() {
        return null;
    }

    @Override
    public RegisterShiftDataAccess generateRegisterShiftDataAccess() {
        return null;
    }

    @Override
    public ConfigDataAccess generateConfigDataAccess() {
        return new POSConfigDataAccessOdoo();
    }

    @Override
    public PluginsDataAccess generatePluginsDataAccess() {
        return null;
    }

    @Override
    public CheckoutDataAccess generateCheckoutDataAccess() {
        return new POSCheckoutDataAccessOdoo();
    }
}
