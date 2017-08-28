package com.magestore.app.lib.model;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.pos.model.magento.PosModelFactory;

/**
 * Khởi tạo model factory để tạo ra các model
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class ModelFactory {
    private MagestoreContext mContext;

    public abstract User generateUser();
    public abstract Product generateProduct();
    public abstract Customer generateCustomer();
    public abstract CustomerAddress generateCustomerAddress();

    public static ModelFactory getFactory(MagestoreContext context) throws IllegalAccessException, InstantiationException {
        ModelFactory modelFactory = (ModelFactory) PosModelFactory.class.newInstance();
        modelFactory.mContext = context;
        return modelFactory;
    }
}
