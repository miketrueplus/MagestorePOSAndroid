package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderShippingAssignments;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShippingAssignments extends PosAbstractModel implements OrderShippingAssignments {
    List<PosOrderShipping> shipping;

    @Override
    public List<PosOrderShipping> getShipping() {
        return (List<PosOrderShipping>)(List<?>) shipping;
    }
}
