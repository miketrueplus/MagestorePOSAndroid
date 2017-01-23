package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.order.OrderHistoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderShipmentItemsListController extends AbstractListController<Items> implements ListController<Items> {
    Order mSelectedOrder;
    OrderHistoryListController mOrderHistoryListController;

    /**
     * Thiết lập controller
     *
     * @param mOrderHistoryListController
     */
    public void setOrderHistoryListController(OrderHistoryListController mOrderHistoryListController) {
        this.mOrderHistoryListController = mOrderHistoryListController;
        setParentController(mOrderHistoryListController);
    }

    @Override
    protected List<Items> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho comment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = checkListShipment(order);
        mView.bindList(mList);
    }

    private List<Items> checkListShipment(Order order) {
        List<Items> listItem = order.getOrderItems();
        List<Items> nListItem = new ArrayList<>();

        for (Items item : listItem) {
            if (item.QtyShip() > 0) {
                nListItem.add(item);
            }
        }

        return nListItem;
    }
}
