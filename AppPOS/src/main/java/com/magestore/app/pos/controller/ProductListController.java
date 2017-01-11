package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.catalog.Product;

import java.util.List;

/**
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class ProductListController extends AbstractListController<Product> {
    @Override
    protected List<Product> loadDataBackground(Void... params) throws Exception {
        return null;
    }
}
