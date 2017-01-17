package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.service.order.OrderHistoryService;

import java.util.List;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderPaymentListController extends AbstractListController<OrderWebposPayment> implements ListController<OrderWebposPayment> {
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
        setParentController(mOrderHistoryListController);
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
    protected List<OrderWebposPayment> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 order cho payment
     *
     * @param order
     */
    public void doSelectOrder(Order order) {
        mSelectedOrder = order;
        mList = order.getWebposOrderPayments();
        mView.bindList(mList);
    }

}
