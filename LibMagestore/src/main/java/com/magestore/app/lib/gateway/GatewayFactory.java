package com.magestore.app.lib.gateway;

import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;

/**
 * Factory tạo các object API đến magestore pos server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class GatewayFactory {
    public abstract ProductGateway generateProductGateway();
    public abstract UserGateway generateUserGateway();
    public abstract OrderGateway generateOrderGateway();
    public abstract CustomerGateway generateCustomerGateway();
    public abstract ConfigGateway generateConfigGateway();


    /**
     * Khởi tạo Gateway factory để kết nối, gọi các API đến GatewayFactory
     * @param cl
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static GatewayFactory getFactory(Class cl) throws IllegalAccessException, InstantiationException {
        return (GatewayFactory) cl.newInstance();
    }
}
