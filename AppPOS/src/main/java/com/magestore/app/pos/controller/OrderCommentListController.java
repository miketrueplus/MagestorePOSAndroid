package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.service.order.OrderHistoryService;

import java.util.List;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderCommentListController extends AbstractListController<OrderStatus> implements ListController<OrderStatus> {
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
    protected List<OrderStatus> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho comment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = order.getOrderStatus();
        mView.bindList(mList);
    }

    public void notifyDataSetChanged() {
        mView.notifyDataSetChanged();
    }
}
