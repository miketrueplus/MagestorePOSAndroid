package com.magestore.app.lib.service.customer;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerService extends Service {

    Customer createCustomer();

    List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    void updateCustomer(Customer customer) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    void insertCustomer(Customer customer) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    CustomerAddress createAddress();

    CustomerAddress createAddress(Customer customer, CustomerAddress customerAddress);

    CustomerAddress createAddress(Customer customer);

    void removeAddress(Customer customer, CustomerAddress customerAddress);

    void deleteAddress(Customer customer, CustomerAddress customerAddress) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    void updateAddress(Customer customer, CustomerAddress customerAddress) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    void insertAddress(Customer customer, CustomerAddress customerAddress) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Complain createComplain();

    Complain createComplain(Customer customer);

    Complain createComplain(Customer customer, Complain complain);

    List<Complain> retrieveComplain(String customerID) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    void insertComplain(Customer customer, Complain complain) throws InstantiationException, IllegalAccessException, IOException, ParseException;
}
