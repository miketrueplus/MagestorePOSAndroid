package com.magestore.app.pos.service.order;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.model.sales.PosOrderCommentParams;
import com.magestore.app.pos.model.sales.PosOrderItemParams;
import com.magestore.app.pos.model.sales.PosOrderRefundParams;
import com.magestore.app.pos.model.sales.PosOrderShipmentParams;
import com.magestore.app.pos.model.sales.PosOrderShipmentTrackParams;
import com.magestore.app.pos.model.sales.PosOrderStatus;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderHistoryService extends AbstractService implements OrderHistoryService {
    @Override
    public List<Order> retrieveOrderList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderGateway = factory.generateOrderDataAccess();
        // TODO: test order có webpos_payments
        return orderGateway.getOrders(size, 32);
//        return orderGateway.getOrders(size, 1);
    }

    @Override
    public List<Order> retrieveOrderLastMonth(Customer customer) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        return retrieveOrderList(3);
    }

    @Override
    public String sendEmail(String email, String orderId) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderGateway = factory.generateOrderDataAccess();
        return orderGateway.sendEmail(email, orderId);
    }

    @Override
    public Order insertOrderStatus(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        String orderId = order.getID();

        OrderStatus param = order.getParamStatus();
        OrderStatus orderStatus = createOrderStatus();
        orderStatus.setComment(param.getComment());
        orderStatus.setCreatedAt(param.getCreatedAt());
        orderStatus.setId(param.getID());
        orderStatus.setEntityName(param.getEntityName());
        orderStatus.setIsCustomerNotified(param.getIsCustomerNotified());
        orderStatus.setIsVisibleOnFront(param.getIsVisibleOnFront());
        orderStatus.setParentId(param.getParentId());
        orderStatus.setStatus(param.getStatus());
        orderStatus.setExtensionAttributes(param.getExtensionAttributes());

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderGateway = factory.generateOrderDataAccess();

        return orderGateway.insertOrderStatus(orderStatus, orderId);
    }

    @Override
    public Order createShipment(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        OrderShipmentParams shipmentParams = order.getParamShipment();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderGateway = factory.generateOrderDataAccess();

        return orderGateway.createShipment(shipmentParams);
    }

    @Override
    public Order orderRefund(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        OrderRefundParams refundParams = order.getParamRefund();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderGateway = factory.generateOrderDataAccess();

        return orderGateway.orderRefund(refundParams);
    }

    @Override
    public OrderStatus createOrderStatus() {
        PosOrderStatus orderStatus = new PosOrderStatus();
        return orderStatus;
    }

    @Override
    public OrderShipmentParams createOrderShipmentParams() {
        PosOrderShipmentParams orderShipmentParams = new PosOrderShipmentParams();
        return orderShipmentParams;
    }

    @Override
    public OrderShipmentTrackParams createOrderShipmentTrackParams() {
        PosOrderShipmentTrackParams orderShipmentTrackParams = new PosOrderShipmentTrackParams();
        return orderShipmentTrackParams;
    }

    @Override
    public List<OrderShipmentTrackParams> createListTrack() {
        List<OrderShipmentTrackParams> listTrack = new ArrayList<OrderShipmentTrackParams>();
        return listTrack;
    }

    @Override
    public OrderCommentParams createCommentParams() {
        PosOrderCommentParams orderCommentParams = new PosOrderCommentParams();
        return orderCommentParams;
    }

    @Override
    public List<OrderCommentParams> createListComment() {
        List<OrderCommentParams> listComment = new ArrayList<OrderCommentParams>();
        return listComment;
    }

    @Override
    public OrderItemParams createOrderItemParams() {
        OrderItemParams param = new PosOrderItemParams();
        return param;
    }

    @Override
    public OrderRefundParams createOrderRefundParams() {
        PosOrderRefundParams orderRefundParams = new PosOrderRefundParams();
        return orderRefundParams;
    }
}
