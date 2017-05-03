package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.CartItem;

import java.util.List;

/**
 * Created by Johan on 4/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderItemsInfoBuy extends Model {
    List<OrderCartItem> getListOrderCartItems();
}
