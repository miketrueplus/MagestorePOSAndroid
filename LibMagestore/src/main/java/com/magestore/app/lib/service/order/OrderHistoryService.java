package com.magestore.app.lib.service.order;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderHistoryService extends Service {
    List<Order> retrieveOrderList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<Order> retrieveOrderLastMonth(Customer customer) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    String sendEmail(String email, String orderId) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Order insertOrderStatus(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Order createShipment(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Order orderRefund(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    OrderStatus createOrderStatus();

    OrderShipmentParams createOrderShipmentParams();

    OrderShipmentTrackParams createOrderShipmentTrackParams();

    List<OrderShipmentTrackParams> createListTrack();

    OrderCommentParams createCommentParams();

    List<OrderCommentParams> createListComment();

    OrderItemParams createOrderItemParams();

    OrderRefundParams createOrderRefundParams();
}
