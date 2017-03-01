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
    public ProductOption create(Product product) {
        return null;
    }

    @Override
    public int count(Product product) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public ProductOption retrieve(Product product, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<ProductOption> retrieve(Product product, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<ProductOption> retrieve(Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ProductDataAccess productDataAccess = factory.generateProductDataAccess();

        // load product option
        List<ProductOption> productOptionList = productDataAccess.loadProductOption(product);
        product.setProductOption(productOptionList);

        // return
        return productOptionList;
    }

    @Override
    public List<ProductOption> retrieve(Product product, String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public boolean update(Product product, ProductOption oldModel, ProductOption newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(Product product, ProductOption... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean delete(Product product, ProductOption... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }
}
