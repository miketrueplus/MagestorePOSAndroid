package com.magestore.app.pos.service.order;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.service.order.OrderHistoryService;
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
        String check = orderGateway.sendEmail(email, orderId);
        return check;
    }
}
