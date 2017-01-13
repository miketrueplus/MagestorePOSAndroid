package com.magestore.app.lib.service;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.lib.service.sales.OrderService;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.service.POSServiceFactory;

/**
 * Khởi tạo các instance của các use case
 * Created by Mike on 12/18/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class ServiceFactory {
    private MagestoreContext mContext;

    public abstract UserService generateUserService();
    public abstract ProductService generateProductService();
    public abstract CartService generateCartService();
    public abstract OrderService generateOrderService();
    public abstract OrderHistoryService generateOrderHistoryService();
    public abstract CustomerService generateCustomerService();
    public abstract ConfigService generateConfigService();

    /**
     * Trả lại context
     * @return
     */
    public MagestoreContext getContext() {
        return mContext;
    }

    /**
     * Khởi tạo DataAccess factory để kết nối, gọi các API đến DataAccessFactory
     * @param
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static ServiceFactory getFactory(MagestoreContext context) throws IllegalAccessException, InstantiationException {
        ServiceFactory serviceFactory = (ServiceFactory) POSServiceFactory.class.newInstance();
        serviceFactory.mContext = context;
        return serviceFactory;
    }
}
