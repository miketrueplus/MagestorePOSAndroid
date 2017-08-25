package com.magestore.app.pos.service.catalog;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.catalog.ProductDataAccess;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;

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
}
