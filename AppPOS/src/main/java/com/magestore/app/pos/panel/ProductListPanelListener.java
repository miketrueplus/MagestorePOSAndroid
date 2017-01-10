package com.magestore.app.pos.panel;

import com.magestore.app.lib.model.catalog.Product;

import java.util.List;

/**
 * Sự kiện tương tác với panel danh sách sản phẩm
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ProductListPanelListener {
    /**
     * Khi sản phẩm được chọn trên danh sách
     * @param product
     */
    void onSelectProduct(Product product);

    /**
     * Khi load sản phẩm thành công
     * @param productList
     */
    void onSuccessLoadProduct(List<Product> productList);
}
