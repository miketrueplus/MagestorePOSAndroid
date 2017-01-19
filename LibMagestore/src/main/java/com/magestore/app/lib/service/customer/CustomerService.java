package com.magestore.app.lib.service.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
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
    /**
     * Đặt biến tham chiếu customer để thực hiện các nghiệp vụ liên quan customer
     * @param customer
     */
    void setCustomer(Customer customer);

    /**
     * Trả tham chiếu đến customer đang xử lý nghiệp vụ
     * @return
     */
    Customer getCustomer();

    /**
     * Khởi tạo mới 1 customer
     * @return
     */
    Customer createCustomer();

    List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Tạo một customerAddress mới
     * @param customer
     * @return
     */
    void newAddress(Customer customer, CustomerAddress customerAddress);
    void newAddress(CustomerAddress customerAddress);
    CustomerAddress newAddress(Customer customer);
    CustomerAddress newAddress();

    /**
     * Xóa customerAddress
     * @param customer
     * @param customerAddress
     */
    void deleteAddress(Customer customer, CustomerAddress customerAddress);
    void deleteAddress(CustomerAddress customerAddress);

    /**
     * Cập nhật customerAddress
     * @param customer
     * @param customerAddress
     */
    void updateAddress(Customer customer, CustomerAddress customerAddress);
    void updateAddress(CustomerAddress customerAddress);

    /**
     * Khởi tạo địa chỉ mới
     */
    public CustomerAddress createAddress();

    /**
     * Cập nhật complain khách hàng
     * @param customerID
     * @return
     */
    List<Complain> getComplain(String customerID);
    void addComplain(Complain complain);
}
