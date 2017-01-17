package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.order.OrderHistoryService;

import java.util.List;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryListController extends AbstractListController<Order> {
    /**
     * Service xử lý các vấn đề liên quan đến order
     */
    OrderHistoryService mOrderService;

    /**
     * Thiết lập service
     *
     * @param mOrderService
     */
    public void setOrderService(OrderHistoryService mOrderService) {
        this.mOrderService = mOrderService;
    }

    /**
     * Trả lại order service
     *
     * @return
     */
    public OrderHistoryService getOrderService() {
        return mOrderService;
    }

    @Override
    protected List<Order> loadDataBackground(Void... params) throws Exception {
        // TODO: test lấy webpos_payments
        List<Order> listOrder = mOrderService.retrieveOrderList(50);
//        List<Order> listOrder = mOrderService.retrieveOrderList(30);
        return listOrder;
    }

}
