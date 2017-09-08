package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 9/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataProduct extends Model {
    List<Product> getItems();
    int getTotalCount();
}
