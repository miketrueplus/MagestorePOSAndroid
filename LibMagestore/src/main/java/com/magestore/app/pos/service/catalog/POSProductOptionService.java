package com.magestore.app.pos.service.catalog;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/29/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSProductOptionService extends AbstractService implements ProductOptionService {

    @Override
    public ProductOption retrieve(Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();

        // load product option
        ProductOption productOption = productDataAccess.loadProductOption(product);
        product.setProductOption(productOption);

        // return
        return productOption;
    }

    @Override
    public List<Product> getAvailableQty(String Id) throws IOException, InstantiationException, ParseException, IllegalAccessException {

        // Khởi tạo data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();

        return productDataAccess.getAvailableQty(Id);
    }
}
