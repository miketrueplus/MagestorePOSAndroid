package com.magestore.app.lib.gateway;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.parse.ParseEntity;
import com.magestore.app.lib.parse.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface ProductGateway extends Gateway {
    List<Product> getProducts(int pageSize, int currentPage) throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException;
    void getProducts(Product product);
    void updateProduct(Product product);
}
