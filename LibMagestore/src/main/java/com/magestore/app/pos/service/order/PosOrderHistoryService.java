package com.magestore.app.pos.service.order;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.model.sales.PosOrderStatus;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
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
    public Order insertOrderStatus(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException{
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
    public OrderStatus createOrderStatus() {
        PosOrderStatus orderStatus = new PosOrderStatus();
        return orderStatus;
    }
}
