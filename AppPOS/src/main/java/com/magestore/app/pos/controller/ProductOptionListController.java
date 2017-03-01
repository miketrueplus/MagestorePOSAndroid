package com.magestore.app.pos.controller;

import android.widget.Toast;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.observ.State;

/**
 * Created by Mike on 3/1/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionListController extends AbstractChildListController<Product, ProductOption> {
    /**
     * Hiển thị dialog show chọn option
     * @param state
     */
    public void doShowProductOptionInput(State state) {
        Product product = (Product) state.getTag(CartItemListController.STATE_ON_SHOW_PRODUCT_OPTION);
        doShowProductOptionInput(product);
    }

    /**
     * Hiển thị dialog show chọn option
     * @param product
     */
    public void doShowProductOptionInput(Product product) {
        bindParent(product);
        doRetrieve();
    }
}
