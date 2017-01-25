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

public class POSCustomerDataAccess extends POSAbstractDataAccess implements CustomerDataAccess {
    // wrap object lại và chuyênr thành json
    private class Wrap {Customer customer; Complain complain;};
    /**
     * Tổng số customer trong hệ thống
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public int countCustomer() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
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
        }
    }

    /**
     * Trả về list customer
     *
     * @param pageSize    Số customer trên 1 page
     * @param currentPage Trang hiện lại
     * @return Danh sách customer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Customer> retrieveCustomers(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
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
                    .setPage(currentPage)
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
//        Gson2PosListCustomer listCustomer = (Gson2PosListCustomer) doAPI(Gson2PosListCustomer.class,
//                POSAPI.REST_CUSOMTER_GET_LISTING,
//                null,
//                POSAPI.PARAM_CURRENT_PAGE, "" + currentPage,
//                POSAPI.PARAM_PAGE_SIZE, "" + pageSize,
//                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID
//        );
//        List<Customer> list = (List<Customer>) (List<?>) (listCustomer.items);
//        return list;
    }

    /**
     * Trả về 1 customer theo customer ID
     *
     * @param customerID
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Customer retrieveCustomer(String customerID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_GET_DETAIL);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);

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

    /**
     * Cập nhật thông tin của 1 customer
     *
     * @param pcustomer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean updateCustomer(final Customer pcustomer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_UPDATE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
            rp = statement.execute(wrapCustomer);
            String strJson = (new Gson()).toJson(wrapCustomer);
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

    /**
     * Thêm 1 customer
     *
     * @param pcustomer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean insertCustomer(final Customer pcustomer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_ADD);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
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
    @Override
    public int countAddress() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListAddress.class);
            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();

            // return
            return listAddress.total_count;
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
    @Override
    public List<CustomerAddress> retrieveCustomerAddress(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setPage(currentPage)
                    .setPageSize(pageSize);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListAddress.class);
            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
            List<CustomerAddress> list = (List<CustomerAddress>) (List<?>) (listAddress.items);

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
    @Override
    public List<CustomerAddress> retrieveCustomerAddress(String customerID)
            throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_ADDRESS_GET_LISTING);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListAddress.class);
            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
            List<CustomerAddress> list = (List<CustomerAddress>) (List<?>) (listAddress.items);

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
    @Override
    public List<CustomerAddress> retrieveCustomerAddress(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        return retrieveCustomerAddress(customer.getID());
    }

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
    @Override
    public boolean updateCustomerAddress(final Customer pcustomer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // Lưu address hiện tại tạm vào
        if (pcustomer.getAddress() == null) return false;
        if (!pcustomer.getAddress().contains(address)) return false;
        int indexAddress = pcustomer.getAddress().indexOf(address);
        CustomerAddress backupAddress = pcustomer.getAddress().get(indexAddress);
        pcustomer.getAddress().set(indexAddress, address);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_UPDATE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            return true;
        } catch (ConnectionException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } catch (IOException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } finally {
            // cập nhật trả lại address
            pcustomer.getAddress().set(indexAddress, backupAddress);

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
     * Thêm 1 địa chỉ cho customer
     *
     * @param pcustomer
     * @param address
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean insertCustomerAddress(final Customer pcustomer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // chèn tạm vào address
        if (pcustomer.getAddress() == null) {
            List<CustomerAddress> addressList = new ArrayList<CustomerAddress>();
            pcustomer.setAddressList(addressList);
        }
        pcustomer.getAddress().add(address);

        // gỡ tạm complain ra
        List<Complain> backupComplain = pcustomer.getComplain();
        pcustomer.setComplain(null);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_ADD);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            return true;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // gỡ address ra
            pcustomer.getAddress().remove(address);
            // khôi phục lại complain
            pcustomer.setComplain(backupComplain);

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
    public boolean deleteCustomerAddress(final Customer pcustomer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // xóa tạm khỏi address
        if (pcustomer.getAddress() == null) return false;
        if (!pcustomer.getAddress().contains(address)) return false;
        int indexAddress = pcustomer.getAddress().indexOf(address);
        pcustomer.getAddress().remove(address);

        // gỡ tạm complain ra
        List<Complain> backupComplain = pcustomer.getComplain();
        pcustomer.setComplain(null);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_DELETE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            return true;
        } catch (ConnectionException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } catch (IOException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } finally {
            // chèn address lại
            pcustomer.getAddress().add(indexAddress, address);
            // khôi phục complain lại
            pcustomer.setComplain(backupComplain);

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
         * Lấy toàn bộ complain của hệ thống, phân trang
         *
         * @return
         * @throws DataAccessException
         * @throws ConnectionException
         * @throws ParseException
         * @throws IOException
         * @throws java.text.ParseException
         */
    @Override
    public int countCustomerComplain() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_COMPLAIN_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setPageSize(1)
                    .setPage(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PostListComplain.class);

