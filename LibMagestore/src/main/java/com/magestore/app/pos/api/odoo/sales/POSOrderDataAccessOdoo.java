package com.magestore.app.pos.api.odoo.sales;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.sales.DataOrder;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundCreditParams;
import com.magestore.app.lib.model.sales.OrderRefundGiftCard;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderTakePaymentParam;
import com.magestore.app.lib.model.sales.OrderUpdateQtyParam;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.sales.PosDataOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListOrderParseModelOdoo;
import com.magestore.app.util.StringUtil;
import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 9/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSOrderDataAccessOdoo extends POSAbstractDataAccessOdoo implements OrderDataAccess {

    private class StatusEntity {
        String order_id;
        String note;
    }

    private class InvoiceEntity {
        String order_id;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            return listOrder.getTotalCount();
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Order> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Order> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("create_date");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Order> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("name", finalSearchString)
                    .setFilterOrLike("partner_id.name", finalSearchString)
                    .setFilterOrLike("partner_id.email", finalSearchString)
                    .setFilterOrLike("state", finalSearchString)
                    .setSortOrderDESC("create_date");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Order oldModel, Order newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Order... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Order... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public List<Order> retrieve(int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderDESC("create_date");

            if (!StringUtil.isNullOrEmpty(status))
                paramBuilder.setFilterIn("state", status);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public List<Order> retrieve(String searchString, int page, int pageSize, String status) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearchString = "%" + searchString + "%";
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("name", finalSearchString)
                    .setFilterOrLike("partner_id.name", finalSearchString)
                    .setFilterOrLike("partner_id.email", finalSearchString)
                    .setFilterOrLike("state", finalSearchString)
                    .setSortOrderDESC("create_date");

            if (!StringUtil.isNullOrEmpty(status))
                paramBuilder.setFilterIn("state", status);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_COMMENTS);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            StatusEntity statusEntity = new StatusEntity();
            statusEntity.order_id = orderId;
            statusEntity.note = orderStatus.getComment();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(statusEntity);
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list.get(0);
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public boolean orderRefundByCredit(OrderRefundCreditParams orderRefundCreditParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return false;
    }

    @Override
    public boolean orderRefundByGiftCard(Order order, OrderRefundGiftCard orderRefundGiftCard) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return false;
    }

    @Override
    public Order orderRefund(OrderRefundParams refundParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderInvoiceUpdateQty(OrderUpdateQtyParam orderUpdateQtyParam) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderInvoice(OrderInvoiceParams invoiceParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_ORDER_INVOICE);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            InvoiceEntity invoiceEntity = new InvoiceEntity();
            invoiceEntity.order_id = invoiceParams.getOrderId();

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(invoiceEntity);
            rp.setParseImplement(new Gson2PosListOrderParseModelOdoo());
            rp.setParseModel(PosDataOrder.class);
            DataOrder listOrder = (DataOrder) rp.doParse();
            List<Order> list = listOrder.getItems();
            return list.get(0);
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Order orderCancel(OrderCommentParams cancelParams, String orderID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public List<Product> retrieveOrderItem(String Ids) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public List<CheckoutPayment> retrievePaymentMethod() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }

    @Override
    public Order orderTakePayment(OrderTakePaymentParam orderTakePaymentParam, String orderID, Order order) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return null;
    }
}
