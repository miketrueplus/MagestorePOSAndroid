package com.magestore.app.pos.service;

import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.service.catalog.POSProductService;
import com.magestore.app.pos.service.config.POSConfigService;
import com.magestore.app.pos.service.customer.POSCustomerService;
import com.magestore.app.pos.service.sales.POSOrderService;
import com.magestore.app.pos.service.user.POSUserService;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSServiceFactory extends ServiceFactory {
    public UserService generateUserService() {
        UserService useCase = new POSUserService();
        useCase.setContext(getContext());
        return useCase;
    }

    public ProductService generateProductService() {
        ProductService useCase = new POSProductService();
        useCase.setContext(getContext());
        return useCase;
    }

    public CartService generateOrderService() {
        CartService useCase = new POSOrderService();
        useCase.setContext(getContext());
        return useCase;
    }

    public CustomerService generateCustomerService() {
        CustomerService useCase = new POSCustomerService();
        useCase.setContext(getContext());
        return useCase;
    }

    public ConfigService generateConfigService() {
        ConfigService useCase = new POSConfigService();
        useCase.setContext(getContext());
        return useCase;
    }
}
