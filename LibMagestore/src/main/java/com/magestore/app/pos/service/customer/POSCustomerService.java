package com.magestore.app.pos.service.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.pos.model.customer.PosComplain;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Các thao tác nghiệp vụ liên quan đến customer
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerService extends AbstractService implements CustomerService {
    Customer mCustomer;

    @Override
    public void setCustomer(Customer customer) {
        mCustomer = customer;
    }

    @Override
    public Customer getCustomer() {
        return mCustomer;
    }

    @Override
    public Customer createCustomer() {
        return new PosCustomer();
    }


    @Override
    public List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerGateway = factory.generateCustomerDataAccess();

        // lấy danh sách khách hàng
        return customerGateway.getCustomers(size, 1);
    }

    @Override
    public void deleteAddress(Customer customer, CustomerAddress customerAddress) {
        // Tìm customerAddress
        if (customer != null && customerAddress != null & customer.getAddress() != null)
            customer.getAddress().remove(customerAddress);
    }

    @Override
    public void deleteAddress(CustomerAddress customerAddress) {
        deleteAddress(mCustomer, customerAddress);
    }

    @Override
    public void updateAddress(Customer customer, CustomerAddress customerAddress) {

    }

    @Override
    public void updateAddress(CustomerAddress customerAddress) {
        updateAddress(mCustomer, customerAddress);
    }

    @Override
    public void newAddress(CustomerAddress customerAddress) {
        newAddress(mCustomer, customerAddress);
    }

    @Override
    public CustomerAddress newAddress() {
        return newAddress(mCustomer);
    }

    @Override
    public CustomerAddress newAddress(Customer customer) {
        CustomerAddress customerAddress = new PosCustomerAddress();
        newAddress(customer, customerAddress);
        return customerAddress;
    }

    @Override
    public void newAddress(Customer customer, CustomerAddress customerAddress) {
        // Kiểm tra danh sách địa chỉ
        List<CustomerAddress> customerAddressList = customer.getAddress();

        // Nếu chưa có địa chỉ, khởi tạo danh sách
        if (customerAddressList == null)
            customerAddressList = customer.newAddressList();

        // Thêm địa chỉ mới vào
        customerAddressList.add(customerAddress);
    }

    @Override
    public CustomerAddress createAddress() {
        return new PosCustomerAddress();
    }

    @Override
    public List<Complain> getComplain(String customerID) {
        Complain complain1 = new PosComplain();
        complain1.setContent("hello");
        complain1.setCustomerID(customerID);
        complain1.setCreateAt("now");
        complain1.setComplainID("1");

        Complain complain2 = new PosComplain();
        complain2.setContent("hello 2");
        complain2.setCustomerID(customerID);
        complain2.setCreateAt("now 2");
        complain2.setComplainID("2");

        List<Complain> list = new ArrayList<Complain>();
        list.add(complain1);
        list.add(complain2);

        return list;
    }

    @Override
    public void addComplain(Complain complain) {

    }
}
