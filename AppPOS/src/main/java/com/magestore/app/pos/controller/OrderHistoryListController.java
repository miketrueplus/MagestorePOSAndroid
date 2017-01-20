package com.magestore.app.pos.controller;

import android.os.AsyncTask;
import android.os.Build;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.panel.OrderSendEmailPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryListController extends AbstractListController<Order> {
    OrderSendEmailPanel mOrderSendEmailPanel;

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

    @Override
    protected List<Order> loadDataBackground(Void... params) throws Exception {
        // TODO: test lấy webpos_payments
        List<Order> listOrder = mOrderService.retrieveOrderList(50);
//        List<Order> listOrder = mOrderService.retrieveOrderList(30);
        return listOrder;
    }

    public void sendEmail(final String email, final String orderId){
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
}
