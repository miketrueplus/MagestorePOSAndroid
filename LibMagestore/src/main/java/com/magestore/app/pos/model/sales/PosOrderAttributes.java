package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderAttributes;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderAttributes extends PosAbstractModel implements OrderAttributes {
    List<PosOrderShippingAssignments> shipping_assignments;

    @Override
    public List<PosOrderShippingAssignments> getShippingAssignments() {
        return shipping_assignments;
    }
}
