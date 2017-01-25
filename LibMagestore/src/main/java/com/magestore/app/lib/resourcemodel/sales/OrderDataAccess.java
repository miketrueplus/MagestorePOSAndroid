package com.magestore.app.lib.resourcemodel.sales;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface OrderDataAccess extends DataAccess {
    List<Order> getOrders(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
