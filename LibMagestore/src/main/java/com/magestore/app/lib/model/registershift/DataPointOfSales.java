package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 9/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataPointOfSales extends Model {
    List<PointOfSales> getItems();

    int getTotalCount();
}
