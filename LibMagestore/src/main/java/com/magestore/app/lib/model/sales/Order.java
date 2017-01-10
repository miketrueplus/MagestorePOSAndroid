package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.Items;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Order extends Model {
    Order newInstance();
    List<Items> newOrderItems();
    List<Items> getOrderItems();
}
