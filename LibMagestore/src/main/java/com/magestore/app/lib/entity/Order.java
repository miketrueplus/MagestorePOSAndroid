package com.magestore.app.lib.entity;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Order extends Entity {
    Order newInstance();
    List<OrderItem> newOrderItems();
    List<OrderItem> getOrderItems();
}
