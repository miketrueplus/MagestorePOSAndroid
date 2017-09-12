package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 9/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataOrder extends Model {
    List<Order> getItems();
    int getTotalCount();
}
