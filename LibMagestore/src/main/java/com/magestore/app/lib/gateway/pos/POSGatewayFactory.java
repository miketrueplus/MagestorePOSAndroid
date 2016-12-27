package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.gateway.CustomerGateway;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.OrderGateway;
import com.magestore.app.lib.gateway.ProductGateway;
import com.magestore.app.lib.gateway.UserGateway;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSGatewayFactory extends GatewayFactory {

    @Override
    public ProductGateway generateProductGateway() {
        return new POSProductGateway();
    }

    @Override
    public UserGateway generateUserGateway() {
        return new POSUserGateway();
    }

    @Override
    public OrderGateway generateOrderGateway() {
        return null;
    }

    @Override
    public CustomerGateway generateCustomerGateway() {
        return new POSCustomerGateway();
    }
}