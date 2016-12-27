package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.Order;
import com.magestore.app.lib.entity.OrderItem;
import com.magestore.app.lib.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class PosOrder extends AbstractEntity implements Order {
    private String webpos_staff_id;
    private String webpos_staff_name;
    private String entnty_name;

    private List<PosOrderItem> items;

    @Override
    public Order newInstance() {
        return new PosOrder();
    }

    @Override
    public List<OrderItem> getOrderItems() {
        return (List<OrderItem>)(List<?>) items;

    }

    @Override
    public List<OrderItem> newOrderItems() {
        items = new ArrayList<PosOrderItem>();
        return (List<OrderItem>)(List<?>) items;
    }
}
