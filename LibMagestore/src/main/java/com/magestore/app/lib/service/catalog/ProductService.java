package com.magestore.app.lib.service.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/20/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface ProductService extends Service {
    /**
     * Đặt biến tham chiếu product để thực hiện các nghiệp vụ liên quan product
     * @param product
     */
    void setProduct(Product product);

    /**
     * Trả tham chiếu đến product đang xử lý nghiệp vụ
     * @return
     */
    Product getProduct();

    /**
     * Lấy danh mục product
     * @param size
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    List<Product> retrieveProductList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Load ảnh bitmap
     * @param product
     * @return
     */
    public Bitmap retrieveBitmap(Product product);
}
