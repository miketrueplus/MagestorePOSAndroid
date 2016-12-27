package com.magestore.app.lib.usecase;

import com.magestore.app.lib.entity.Product;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/20/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface ProductUseCase extends UseCase {
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

    List<Product> retrieveProductList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;
}
