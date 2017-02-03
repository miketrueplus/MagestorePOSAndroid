package com.magestore.app.pos.api.m2.customer;


import android.location.Address;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListAddress;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomer;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.parse.gson2pos.Gson2PostListComplain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Các API về customer
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerDataAccess
        extends POSAbstractDataAccess
        implements CustomerDataAccess {

    // wrap object lại và chuyênr thành json
    private class Wrap {Customer customer; Complain complain;};
    /**
     * đếm số customer
     * @return
     * @throws java.text.ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListCustomer.class);
            Gson2PosListCustomer listCustomer = (Gson2PosListCustomer) rp.doParse();

            // return
            return listCustomer.total_count;
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
        }    }

    @Override
    public List<Customer> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return retrieve(1, 100);
    }

    @Override
    public List<Customer> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListCustomer.class);
            Gson2PosListCustomer listCustomer = (Gson2PosListCustomer) rp.doParse();
            List<Customer> list = (List<Customer>) (List<?>) (listCustomer.items);

            // return
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

    /**
     * Trả về 1 customer theo customer ID
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Customer retrieve(String strID) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_GET_DETAIL);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, strID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
            //.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(PosCustomer.class);
            return (Customer) rp.doParse();
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
    public List<Customer> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return retrieve();
    }

    @Override
    public boolean update(Customer oldCustomer, final Customer newCustomer) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // gỡ tạm complain ra
        List<Complain> backupComplain = newCustomer.getComplain();
        newCustomer.setComplain(null);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_UPDATE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, oldCustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = newCustomer;
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            return true;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // khôi phục lại complain
            newCustomer.setComplain(backupComplain);

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
    public boolean insert(final Customer... customers) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_ADD);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = customers[0];
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            return true;
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
    public boolean delete(Customer... models) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_ADD);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, models[0].getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = models[0];
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            return true;
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
        }    }

    /**
     * Đếm toàn bộ số address có trong hệ thống
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    @Override
//    public int countAddress() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        Connection connection = null;
//        Statement statement = null;
//        ResultReading rp = null;
//        ParamBuilder paramBuilder = null;
//
//        try {
//            // Khởi tạo connection và khởi tạo truy vấn
//            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
//            statement = connection.createStatement();
//            statement.prepareQuery(POSAPI.REST_ADDRESS_GET_LISTING);
//
//            // Xây dựng tham số
//            paramBuilder = statement.getParamBuilder()
//                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
//                    .setPage(1)
//                    .setPageSize(1);
//
//            // thực thi truy vấn và parse kết quả thành object
//            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
//            rp.setParseModel(Gson2PosListAddress.class);
//            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
//
//            // return
//            return listAddress.total_count;
//        } catch (ConnectionException ex) {
//            throw ex;
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            // đóng result reading
//            if (rp != null) rp.close();
//            rp = null;
//
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;
//
//            // đóng statement
//            if (statement != null) statement.close();
//            statement = null;
//
//            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
//        }
//    }

    /**
     * Trả về tất cả address trong hệ thống
     * @param pageSize
     * @param currentPage
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    @Override
//    public List<CustomerAddress> retrieveCustomerAddress(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        Connection connection = null;
//        Statement statement = null;
//        ResultReading rp = null;
//        ParamBuilder paramBuilder = null;
//
//        try {
//            // Khởi tạo connection và khởi tạo truy vấn
//            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
//            statement = connection.createStatement();
//            statement.prepareQuery(POSAPI.REST_ADDRESS_GET_LISTING);
//
//            // Xây dựng tham số
//            paramBuilder = statement.getParamBuilder()
//                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
//                    .setPage(currentPage)
//                    .setPageSize(pageSize);
//
//            // thực thi truy vấn và parse kết quả thành object
//            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
//            rp.setParseModel(Gson2PosListAddress.class);
//            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
//            List<CustomerAddress> list = (List<CustomerAddress>) (List<?>) (listAddress.items);
//
//            // return
//            return list;
//        } catch (ConnectionException ex) {
//            throw ex;
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            // đóng result reading
//            if (rp != null) rp.close();
//            rp = null;
//
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;
//
//            // đóng statement
//            if (statement != null) statement.close();
//            statement = null;
//
//            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
//        }
//    }

    /**
     * Trả về address của 1 customer
     *
     * @param customerID
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    @Override
//    public List<CustomerAddress> retrieveCustomerAddress(String customerID)
//            throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        Connection connection = null;
//        Statement statement = null;
//        ResultReading rp = null;
//        ParamBuilder paramBuilder = null;
//
//        try {
//            // Khởi tạo connection và khởi tạo truy vấn
//            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
//            statement = connection.createStatement();
//            statement.prepareQuery(POSAPI.REST_CUSOMTER_ADDRESS_GET_LISTING);
//            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);
//
//            // Xây dựng tham số
//            paramBuilder = statement.getParamBuilder()
//                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);
//
//            // thực thi truy vấn và parse kết quả thành object
//            rp = statement.execute();
//            rp.setParseImplement(getClassParseImplement());
//            rp.setParseModel(Gson2PosListAddress.class);
//            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
//            List<CustomerAddress> list = (List<CustomerAddress>) (List<?>) (listAddress.items);
//
//            // return
//            return list;
//        } catch (ConnectionException ex) {
//            throw ex;
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            // đóng result reading
//            if (rp != null) rp.close();
//            rp = null;
//
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;
//
//            // đóng statement
//            if (statement != null) statement.close();
//            statement = null;
//
//            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
//        }
//    }

    /**
     * Trả về address của 1 customer
     *
     * @param customer
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    @Override
//    public List<CustomerAddress> retrieveCustomerAddress(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        return retrieveCustomerAddress(customer.getID());
//    }

    /**
     * Trả về Cập nhật địa chỉ của 1 customer
     *
     * @param pcustomer
     * @param address
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    @Override
//    public boolean updateCustomerAddress(final Customer pcustomer, CustomerAddress oldCustomerAddress, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
//        Connection connection = null;
//        Statement statement = null;
//        ResultReading rp = null;
//        ParamBuilder paramBuilder = null;
//
//        // Lưu address hiện tại tạm vào
//        if (pcustomer.getAddress() == null) return false;
//        if (!pcustomer.getAddress().contains(oldCustomerAddress)) return false;
//        int indexAddress = pcustomer.getAddress().indexOf(oldCustomerAddress);
//        pcustomer.getAddress().set(indexAddress, address);
//
//        // gỡ tạm complain ra
//        List<Complain> backupComplain = pcustomer.getComplain();
//        pcustomer.setComplain(null);
//
//        try {
//            // Khởi tạo connection và khởi tạo truy vấn
//            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
//            statement = connection.createStatement();
//            statement.prepareQuery(POSAPI.REST_ADDRESS_UPDATE);
//            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());
//
//            // Xây dựng tham số
//            paramBuilder = statement.getParamBuilder()
//                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
//                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());
//
//            // thực thi truy vấn và parse kết quả thành object
//            Wrap wrapCustomer = new Wrap();
//            wrapCustomer.customer = pcustomer;
//            rp = statement.execute(wrapCustomer);
//            String result = rp.readResult2String();
//            return true;
//        } catch (ConnectionException ex) {
//            pcustomer.getAddress().remove(address);
//            throw ex;
//        } catch (IOException ex) {
//            pcustomer.getAddress().remove(address);
//            throw ex;
//        } finally {
//            // cập nhật trả lại address
//            pcustomer.getAddress().set(indexAddress, oldCustomerAddress);
//            // khôi phục lại complain
//            pcustomer.setComplain(backupComplain);
//
//            // đóng result reading
//            if (rp != null) rp.close();
//            rp = null;
//
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;
//
//            // đóng statement
//            if (statement != null) statement.close();
//            statement = null;
//
//            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
//        }
//    }
}
