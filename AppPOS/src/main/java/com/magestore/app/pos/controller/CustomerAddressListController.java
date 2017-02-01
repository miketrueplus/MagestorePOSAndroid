package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.ChildListController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.customer.CustomerService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Task cho danh sách địa chỉ khách hàng
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerAddressListController
        extends AbstractChildListController<Customer, CustomerAddress>
        implements ChildListController<Customer, CustomerAddress> {

    /**
     * Thiết lập service
     *
     * @param service
     */
    public void setCustomerService(CustomerService service) {
        try {
            setChildListService(ServiceFactory.getFactory(getMagestoreContext()).generateCustomerAddressService());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void bindCustomer(Customer customer) {
        bindParent(customer);
    }

    public CustomerAddress createNewCustomerAddress() {
        try {
            return getChildListService().create(getParent());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
