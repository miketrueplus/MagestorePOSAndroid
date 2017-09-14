package com.magestore.app.pos.model.registershift;

import com.magestore.app.lib.model.registershift.DataPointOfSales;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataPointOfSales extends PosAbstractModel implements DataPointOfSales {
    private List<PosPointOfSales> items;
    private int total_count;

    @Override
    public List<PointOfSales> getItems() {
        return (List<PointOfSales>) (List<?>) items;
    }

    @Override
    public int getTotalCount() {
        return total_count;
    }
}
