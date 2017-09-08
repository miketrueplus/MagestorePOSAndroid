package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.DataProduct;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataProduct extends PosAbstractModel implements DataProduct{
    List<Product> items;
    int total_count;

    @Override
    public List<Product> getItems() {
        return items;
    }

    @Override
    public int getTotalCount() {
        return total_count;
    }
}
