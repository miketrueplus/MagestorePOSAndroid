package com.magestore.app.lib.usecase;

import com.magestore.app.lib.entity.Address;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Order;
import com.magestore.app.lib.entity.Product;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface CustomerUseCase extends UseCase {
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
    void newCustomer();

    /**
     * Khởi tạo mới 1 customer
     * @return
     */
    Customer createCustomer();

    List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Tạo một address mới
     * @param customer
     * @return
     */
    void newAddress(Customer customer, Address address);
    void newAddress(Address address);
    Address newAddress(Customer customer);
    Address newAddress();

    /**
     * Xóa address
     * @param customer
     * @param address
     */
    void deleteAddress(Customer customer, Address address);
    void deleteAddress(Address address);

    /**
     * Cập nhật address
     * @param customer
     * @param address
     */
    void updateAddress(Customer customer, Address address);
    void updateAddress(Address address);

    /**
     * Khởi tạo địa chỉ mới
     */
    public Address createAddress();
}
