package com.magestore.app.pos.ui;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.view.ui.PosUI;

import java.util.List;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface SalesUI extends PosUI {
    void setProductList(List<Product> listProduct);
    List<Product> getProductList();
}
