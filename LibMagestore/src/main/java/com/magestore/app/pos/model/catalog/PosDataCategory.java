package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.lib.model.catalog.DataCategory;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataCategory extends PosAbstractModel implements DataCategory{
    List<PosCategory> items;
    int total_count;

    @Override
    public List<Category> getItems() {
        return (List<Category>) (List<?>)items;
    }

    @Override
    public int getTotal() {
        return total_count;
    }
}
