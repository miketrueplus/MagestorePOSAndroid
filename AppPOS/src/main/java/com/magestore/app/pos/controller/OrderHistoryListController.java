package com.magestore.app.pos.controller;

import android.content.Intent;
import android.os.Parcelable;
import android.widget.RelativeLayout;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderItemUpdateQtyParam;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.SalesActivity;
import com.magestore.app.pos.panel.OrderAddCommentPanel;
import com.magestore.app.pos.panel.OrderCancelPanel;
import com.magestore.app.pos.panel.OrderDetailPanel;
import com.magestore.app.pos.panel.OrderInvoicePanel;
import com.magestore.app.pos.panel.OrderAddPaymentPanel;
import com.magestore.app.pos.panel.OrderListChoosePaymentPanel;
import com.magestore.app.pos.panel.OrderListPanel;
import com.magestore.app.pos.panel.OrderRefundPanel;
import com.magestore.app.pos.panel.OrderSendEmailPanel;
import com.magestore.app.pos.panel.OrderShipmentPanel;
import com.magestore.app.pos.panel.OrderTakePaymentPanel;
import com.magestore.app.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryListController extends AbstractListController<Order> {
    String mSearchStatus;
    OrderSendEmailPanel mOrderSendEmailPanel;

    OrderAddCommentPanel mOrderAddCommentPanel;
    OrderCommentListController mOrderCommentListController;
    OrderHistoryItemsListController mOrderHistoryItemsListController;
    OrderPaymentListController mOrderPaymentListController;

    OrderShipmentPanel mOrderShipmentPanel;
    OrderRefundPanel mOrderRefundPanel;
    OrderInvoicePanel mOrderInvoicePanel;
    OrderCancelPanel mOrderCancelPanel;
    OrderTakePaymentPanel mOrderTakePaymentPanel;
    OrderAddPaymentPanel mOrderAddPaymentPanel;
    OrderListChoosePaymentPanel mOrderListChoosePaymentPanel;

    public static int SENT_EMAIL_TYPE = 1;
    public static String SENT_EMAIL_CODE = "send_email";
    public static int INSERT_STATUS_TYPE = 2;
    public static String INSERT_STATUS_CODE = "insert_status";
    public static int CREATE_SHIPMENT_TYPE = 3;
    public static String CREATE_SHIPMENT_CODE = "create_shipment";
    public static int ORDER_REFUND_TYPE = 4;
    public static String ORDER_REFUND_CODE = "order_refund";
    public static int ORDER_INVOICE_UPDATE_QTY_TYPE = 5;
    public static String ORDER_INVOICE_UPDATE_QTY_CODE = "order_invoice_update_qty";
    public static int ORDER_INVOICE_TYPE = 6;
    public static String ORDER_INVOICE_CODE = "order_invoice";
    public static int ORDER_CANCEL_TYPE = 7;
    public static String ORDER_CANCEL_CODE = "order_cancel";
    public static int ORDER_REORDER_TYPE = 8;
    public static String ORDER_REORDER_CODE = "order_reorder";
    public static int RETRIEVE_PAYMENT_METHOD_TYPE = 9;
    public static String RETRIEVE_PAYMENT_METHOD_CODE = "retrieve_payment_method";
    public static int ORDER_TAKE_PAYMENT_TYPE = 10;
    public static String ORDER_TAKE_PAYMENT_CODE = "order_take_payment";

    public static String SEND_ORDER_TO_SALE_ACTIVITY = "com.magestore.app.pos.controller.orderhistory.reorder";

    Map<String, Object> wraper;

    /**
     * Service xử lý các vấn đề liên quan đến order
     */
    OrderHistoryService mOrderService;
    RelativeLayout mLayoutOrderCommentLoading;

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

    public void setOrderTakePaymentPanel(OrderTakePaymentPanel mOrderTakePaymentPanel) {
        this.mOrderTakePaymentPanel = mOrderTakePaymentPanel;
    }

    public void setOrderAddPaymentPanel(OrderAddPaymentPanel mOrderAddPaymentPanel) {
        this.mOrderAddPaymentPanel = mOrderAddPaymentPanel;
    }

    public void setOrderListChoosePaymentPanel(OrderListChoosePaymentPanel mOrderListChoosePaymentPanel) {
        this.mOrderListChoosePaymentPanel = mOrderListChoosePaymentPanel;
    }

    @Override
    public void onRetrievePostExecute(List<Order> list) {
        super.onRetrievePostExecute(list);
        if (wraper == null) {
            wraper = new HashMap<>();
        }
    }

    public void doInputSendEmail(Map<String, Object> paramSendEmail) {
        showDetailOrderLoading(true);
        doAction(SENT_EMAIL_TYPE, SENT_EMAIL_CODE, paramSendEmail, null);
    }

    public void doInputCreateShipment(Order order) {
        showDetailOrderLoading(true);
        doAction(CREATE_SHIPMENT_TYPE, CREATE_SHIPMENT_CODE, wraper, order);
    }

    public void doInputInsertStatus(Order order) {
        showDetailOrderLoading(true);
        doAction(INSERT_STATUS_TYPE, INSERT_STATUS_CODE, wraper, order);
    }

    public void doInputRefund(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_REFUND_TYPE, ORDER_REFUND_CODE, wraper, order);
    }

    public void doInputInvoiceUpdateQty(OrderUpdateQtyParam orderUpdateQtyParam) {
        doAction(ORDER_INVOICE_UPDATE_QTY_TYPE, ORDER_INVOICE_UPDATE_QTY_CODE, wraper, orderUpdateQtyParam);
    }

    public void doInputInvoice(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_INVOICE_TYPE, ORDER_INVOICE_CODE, wraper, order);
    }

    public void doInputCancel(Order order) {
        showDetailOrderLoading(true);
        doAction(ORDER_CANCEL_TYPE, ORDER_CANCEL_CODE, wraper, order);
    }

    public void doInputReorder(Order order) {
        showDetailOrderLoading(true);
//        doAction(ORDER_REORDER_TYPE, ORDER_REORDER_CODE, wraper, order);
        // chuyển order sang cho sales activity để re-order
        SalesActivity.mOrder = order;
        Intent intent = new Intent();
        intent.setAction(SEND_ORDER_TO_SALE_ACTIVITY);
        getMagestoreContext().getActivity().sendBroadcast(intent);
        getMagestoreContext().getActivity().finish();
    }

    public void doRetrievePaymentMethod() {
        if (wraper == null) {
            wraper = new HashMap<>();
        }
        doAction(RETRIEVE_PAYMENT_METHOD_TYPE, RETRIEVE_PAYMENT_METHOD_CODE, wraper, null);
    }

    public void doInputTakePayment(Order order) {
        // Check payment khác null hay ko
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            doAction(ORDER_TAKE_PAYMENT_TYPE, ORDER_TAKE_PAYMENT_CODE, wraper, order);
            showDetailOrderLoading(true);
        } else {
            // show notifi
            mOrderTakePaymentPanel.showNotifiSelectPayment();
        }
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
        } else if (actionType == ORDER_INVOICE_UPDATE_QTY_TYPE) {
            wraper.put("invoice_update_qty_respone", mOrderService.orderInvoiceUpdateQty((OrderUpdateQtyParam) models[0]));
            return true;
        } else if (actionType == ORDER_INVOICE_TYPE) {
            wraper.put("invoice_respone", mOrderService.orderInvoice((Order) models[0]));
            return true;
        } else if (actionType == ORDER_CANCEL_TYPE) {
            wraper.put("cancel_respone", mOrderService.orderCancel((Order) models[0]));
            return true;
        } else if (actionType == ORDER_REORDER_TYPE) {
            Order order = (Order) models[0];
            String Ids = getIdsItemInfoBuy(order);
            wraper.put("list_product", mOrderService.retrieveOrderItem(Ids));
            return true;
        } else if (actionType == RETRIEVE_PAYMENT_METHOD_TYPE) {
            wraper.put("list_payment", mOrderService.retrievePaymentMethod());
            return true;
        } else if (actionType == ORDER_TAKE_PAYMENT_TYPE) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
            wraper.put("take_payment_respone", mOrderService.orderTakePayment(((Order) models[0]), listCheckoutPayment));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onActionPostExecute(success, actionType, actionCode, wraper, models);
        if (actionType == SENT_EMAIL_TYPE) {
            mOrderSendEmailPanel.showAlertRespone(success);
            showDetailOrderLoading(false);
        } else if (success && actionType == CREATE_SHIPMENT_TYPE) {
            Order order = (Order) wraper.get("shipment_respone");
            mOrderShipmentPanel.showAlertRespone();
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            mView.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == INSERT_STATUS_TYPE) {
            Order order = (Order) wraper.get("status_respone");
            mOrderAddCommentPanel.showAlertRespone();
            mOrderCommentListController.doSelectOrder(order);
            mOrderCommentListController.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_REFUND_TYPE) {
            Order order = (Order) wraper.get("refund_respone");
            mOrderRefundPanel.showAlertRespone();
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            mView.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_INVOICE_UPDATE_QTY_TYPE) {
            Order order = (Order) wraper.get("invoice_update_qty_respone");
            mOrderInvoicePanel.bindTotal(order);
        } else if (success && actionType == ORDER_INVOICE_TYPE) {
            Order order = (Order) wraper.get("invoice_respone");
            mOrderInvoicePanel.showAlertRespone();
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_CANCEL_TYPE) {
            Order order = (Order) wraper.get("cancel_respone");
            mOrderCancelPanel.showAlertRespone();
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            ((OrderListPanel) mView).notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        } else if (success && actionType == ORDER_REORDER_TYPE) {
            List<Product> listProduct = (List<Product>) wraper.get("list_product");
            Order order = (Order) models[0];
            order.setListProductReorder(listProduct);
            // chuyển order sang cho sales activity để re-order
            SalesActivity.mOrder = order;
            Intent intent = new Intent();
            intent.setAction(SEND_ORDER_TO_SALE_ACTIVITY);
            getMagestoreContext().getActivity().sendBroadcast(intent);
            getMagestoreContext().getActivity().finish();
        } else if (success && actionType == ORDER_TAKE_PAYMENT_TYPE) {
            Order order = (Order) wraper.get("take_payment_respone");
            mOrderHistoryItemsListController.doSelectOrder(order);
            mOrderCommentListController.doSelectOrder(order);
            mOrderPaymentListController.doSelectOrder(order);
            mOrderHistoryItemsListController.notifyDataSetChanged();
            mOrderCommentListController.notifyDataSetChanged();
            mOrderPaymentListController.notifyDataSetChanged();
            setNewOrderToList(((Order) models[0]), order);
            mView.notifyDataSetChanged();
            ((OrderDetailPanel) mDetailView).changeColorStatusOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).changeStatusTopOrder(order.getStatus());
            ((OrderDetailPanel) mDetailView).bindDataRespone(order);
            ((OrderDetailPanel) mDetailView).setOrder(order);
            showDetailOrderLoading(false);
        }
    }

    /**
     * cập nhật lại order trong list
     *
     * @param oldOrder
     * @param newOrder
     */
    private void setNewOrderToList(Order oldOrder, Order newOrder) {
        int index = mList.indexOf(oldOrder);
        mList.remove(index);
        mList.add(index, newOrder);
        bindList(mList);
    }

    /**
     * set data cho list choose payment
     */
    public void bindDataListChoosePayment() {
        List<CheckoutPayment> list_payment = (List<CheckoutPayment>) wraper.get("list_payment");
        mOrderAddPaymentPanel.bindList(list_payment);
    }

    // khi thay đổi value từng payment update giá trị money
    public void updateMoneyTotal(boolean type, float totalPrice) {
        mOrderTakePaymentPanel.updateMoneyTotal(type, totalPrice);
    }

    /**
     * add thêm payment trong vào checkout
     *
     * @param method
     */
    public void onAddPaymentMethod(CheckoutPayment method) {
        Order mOrder = ((OrderDetailPanel) mDetailView).getOrder();
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        checkIsPayLater(method, listPayment);
        float total = 0;
        if (method.isPaylater().equals("1")) {
            if (mOrder.getRemainMoney() > 0) {
                isEnableButtonAddPayment(true);
            } else {
                isEnableButtonAddPayment(false);
            }
        } else {
            if (mOrder.getRemainMoney() > 0) {
                total = mOrder.getRemainMoney();
                isEnableButtonAddPayment(true);
            } else {
                total = mOrder.getTotalDue();
                isEnableButtonAddPayment(false);
            }
        }

        method.setAmount(total);
        method.setBaseAmount(total);
        method.setRealAmount(total);
        method.setBaseRealAmount(total);

        if (listPayment == null) {
            listPayment = new ArrayList<>();
        }
        listPayment.add(method);
        wraper.put("list_choose_payment", listPayment);
        mOrderListChoosePaymentPanel.bindList(listPayment);
        mOrderListChoosePaymentPanel.updateTotal(listPayment);
        mOrderTakePaymentPanel.showPanelListChoosePayment();
        mOrderTakePaymentPanel.hideCheckPaymenrRequired();
    }

    /**
     * xóa 1 payment method  checkout
     */
    public void onRemovePaymentMethod() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        Order mOrder = ((OrderDetailPanel) mDetailView).getOrder();
        if (listPayment.size() == 0) {
            mOrderTakePaymentPanel.showPanelAddPaymentMethod();
            mOrderTakePaymentPanel.bindTotalPrice(mOrder.getTotalDue());
            isEnableButtonAddPayment(false);
        }
    }

    /**
     * kiểm tra nếu payment truyền vào ko phải pay later thì remove all payment is_pay_later
     *
     * @param checkoutPayment
     * @param listPayment
     */
    public void checkIsPayLater(CheckoutPayment checkoutPayment, List<CheckoutPayment> listPayment) {
        if (!checkoutPayment.isPaylater().equals("1")) {
            if (listPayment != null && listPayment.size() > 0) {
                for (int i = 0; i < listPayment.size(); i++) {
                    CheckoutPayment payment = listPayment.get(i);
                    if (payment.isPaylater().equals("1")) {
                        listPayment.remove(payment);
                        i--;
                    }
                }
            }
        }
    }

    // lấy toàn bị id item để lấy full thông tin product
    private String getIdsItemInfoBuy(Order order) {
//        List<CartItem> listItems = order.getItemsInfoBuy().getListItems();
        String Ids = "";
//        for (CartItem item : listItems) {
//            if (!item.getID().equals("custom_item")) {
//                Ids = item.getID() + ",";
//            }
//        }
        return Ids;
    }

    /* Felix 3/4/2017 Start*/
    public boolean checkDimissDialogTakePayment(Order order) {
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_choose_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            return true;
        }

        return false;
    }
    /* Felix 3/4/2017 End*/

    /**
     * ẩn hoặc hiện button add payment
     *
     * @param enable
     */
    public void isEnableButtonAddPayment(boolean enable) {
        mOrderTakePaymentPanel.isEnableButtonAddPayment(enable);
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

    public OrderUpdateQtyParam createOrderUpdateQtyParam() {
        return mOrderService.createOrderUpdateQtyParam();
    }

    public OrderItemUpdateQtyParam creaOrderItemUpdateQtyParam() {
        return mOrderService.createOrderItemUpdateQtyParam();
    }

    /*Felix 3/4/2017 Start*/
    public void showDetailOrderLoading(boolean visible) {
        ((OrderDetailPanel) mDetailView).showDetailOrderLoading(visible);
    }
    /*Felix 3/4/2017 End*/

    public boolean checkCanInvoice(Order order) {
        return mOrderService.checkCanInvoice(order);
    }

    public boolean checkCanTakePayment(Order order) {
        return mOrderService.checkCanTakePayment(order);
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

    public float checkShippingRefund(Order order) {
        return mOrderService.checkShippingRefund(order);
    }

    public boolean checkCanRefundGiftcard(Order order) {
        return mOrderService.checkCanRefundGiftcard(order);
    }

    public boolean checkCanStoreCredit(Order order) {
        return mOrderService.checkCanStoreCredit(order);
    }

    /**
     * Thực hiện search theo status
     * @param searchStatus
     */
    public void doSearchStatus(String searchStatus) {
        mSearchStatus = searchStatus;
        reload();
    }

    /**
     * Hướng tìm kiếm theo status
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    protected List<Order> callRetrieveService(int page, int pageSize) throws Exception {
        if (StringUtil.isNullOrEmpty(mSearchStatus) || !(getListService() instanceof OrderHistoryService))
            return super.callRetrieveService(page, pageSize);
        else {
            OrderHistoryService service = (OrderHistoryService) getListService();
            if (StringUtil.isNullOrEmpty(getSearchString()))
                return service.retrieve(page, pageSize, mSearchStatus);
            else
                return service.retrieve(getSearchString(), page, pageSize, mSearchStatus);
        }
    }
}
