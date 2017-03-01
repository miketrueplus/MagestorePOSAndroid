package com.magestore.app.lib.service.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/20/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ProductService extends Service, ListService<Product> {
    List<Product> retrieve(String categoryId, String searchString, int pageSize, int currentPage) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Load ảnh bitmap
     * @param product
     * @return
     */
    public Bitmap retrieveBitmap(Product product, int sizeWidth, int sizeHeight) throws InstantiationException, IllegalAccessException, IOException;
}
