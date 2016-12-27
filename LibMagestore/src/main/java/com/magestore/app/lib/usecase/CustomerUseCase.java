package com.magestore.app.lib.usecase;

import com.magestore.app.lib.entity.Customer;
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

public interface CustomerUseCase {
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

    List<Customer> retrieveCustomerList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;

}
