package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.DataOrder;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataOrder extends PosAbstractModel implements DataOrder {
    List<PosOrder> items;
    int total_count;

    @Override
    public List<Order> getItems() {
        return (List<Order>) (List<?>) items;
    }

    @Override
    public int getTotalCount() {
        return total_count;
    }
}
