package com.magestore.app.lib.usecase.pos;

import com.magestore.app.lib.entity.Address;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Product;
import com.magestore.app.lib.entity.pos.PosAddress;
import com.magestore.app.lib.entity.pos.PosCustomer;
import com.magestore.app.lib.gateway.CustomerGateway;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.ProductGateway;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.usecase.CustomerUseCase;

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

public class POSCustomerUseCase extends AbstractUseCase implements CustomerUseCase {
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
    public void newCustomer() {
        setCustomer(createCustomer());
    }

    @Override
    public List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo customer gateway factory
        GatewayFactory factory = GatewayFactory.getFactory(getContext());
        CustomerGateway customerGateway = factory.generateCustomerGateway();

        // Lấy list 30 products đầu tiên
        return customerGateway.getCustomers(size, 1);
    }

    @Override
    public void deleteAddress(Customer customer, Address address) {
        // Tìm address
        if (customer != null && address != null & customer.getAddress() != null)
            customer.getAddress().remove(address);
    }

    @Override
    public void deleteAddress(Address address) {
        deleteAddress(mCustomer, address);
    }

    @Override
    public void updateAddress(Customer customer, Address address) {

    }

    @Override
    public void updateAddress(Address address) {
        updateAddress(mCustomer, address);
    }

    @Override
    public void newAddress(Customer customer, Address address) {
        // Kiểm tra danh sách địa chỉ
        List<Address> addressList = customer.getAddress();

        // Nếu chưa có địa chỉ, khởi tạo danh sách
        if (addressList == null)
            addressList = customer.newAddressList();

        // Thêm địa chỉ mới vào
        addressList.add(address);
    }

    @Override
    public void newAddress(Address address) {
        newAddress(mCustomer, address);
    }

    @Override
    public Address newAddress(Customer customer) {
        Address address = new PosAddress();
        newAddress(customer, address);
        return address;
    }

    @Override
    public Address newAddress() {
        return newAddress(mCustomer);
    }

    @Override
    public Address createAddress() {
        return new PosAddress();
    }
}
