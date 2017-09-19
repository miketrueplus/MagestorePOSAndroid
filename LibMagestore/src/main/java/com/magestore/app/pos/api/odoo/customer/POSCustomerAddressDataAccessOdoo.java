package com.magestore.app.pos.api.odoo.customer;

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
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.customer.PosDataCustomer;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomerPaserModelOdoo;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCustomerAddressDataAccessOdoo extends POSAbstractDataAccessOdoo implements CustomerAddressDataAccess {

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
    public int count(Customer customer) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public CustomerAddress retrieve(Customer customer, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer, String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Customer customer, CustomerAddress oldChild, CustomerAddress newChild) throws ParseException, InstantiationException, IllegalAccessException, IOException {
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

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.id = customer.getID();
            customerEntity.email = customer.getEmail();
            customerEntity.company_type = getCustomerGroupCode(customer.getGroupID());
            CustomerAddress address = newChild;
            customerEntity.name = address.getName();
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

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute(customerEntity);
            rp.setParseImplement(new Gson2PosListCustomerPaserModelOdoo());
            rp.setParseModel(PosDataCustomer.class);
            DataCustomer listCustomer = (DataCustomer) rp.doParse();
            Customer customer_respone = listCustomer.getItems().get(0);
            newChild = customer_respone.getAddress().get(0);
            customer.setFirstName(customer_respone.getFirstName());
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
    public boolean insert(Customer customer, CustomerAddress... childs) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public boolean delete(Customer customer, CustomerAddress... childs) throws ParseException, InstantiationException, IllegalAccessException, IOException {
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
