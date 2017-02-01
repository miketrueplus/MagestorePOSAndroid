package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.panel.OrderAddCommentPanel;
import com.magestore.app.pos.panel.OrderRefundPanel;
import com.magestore.app.pos.panel.OrderSendEmailPanel;
import com.magestore.app.pos.panel.OrderShipmentPanel;

import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryListController extends AbstractListController<Order> {
    OrderSendEmailPanel mOrderSendEmailPanel;

    OrderAddCommentPanel mOrderAddCommentPanel;
    OrderCommentListController mOrderCommentListController;
    OrderHistoryItemsListController mOrderHistoryItemsListController;

    OrderShipmentPanel mOrderShipmentPanel;
    OrderRefundPanel mOrderRefundPanel;

    public static int SENT_EMAIL_TYPE = 1;
    public static String SENT_EMAIL_CODE = "send_email";
    public static int INSERT_STATUS_TYPE = 2;
    public static String INSERT_STATUS_CODE = "insert_status";
    public static int CREATE_SHIPMENT_TYPE = 3;
    public static String CREATE_SHIPMENT_CODE = "create_shipment";
    public static int ORDER_REFUND_TYPE = 4;
    public static String ORDER_REFUND_CODE = "order_refund";

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

    public void setOrderSendEmailPanel(OrderSendEmailPanel mOrderSendEmailPanel) {
        this.mOrderSendEmailPanel = mOrderSendEmailPanel;
    }

    public void setOrderAddCommentPanel(OrderAddCommentPanel mOrderAddCommentPanel) {
        this.mOrderAddCommentPanel = mOrderAddCommentPanel;
    }

    public void setOrderCommentListController(OrderCommentListController mOrderCommentListController) {
        this.mOrderCommentListController = mOrderCommentListController;
    }

    public void setOrderHistoryItemsListController(OrderHistoryItemsListController mOrderHistoryItemsListController) {
        this.mOrderHistoryItemsListController = mOrderHistoryItemsListController;
    }

    public void setOrderShipmentPanel(OrderShipmentPanel mOrderShipmentPanel) {
        this.mOrderShipmentPanel = mOrderShipmentPanel;
    }

    public void setOrderRefundPanel(OrderRefundPanel mOrderRefundPanel) {
        this.mOrderRefundPanel = mOrderRefundPanel;
    }

    @Override
    protected List<Order> loadDataBackground(Void... params) throws Exception {
        // TODO: test lấy webpos_payments
        List<Order> listOrder = mOrderService.retrieveOrderList(50);
//        List<Order> listOrder = mOrderService.retrieveOrderList(30);
        return listOrder;
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == SENT_EMAIL_TYPE) {
            String email = (String) wraper.get("email");
            String orderId = (String) wraper.get("order_id");
            return Boolean.parseBoolean(mOrderService.sendEmail(email, orderId).trim());
        } else if (actionType == CREATE_SHIPMENT_TYPE) {
            Order order = mOrderService.createShipment((Order) models[0]);
            if (order != null) {
                return true;
            }
        } else if (actionType == INSERT_STATUS_TYPE) {
            Order order = mOrderService.insertOrderStatus((Order) models[0]);
            if (order != null) {
                return true;
            }
        } else if (actionType == ORDER_REFUND_TYPE) {
            Order order = mOrderService.orderRefund((Order) models[0]);
            if (order != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onActionPostExecute(success, actionType, actionCode, wraper, models);
        if (actionType == SENT_EMAIL_TYPE) {
            mOrderSendEmailPanel.showAlertRespone(success);
        } else if (actionType == CREATE_SHIPMENT_TYPE) {
            if (success) {
                Order order = (Order) models[0];
                mOrderShipmentPanel.showAlertRespone();
                mOrderHistoryItemsListController.doSelectOrder(order);
                mOrderCommentListController.doSelectOrder(order);
            }
        } else if (actionType == INSERT_STATUS_TYPE) {
            if (success) {
                Order order = (Order) models[0];
                mOrderAddCommentPanel.showAlertRespone();
                mOrderCommentListController.doSelectOrder(order);
            }
        } else if (actionType == ORDER_REFUND_TYPE) {
            if (success) {
                Order order = (Order) models[0];
                mOrderRefundPanel.showAlertRespone();
                mOrderHistoryItemsListController.doSelectOrder(order);
                mOrderCommentListController.doSelectOrder(order);
            }
        }
    }

    public OrderStatus createOrderStatus() {
        return mOrderService.createOrderStatus();
    }

    public OrderShipmentParams createOrderShipmentParams() {
        return mOrderService.createOrderShipmentParams();
    }

    public OrderShipmentTrackParams createOrderShipmentTrackParams() {
        return mOrderService.createOrderShipmentTrackParams();
    }

    public List<OrderShipmentTrackParams> createListTrack() {
        return mOrderService.createListTrack();
    }

    public OrderCommentParams createCommentParams() {
        return mOrderService.createCommentParams();
    }

    public List<OrderCommentParams> createListComment() {
        return mOrderService.createListComment();
    }

    public OrderRefundParams createOrderRefundParams() {
        return mOrderService.createOrderRefundParams();
    }
}
