package com.magestore.app.pos.service.customer;

import com.google.gson.Gson;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.DataAccessException;
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

public class POSCustomerService extends AbstractService
        implements CustomerService
{
    Customer mCustomer;

//    /**
//     * Khởi tạo 1 address trống
//     *
//     * @return
//     */
//    @Override
//    public CustomerAddress createAddress() {
//        return new PosCustomerAddress();
//    }


    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();
        return customerDataAccess.count();
    }

    @Override
    public List<Customer> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();

        // lấy danh sách khách hàng
        return customerDataAccess.retrieve(page, pageSize);
    }

    @Override
    public List<Customer> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();

        // lấy danh sách khách hàng
        return customerDataAccess.retrieve(1, 500);
    }

    @Override
    public List<Customer> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // cập nhật factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();

        // lấy danh sách khách hàng
        return customerDataAccess.retrieve(searchString, page, pageSize);
    }

    @Override
    public boolean update(Customer oldCustomer, Customer newCustomer) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();

        // set lại id và address của old customer cho new customer
        newCustomer.setID(oldCustomer.getID());
        newCustomer.setAddressList(oldCustomer.getAddress());

        // kiểm tra có complains không, không thì phải tạo mới
        if (newCustomer.getComplain() == null) {
            List<Complain> listComplain = (List<Complain>) (List<?>) new ArrayList<PosComplain>();
            newCustomer.setComplain(listComplain);
        }

        // cập nhật d.sách
        return customerDataAccess.update(oldCustomer, newCustomer);
    }

    @Override
    public boolean insert(Customer... models) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();

        for (Customer customer : models) {
            customerDataAccess.insert(customer);
        }
        return true;
    }

    @Override
    public boolean delete(Customer... models) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerDataAccess customerDataAccess = factory.generateCustomerDataAccess();

        for (Customer customer : models) {
            customerDataAccess.delete(customer);
        }
        return true;
    }

    @Override
    public Customer create() {
        return new PosCustomer();
    }

    @Override
    public Customer retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }
}
