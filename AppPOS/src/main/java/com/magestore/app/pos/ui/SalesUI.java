package com.magestore.app.pos.ui;

import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.ui.PosUI;

import java.util.List;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface SalesUI extends PosUI {
    void setProductList(List<Product> listProduct);
    List<Product> getProductList();
    void loadListProductImage();
}
