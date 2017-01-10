package com.magestore.app.lib.resourcemodel.catalog;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface ProductDataAccess extends DataAccess {
    List<Product> getProducts(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
    void getProducts(Product product);
    void updateProduct(Product product);
}
