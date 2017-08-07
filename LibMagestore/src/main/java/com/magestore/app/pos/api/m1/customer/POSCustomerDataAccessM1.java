package com.magestore.app.pos.api.m1.customer;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomer;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 8/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCustomerDataAccessM1 extends POSAbstractDataAccess implements CustomerDataAccess {
    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CUSOMTER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1)
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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

    @Override
    public List<Customer> retrieve() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return retrieve(1, 100);
    }

    @Override
    public List<Customer> retrieve(int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CUSOMTER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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

    @Override
    public List<Customer> retrieve(String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        String finalSearch = "%" + searchString + "%";

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CUSOMTER_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name")
                    .setFilterOrLike("name", finalSearch)
                    .setFilterOrLike("email", finalSearch)
                    .setFilterOrLike("full_name", finalSearch)
                    .setFilterOrLike("telephone", finalSearch)
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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

    @Override
    public Customer retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Customer oldModel, Customer newModel) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean insert(Customer... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Customer... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }
}
