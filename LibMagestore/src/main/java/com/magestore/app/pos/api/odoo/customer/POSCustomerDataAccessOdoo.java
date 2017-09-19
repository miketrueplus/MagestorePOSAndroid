package com.magestore.app.pos.api.odoo.customer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.DataCustomer;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.customer.PosDataCustomer;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomer;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomerPaserModelOdoo;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCustomerDataAccessOdoo extends POSAbstractDataAccessOdoo implements CustomerDataAccess {

    private class CustomerEntity {
        String id;
        String email;
        String name;
        String phone;
        String street;
        String zip;
        String vat;
        String company_type;
        String state_id;
        String country_id;
        String city;
        String street2;
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
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListCustomerPaserModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();

            // return
            return listCustomer.getTotalCount();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListCustomerPaserModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();

            // return
            return listCustomer.getItems();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(page)
                    .setPageSize(pageSize)
                    .setFilterOrLike("email", finalSearch)
                    .setFilterOrLike("name", finalSearch)
                    .setFilterOrLike("phone", finalSearch)
                    .setSortOrderASC("name");

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosListCustomerPaserModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();

            // return
            return listCustomer.getItems();
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
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_UPDATE);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            Customer old_customer = newModel;

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.id = oldModel.getID();
            customerEntity.name = old_customer.getName();
            customerEntity.email = old_customer.getEmail();
            customerEntity.company_type = getCustomerGroupCode(old_customer.getGroupID());
            List<CustomerAddress> listAddress = old_customer.getAddress();
            if (listAddress != null && listAddress.size() > 0) {
                CustomerAddress address = listAddress.get(0);
                customerEntity.phone = address.getTelephone();
                customerEntity.street = address.getStreet1();
                customerEntity.zip = address.getPostCode();
                customerEntity.vat = address.getVAT();
                if (address.getRegion() != null) {
                    customerEntity.state_id = String.valueOf(address.getRegion().getRegionID());
                }
                customerEntity.country_id = getCountryId(address.getCountry());
                customerEntity.city = address.getCity();
                customerEntity.street2 = address.getStreet2();
            }

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(customerEntity);
            rp.setParseImplement(new Gson2PosListCustomerPaserModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();
            Customer customer_respone = listCustomer.getItems().get(0);
            old_customer.setID(customer_respone.getID());
            // return
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
    public boolean insert(Customer... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CUSOMTER_CREATE_CUSTOMER);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            Customer old_customer = models[0];

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.name = old_customer.getName();
            customerEntity.email = old_customer.getEmail();
            customerEntity.company_type = getCustomerGroupCode(old_customer.getGroupID());
            List<CustomerAddress> listAddress = old_customer.getAddress();
            if (listAddress != null && listAddress.size() > 0) {
                CustomerAddress address = listAddress.get(0);
                customerEntity.phone = address.getTelephone();
                customerEntity.street = address.getStreet1();
                customerEntity.zip = address.getPostCode();
                customerEntity.vat = address.getVAT();
                if (address.getRegion() != null) {
                    customerEntity.state_id = String.valueOf(address.getRegion().getRegionID());
                }
                customerEntity.country_id = getCountryId(address.getCountry());
                customerEntity.city = address.getCity();
                customerEntity.street2 = address.getStreet2();
            }

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(customerEntity);
            rp.setParseImplement(new Gson2PosListCustomerPaserModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();
            Customer customer_respone = listCustomer.getItems().get(0);
            old_customer.setID(customer_respone.getID());
            // return
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
    public boolean delete(Customer... models) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    private String getCountryId(String code) {
        ConfigCountry country = ConfigUtil.getListCountry().get(code);
        return country != null ? country.getKey() : "";
    }



    private String getCustomerGroupCode(String id) {
        String code = ConfigUtil.getListCustomerGroup().get(id);
        return !StringUtil.isNullOrEmpty(code) ? code : "";
    }
}
