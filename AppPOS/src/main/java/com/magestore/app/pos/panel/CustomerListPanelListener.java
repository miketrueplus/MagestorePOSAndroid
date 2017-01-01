package com.magestore.app.pos.panel;

import com.magestore.app.lib.entity.Customer;

import java.util.List;

/**
 * Sự kiện xử lý danh sách khách hàng
 * Created by Mike on 12/30/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerListPanelListener {
    /**
     * Khi user chọn 1 khách hàng trên danh sách
     * @param customer
     */
    void onSelectCustomer(Customer customer);

    /**
     * Khi load danh sách khách hàng thành công
     * @param customerList
     */
    void onSuccessLoadCustomer(List<Customer> customerList);
}
