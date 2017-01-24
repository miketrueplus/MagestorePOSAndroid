package com.magestore.app.pos.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.panel.OrderAddCommentPanel;
import com.magestore.app.pos.panel.OrderSendEmailPanel;

import java.io.IOException;
import java.text.ParseException;
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

    public static int CREATE_SHIPMENT_TYPE = 3;
    public static String CREATE_SHIPMENT_CODE = "create_shipment";

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

    @Override
    protected List<Order> loadDataBackground(Void... params) throws Exception {
        // TODO: test lấy webpos_payments
        List<Order> listOrder = mOrderService.retrieveOrderList(50);
//        List<Order> listOrder = mOrderService.retrieveOrderList(30);
        return listOrder;
    }

    public void sendEmail(final String email, final String orderId) {
        final AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    return mOrderService.sendEmail(email, orderId);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mOrderSendEmailPanel.showAlertRespone(s);
            }
        };

        // chạy task load data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else // Below Api Level 13
            task.execute();
    }

    public void insertOrderStatus(final Order order) {
        final AsyncTask<Void, Void, Order> task = new AsyncTask<Void, Void, Order>() {
            @Override
            protected Order doInBackground(Void... voids) {
                try {
                    return mOrderService.insertOrderStatus(order);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Order order) {
                super.onPostExecute(order);
                if (order != null) {
                    mOrderAddCommentPanel.showAlertRespone();
                    mOrderCommentListController.doSelectOrder(order);
                }
            }
        };

        // chạy task load data
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) // Above Api Level 13
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else // Below Api Level 13
            task.execute();
    }


    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == CREATE_SHIPMENT_TYPE) {
            Order order = mOrderService.createShipment((Order) models[0]);
            if (order != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        super.onActionPostExecute(success, actionType, actionCode, wraper, models);

        if (actionType == CREATE_SHIPMENT_TYPE) {
            if (success) {
                Order order = (Order) models[0];
                mOrderHistoryItemsListController.doSelectOrder(order);
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
}
