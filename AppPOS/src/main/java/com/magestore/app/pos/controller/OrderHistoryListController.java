package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.panel.OrderAddCommentPanel;
import com.magestore.app.pos.panel.OrderCancelPanel;
import com.magestore.app.pos.panel.OrderDetailPanel;
import com.magestore.app.pos.panel.OrderInvoicePanel;
import com.magestore.app.pos.panel.OrderRefundPanel;
import com.magestore.app.pos.panel.OrderSendEmailPanel;
import com.magestore.app.pos.panel.OrderShipmentPanel;

import java.util.HashMap;
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
    OrderPaymentListController mOrderPaymentListController;

    OrderShipmentPanel mOrderShipmentPanel;
    OrderRefundPanel mOrderRefundPanel;
    OrderInvoicePanel mOrderInvoicePanel;
    OrderCancelPanel mOrderCancelPanel;

    public static int SENT_EMAIL_TYPE = 1;
    public static String SENT_EMAIL_CODE = "send_email";
    public static int INSERT_STATUS_TYPE = 2;
    public static String INSERT_STATUS_CODE = "insert_status";
    public static int CREATE_SHIPMENT_TYPE = 3;
    public static String CREATE_SHIPMENT_CODE = "create_shipment";
    public static int ORDER_REFUND_TYPE = 4;
    public static String ORDER_REFUND_CODE = "order_refund";
    public static int ORDER_INVOICE_TYPE = 5;
    public static String ORDER_INVOICE_CODE = "order_invoice";
    public static int ORDER_CANCEL_TYPE = 6;
    public static String ORDER_CANCEL_CODE = "order_cancel";

    Map<String, Object> wraper;

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
        setListService(mOrderService);
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

    public OrderCommentListController getOrderCommentListController() {
        return mOrderCommentListController;
    }

    public OrderHistoryItemsListController getOrderHistoryItemsListController() {
        return mOrderHistoryItemsListController;
    }

    public void setOrderHistoryItemsListController(OrderHistoryItemsListController mOrderHistoryItemsListController) {
        this.mOrderHistoryItemsListController = mOrderHistoryItemsListController;
    }

    public void setOrderPaymentListController(OrderPaymentListController mOrderPaymentListController) {
        this.mOrderPaymentListController = mOrderPaymentListController;
    }

    public OrderPaymentListController getOrderPaymentListController() {
        return mOrderPaymentListController;
    }

    public void setOrderShipmentPanel(OrderShipmentPanel mOrderShipmentPanel) {
        this.mOrderShipmentPanel = mOrderShipmentPanel;
    }

    public void setOrderRefundPanel(OrderRefundPanel mOrderRefundPanel) {
        this.mOrderRefundPanel = mOrderRefundPanel;
    }

    public void setOrderInvoicePanel(OrderInvoicePanel mOrderInvoicePanel) {
        this.mOrderInvoicePanel = mOrderInvoicePanel;
    }

    public void setOrderCancelPanel(OrderCancelPanel mOrderCancelPanel) {
        this.mOrderCancelPanel = mOrderCancelPanel;
    }

    @Override
    public void onRetrievePostExecute(List<Order> list) {
        super.onRetrievePostExecute(list);
        wraper = new HashMap<>();
    }

    public void doInputSendEmail(Map<String, Object> paramSendEmail) {
        doAction(SENT_EMAIL_TYPE, SENT_EMAIL_CODE, paramSendEmail, null);
    }

    public void doInputCreateShipment(Order order) {
        doAction(CREATE_SHIPMENT_TYPE, CREATE_SHIPMENT_CODE, wraper, order);
    }

    public void doInputInsertStatus(Order order) {
        doAction(INSERT_STATUS_TYPE, INSERT_STATUS_CODE, wraper, order);
    }

    public void doInputRefund(Order order) {
        doAction(ORDER_REFUND_TYPE, ORDER_REFUND_CODE, wraper, order);
    }

    public void doInputInvoice(Order order) {
        doAction(ORDER_INVOICE_TYPE, ORDER_INVOICE_CODE, wraper, order);
    }

    public void doInputCancel(Order order) {
        doAction(ORDER_CANCEL_TYPE, ORDER_CANCEL_CODE, wraper, order);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == SENT_EMAIL_TYPE) {
            String email = (String) wraper.get("email");
            String orderId = (String) wraper.get("order_id");
            return Boolean.parseBoolean(mOrderService.sendEmail(email, orderId).trim());
        } else if (actionType == CREATE_SHIPMENT_TYPE) {
            wraper.put("shipment_respone", mOrderService.createShipment((Order) models[0]));
            return true;
        } else if (actionType == INSERT_STATUS_TYPE) {
            wraper.put("status_respone", mOrderService.insertOrderStatus((Order) models[0]));
            return true;
        } else if (actionType == ORDER_REFUND_TYPE) {
            wraper.put("refund_respone", mOrderService.orderRefund((Order) models[0]));
            return true;
        } else if (actionType == ORDER_INVOICE_TYPE) {
            wraper.put("invoice_respone", mOrderService.orderInvoice((Order) models[0]));
            return true;
        } else if (actionType == ORDER_CANCEL_TYPE) {
            wraper.put("cancel_respone", mOrderService.orderCancel((Order) models[0]));
            return true;
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
                Order order = (Order) wraper.get("shipment_respone");
                mOrderShipmentPanel.showAlertRespone();
                mOrderHistoryItemsListController.doSelectOrder(order);
                mOrderCommentListController.doSelectOrder(order);
                mOrderHistoryItemsListController.notifyDataSetChanged();
                mOrderCommentListController.notifyDataSetChanged();
                mList.set(mList.indexOf(((Order) models[0])), order);
                mView.notifyDataSetChanged();
                ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).bindDataRespone(order);
                ((OrderDetailPanel) mDetailView).setOrder(order);
            }
        } else if (actionType == INSERT_STATUS_TYPE) {
            if (success) {
                Order order = (Order) wraper.get("status_respone");
                mOrderAddCommentPanel.showAlertRespone();
                mOrderCommentListController.doSelectOrder(order);
                mOrderCommentListController.notifyDataSetChanged();
                ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            }
        } else if (actionType == ORDER_REFUND_TYPE) {
            if (success) {
                Order order = (Order) wraper.get("refund_respone");
                mOrderRefundPanel.showAlertRespone();
                mOrderHistoryItemsListController.doSelectOrder(order);
                mOrderCommentListController.doSelectOrder(order);
                mOrderHistoryItemsListController.notifyDataSetChanged();
                mOrderCommentListController.notifyDataSetChanged();
                mList.set(mList.indexOf(((Order) models[0])), order);
                mView.notifyDataSetChanged();
                ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).bindDataRespone(order);
                ((OrderDetailPanel) mDetailView).setOrder(order);
            }
        } else if (actionType == ORDER_INVOICE_TYPE) {
            if (success) {
                Order order = (Order) wraper.get("invoice_respone");
                mOrderInvoicePanel.showAlertRespone();
                mOrderHistoryItemsListController.doSelectOrder(order);
                mOrderCommentListController.doSelectOrder(order);
                mOrderHistoryItemsListController.notifyDataSetChanged();
                mOrderCommentListController.notifyDataSetChanged();
                mList.set(mList.indexOf(((Order) models[0])), order);
                mView.notifyDataSetChanged();
                ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).bindDataRespone(order);
                ((OrderDetailPanel) mDetailView).setOrder(order);
            }
        } else if (actionType == ORDER_CANCEL_TYPE) {
            if (success) {
                Order order = (Order) wraper.get("cancel_respone");
                mOrderCancelPanel.showAlertRespone();
                mOrderHistoryItemsListController.doSelectOrder(order);
                mOrderCommentListController.doSelectOrder(order);
                mOrderHistoryItemsListController.notifyDataSetChanged();
                mOrderCommentListController.notifyDataSetChanged();
                mList.set(mList.indexOf(((Order) models[0])), order);
                mView.notifyDataSetChanged();
                ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
                ((OrderDetailPanel) mDetailView).bindDataRespone(order);
                ((OrderDetailPanel) mDetailView).setOrder(order);
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

    public OrderInvoiceParams createOrderInvoiceParams() {
        return mOrderService.createOrderInvoiceParams();
    }

    public boolean checkCanInvoice(Order order) {
        return mOrderService.checkCanInvoice(order);
    }

    public boolean checkCanCancel(Order order) {
        return mOrderService.checkCanCancel(order);
    }

    public boolean checkCanRefund(Order order) {
        return mOrderService.checkCanRefund(order);
    }

    public boolean checkCanShip(Order order) {
        return mOrderService.checkCanShip(order);
    }
}
