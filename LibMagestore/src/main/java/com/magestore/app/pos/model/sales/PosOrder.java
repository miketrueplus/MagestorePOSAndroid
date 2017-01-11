package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosItems;

import java.util.ArrayList;
import java.util.List;

/**
 * Quảng lý các thông tin của order
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosOrder extends PosAbstractModel implements Order {
    private String webpos_staff_id;
    private String webpos_staff_name;
    private String entnty_name;

    private List<PosItems> items;

    @Override
    public Order newInstance() {
        return new PosOrder();
    }

    @Override
    public List<Items> getOrderItems() {
        return (List<Items>)(List<?>) items;

    }

    @Override
    public List<Items> newOrderItems() {
        items = new ArrayList<PosItems>();
        return (List<Items>)(List<?>) items;
    }
}
