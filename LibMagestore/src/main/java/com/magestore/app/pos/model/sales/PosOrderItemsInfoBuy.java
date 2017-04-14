package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.OrderItemsInfoBuy;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;

import java.util.List;

/**
 * Created by Johan on 4/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderItemsInfoBuy extends PosAbstractModel implements OrderItemsInfoBuy {
    List<PosCartItem> items;

    @Override
    public List<CartItem> getListItems() {
        return (List<CartItem>) (List<?>) items;
    }
}
