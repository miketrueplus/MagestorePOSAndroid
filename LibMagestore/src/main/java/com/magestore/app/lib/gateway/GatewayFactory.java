package com.magestore.app.lib.gateway;

import android.content.Context;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;

/**
 * Factory tạo các object API đến magestore pos server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class GatewayFactory {
    private MagestoreContext mContext;

    public abstract ProductGateway generateProductGateway();
    public abstract UserGateway generateUserGateway();
    public abstract OrderGateway generateOrderGateway();
    public abstract CustomerGateway generateCustomerGateway();
    public abstract ConfigGateway generateConfigGateway();

    public MagestoreContext getContext() {
        return mContext;
    }

    /**
     * Khởi tạo Gateway factory để kết nối, gọi các API đến GatewayFactory
     * @param
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static GatewayFactory getFactory(MagestoreContext context) throws IllegalAccessException, InstantiationException {
        GatewayFactory gatewayFactory = (GatewayFactory) POSGatewayFactory.class.newInstance();
        gatewayFactory.mContext = context;
        return gatewayFactory;
    }
}
