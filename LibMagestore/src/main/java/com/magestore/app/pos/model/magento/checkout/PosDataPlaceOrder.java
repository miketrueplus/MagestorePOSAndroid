package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.DataPlaceOrder;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.magento.sales.PosOrder;

/**
 * Created by Johan on 8/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataPlaceOrder extends PosAbstractModel implements DataPlaceOrder{
    PosOrder data;

    @Override
    public Order getOrder() {
        return data;
    }
}
