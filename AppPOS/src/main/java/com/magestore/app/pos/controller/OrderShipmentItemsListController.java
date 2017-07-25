package com.magestore.app.pos.controller;

import android.util.Log;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderShipmentItemsListController extends AbstractListController<CartItem> implements ListController<CartItem> {
    Order mSelectedOrder;
    OrderHistoryListController mOrderHistoryListController;
    OrderHistoryService mOrderService;
    List<CartItem> listCartItems;

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

    /**
     * Thiết lập 1 order cho comment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = checkListShipment(order);
        bindList(mList);
        mView.bindList(mList);
    }

    private List<CartItem> checkListShipment(Order order) {
        List<CartItem> listItem = order.getOrderItems();
        List<CartItem> nListItem = new ArrayList<>();

        for (CartItem item : listItem) {
            if (item.QtyShip() > 0 && item.getOrderParentItem() == null && !checkProductVirtual(item)) {
                nListItem.add(item);
            }
        }

        return nListItem;
    }

    private boolean checkProductVirtual(CartItem item) {
        if (StringUtil.isNullOrEmpty(item.getIsVirtual()) || item.getIsVirtual().equals("0")) {
            return false;
        }
        return true;
    }

    public OrderItemParams createOrderShipmentItemParams() {
        return mOrderService.createOrderItemParams();
    }

    public List<CartItem> getListItem() {
        return mList;
    }
}
