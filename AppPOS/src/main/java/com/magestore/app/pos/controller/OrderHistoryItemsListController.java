package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.order.OrderHistoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryItemsListController extends AbstractListController<CartItem> implements ListController<CartItem> {
    Order mSelectedOrder;
    OrderHistoryListController mOrderHistoryListController;
    OrderHistoryService mOrderService;

    /**
     * Thiết lập controller
     *
     * @param mOrderHistoryListController
     */
    public void setOrderHistoryListController(OrderHistoryListController mOrderHistoryListController) {
        this.mOrderHistoryListController = mOrderHistoryListController;
//        setParentController(mOrderHistoryListController);
    }

    /**
     * Thiết lập service
     *
     * @param mOrderService
     */
    public void setOrderService(OrderHistoryService mOrderService) {
        this.mOrderService = mOrderService;
    }

    @Override
    protected List<CartItem> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho comment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = checkParentItem(order);
        mView.bindList(mList);
    }

    private List<CartItem> checkParentItem(Order order) {
        List<CartItem> listCartItems = new ArrayList<>();
        for (CartItem cart : order.getOrderItems()) {
            if (cart.getOrderParentItem() == null) {
                listCartItems.add(cart);
            }
        }
        return listCartItems;
    }

    public void notifyDataSetChanged() {
        mView.notifyDataSetChanged();
    }
}
