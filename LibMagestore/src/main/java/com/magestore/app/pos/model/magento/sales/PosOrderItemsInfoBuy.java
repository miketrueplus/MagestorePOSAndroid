package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderCartItem;
import com.magestore.app.lib.model.sales.OrderItemsInfoBuy;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 4/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderItemsInfoBuy extends PosAbstractModel implements OrderItemsInfoBuy {
    List<PosOrderCartItem> items;

    @Override
    public List<OrderCartItem> getListOrderCartItems() {
        return (List<OrderCartItem>) (List<?>) items;
    }
}
