package com.magestore.app.lib.resourcemodel;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.customer.CustomerAddress;
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
import com.magestore.app.pos.api.m1.POSDataAccessFactoryMO;
import com.magestore.app.pos.api.m2.POSDataAccessFactory;
import com.magestore.app.util.ConfigUtil;

/**
 * Factory tạo các object API đến magestore pos server
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class DataAccessFactory {
    private MagestoreContext mContext;

    public abstract ProductDataAccess generateProductDataAccess();
    public abstract UserDataAccess generateUserDataAccess();
    public abstract OrderDataAccess generateOrderDataAccess();

    // factory khởi tạo customer data access
    public abstract CustomerDataAccess generateCustomerDataAccess();
    public abstract CustomerAddressDataAccess generateCustomerAddressDataAccess();
    public abstract CustomerComplainDataAccess generateCustomerComplainDataAccess();
    public abstract CategoryDataAccess generateCategoryDataAccess();
    public abstract CartDataAccess generateCartDataAccess();
    public abstract RegisterShiftDataAccess generateRegisterShiftDataAccess();
    public abstract ConfigDataAccess generateConfigDataAccess();
    public abstract PluginsDataAccess generatePluginsDataAccess();
    public abstract CheckoutDataAccess generateCheckoutDataAccess();

    public MagestoreContext getContext() {
        return mContext;
    }

    /**
     * Khởi tạo DataAccess factory để kết nối, gọi các API đến DataAccessFactory
     * @param context ngữ cảnh xử lý
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static DataAccessFactory getFactory(MagestoreContext context) throws IllegalAccessException, InstantiationException {
        DataAccessFactory dataAccessFactory;
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2)) {
            dataAccessFactory = (DataAccessFactory) POSDataAccessFactory.class.newInstance();
        } else if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_1)) {
            dataAccessFactory = (DataAccessFactory) POSDataAccessFactoryMO.class.newInstance();
        } else {
            dataAccessFactory = (DataAccessFactory) POSDataAccessFactory.class.newInstance();
        }
        dataAccessFactory.mContext = context;
        return dataAccessFactory;
    }

    /**
     * Khởi tạo DataAccess factory để kết nối, gọi các API đến DataAccessFactory
     * @param context ngữ cảnh xử lý
     * @param gatewayFactoryClass class để sinh gateway
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static DataAccessFactory getFactory(MagestoreContext context, Class gatewayFactoryClass) throws IllegalAccessException, InstantiationException {
        DataAccessFactory dataAccessFactory = (DataAccessFactory) gatewayFactoryClass.newInstance();
        dataAccessFactory.mContext = context;
        return dataAccessFactory;
    }
}
