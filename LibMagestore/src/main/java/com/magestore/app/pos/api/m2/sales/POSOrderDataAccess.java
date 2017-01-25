package com.magestore.app.pos.api.m2.sales;

import android.util.Log;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.sales.PosOrder;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListOrder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSOrderDataAccess extends POSAbstractDataAccess implements OrderDataAccess {
    private class OrderEntity {
        String email = null;

        OrderStatus statusHistory;

        Model entity;
    }

    /**
     * Trả về list order
     *
     * @param pageSize    Số customer trên 1 page
     * @param currentPage Trang hiện lại
     * @return Danh sách order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Order> getOrders(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Gson2PosListOrder listOrder = (Gson2PosListOrder) doAPI(Gson2PosListOrder.class,
                POSAPI.REST_ORDER_GET_LISTING,
                null,
                POSAPI.PARAM_CURRENT_PAGE, "" + currentPage,
                POSAPI.PARAM_PAGE_SIZE, "" + pageSize,
                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID);
        List<Order> list = (List<Order>) (List<?>) (listOrder.items);
        return list;
    }

    /**
     * Trả về boolean thành công hay không
     *
     * @param email
     * @return boolean thành công hay không
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public String sendEmail(String email, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_EMAIL);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderId);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                    .setParam(POSAPI.PARAM_ORDER_ID, orderId);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.email = email;

            rp = statement.execute(orderEntity);

            return rp.readResult2String();
        } catch (Exception e) {
            throw new DataAccessException(e);
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

    /**
     * Trả về Order
     *
     * @param orderStatus
     * @param orderId
     * @return Order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Order insertOrderStatus(OrderStatus orderStatus, String orderId) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_COMMENTS);
            statement.setParam(POSAPI.PARAM_ORDER_ID, orderId);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                    .setParam(POSAPI.PARAM_ORDER_ID, orderId);


            OrderEntity orderEntity = new OrderEntity();
            orderEntity.statusHistory = orderStatus;

            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosOrder.class);
            return (Order) rp.doParse();
        } catch (Exception e) {
            throw new DataAccessException(e);
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

    /**
     * Trả về Order
     *
     * @param shipmentParams
     * @return Order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Order createShipment(OrderShipmentParams shipmentParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_SHIPMENT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.entity = shipmentParams;

            // TODO: log params request


            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosOrder.class);

            return (Order) rp.doParse();
        } catch (Exception e) {
            throw new DataAccessException(e);
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
    public Order orderRefund(OrderRefundParams refundParams) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ORDER_SHIPMENT);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            OrderEntity orderEntity = new OrderEntity();
            orderEntity.entity = refundParams;

            Gson gson = new Gson();
            String json = gson.toJson(orderEntity);
            Log.e("JSON", json.toString());

            rp = statement.execute(orderEntity);
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosOrder.class);
            return (Order) rp.doParse();
        } catch (Exception e) {
            throw new DataAccessException(e);
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
}
