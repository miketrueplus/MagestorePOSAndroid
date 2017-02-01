package com.magestore.app.pos.api.m2.customer;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.customer.CustomerComplainDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.parse.gson2pos.Gson2PostListComplain;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/29/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerComplainDataAccess extends POSAbstractDataAccess implements CustomerComplainDataAccess {
    // wrap object lại và chuyênr thành json
    private class Wrap {Customer customer; Complain complain;};

    /**
     * Lấy toàn bộ complain của hệ thống, phân trang
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws com.magestore.app.lib.parse.ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public int count(Customer customer) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException {
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

    @Override
    public Complain retrieve(Customer customer, String strID) throws com.magestore.app.lib.parse.ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    /**
     * Lấy toàn bộ complain của hệ thống, phân trang
     *
     * @param pageSize
     * @param currentPage
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws com.magestore.app.lib.parse.ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Complain> retrieve(Customer customer, int pageSize, int currentPage) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException  {
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
     * @throws com.magestore.app.lib.parse.ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Complain> retrieve(Customer customer) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException {
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
     * @throws com.magestore.app.lib.parse.ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean update(final Customer pcustomer, Complain oldComplain, Complain complain) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // Lưu complain hiện tại tạm vào
        if (pcustomer.getComplain() == null) return false;
        if (!pcustomer.getComplain().contains(oldComplain)) return false;
        int indexComplain = pcustomer.getComplain().indexOf(oldComplain);
//        Complain backupComplain = pcustomer.getComplain().get(indexComplain);
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
            pcustomer.getComplain().set(indexComplain, oldComplain);

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
     * @throws com.magestore.app.lib.parse.ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean insert(final Customer pcustomer, final Complain... complain) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // remove tạm customer id
//        complain[0].setCustomerID(null);
        complain[0].setCustomerEmail(pcustomer.getEmail());
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
            wrapCustomer.complain = complain[0];
            rp = statement.execute(wrapCustomer);

            // return
            return Boolean.parseBoolean(rp.readResult2String("").trim());
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // remove complain
            complain[0].setCustomerID(pcustomer.getID());
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
     * @throws com.magestore.app.lib.parse.ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean delete(final Customer pcustomer, Complain... complain) throws DataAccessException, ConnectionException, com.magestore.app.lib.parse.ParseException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // Xóa tạm complain
        if (pcustomer.getComplain() == null) return false;
        if (!pcustomer.getComplain().contains(complain[0])) return false;
        int indexComplain = pcustomer.getComplain().indexOf(complain[0]);
        pcustomer.getComplain().remove(complain[0]);

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
            pcustomer.getComplain().add(indexComplain, complain[0]);

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