            // parse kết quả
            Gson2PostListComplain listComplain = (Gson2PostListComplain) rp.doParse();
            return listComplain.total_count;
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
     * Lấy toàn bộ complain của hệ thống, phân trang
     *
     * @param pageSize
     * @param currentPage
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Complain> retrieveCustomerComplain(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_COMPLAIN_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setPageSize(pageSize)
                    .setPage(currentPage);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PostListComplain.class);

            // parse kết quả
            Gson2PostListComplain listComplain = (Gson2PostListComplain) rp.doParse();
            List<Complain> list = (List<Complain>) (List<?>) (listComplain.items);
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
     * Lấy complain của 1 customer
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Complain> retrieveCustomerComplain(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_COMPLAIN_GET_LISTING);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, customer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PostListComplain.class);

            // parse kết quả
            Gson2PostListComplain listComplain = (Gson2PostListComplain) rp.doParse();
            List<Complain> list = (List<Complain>) (List<?>) (listComplain.items);
            List<Complain> returnList = new ArrayList<Complain>();
            if (list == null) return returnList;

            // Lọc riêng với customer email
            for (Complain complain: list) {
                if (customer.getEmail().equals(complain.getCustomerEmail()))
                    returnList.add(complain);
            }
            return returnList;
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
     * Cập nhật complain của 1 customer
     * @param pcustomer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean updateCustomerComplain(final Customer pcustomer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // Lưu complain hiện tại tạm vào
        if (pcustomer.getComplain() == null) return false;
        if (!pcustomer.getComplain().contains(complain)) return false;
        int indexComplain = pcustomer.getComplain().indexOf(complain);
        Complain backupComplain = pcustomer.getComplain().get(indexComplain);
        pcustomer.getComplain().set(indexComplain, complain);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_COMPLAIN_UPDATE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            // return
            return true;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // trả lại complain trước
            pcustomer.getComplain().set(indexComplain, backupComplain);

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
     * Thêm complain của 1 customer
     * @param pcustomer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean insertCustomerComplain(final Customer pcustomer, final Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

//        complain.setCustomerID(pcustomer.getID());
        complain.setCustomerEmail(pcustomer.getEmail());
        // chèn tạm complain vào
//        if (pcustomer.getComplain() == null) {
//            List<Complain> complainList = new ArrayList<Complain>();
//            pcustomer.setComplain(complainList);
//        }
//        pcustomer.getComplain().add(complain);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_COMPLAIN_ADD);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.complain = complain;
            rp = statement.execute(wrapCustomer);

            // return
            return Boolean.parseBoolean(rp.readResult2String("").trim());
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // remove complain
//            pcustomer.getComplain().remove(complain);

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
     * Cập nhật complain của 1 customer
     * @param pcustomer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean deleteCustomerComplain(final Customer pcustomer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // Xóa tạm complain
        if (pcustomer.getComplain() == null) return false;
        if (!pcustomer.getComplain().contains(complain)) return false;
        int indexComplain = pcustomer.getComplain().indexOf(complain);
        pcustomer.getComplain().remove(complain);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_COMPLAIN_DELETE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
                    //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;
            rp = statement.execute(wrapCustomer);
            String result = rp.readResult2String();
            // return
            return true;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // chèn complain trả lại
            pcustomer.getComplain().add(indexComplain, complain);

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
