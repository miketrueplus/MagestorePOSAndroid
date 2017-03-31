package com.magestore.app.lib.resourcemodel.sales;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.ListDataAccess;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface OrderDataAccess extends DataAccess, ListDataAccess<Order> {
    String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order orderRefund(OrderRefundParams refundParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order orderInvoice(OrderInvoiceParams invoiceParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    Order orderCancel(OrderCommentParams cancelParams, String orderID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    List<CheckoutPayment> retrievePaymentMethod() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
