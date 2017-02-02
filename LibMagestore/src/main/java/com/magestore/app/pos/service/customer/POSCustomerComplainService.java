package com.magestore.app.pos.service.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.customer.CustomerComplainDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.service.customer.CustomerComplainService;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.model.customer.PosComplain;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/29/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerComplainService extends AbstractService implements CustomerComplainService {

    /**
     * Khởi tạo 1 complain cho customer
     *
     * @param customer
     * @return
     */
    @Override
    public Complain create(Customer customer) {
        Complain complain = new PosComplain();
//        complain.setCustomer(customer);
//        complain.setCustomerEmail(customer.getEmail());
        return complain;
    }

    /**
     * Khởi tạo 1 complain cho customer
     *
     * @param customer
     * @param complain
     * @return
     */
//    @Override
    public void add(Customer customer, Complain... complain) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        // cho complain tham chiếu đến customer
        complain[0].setCustomer(customer);
        complain[0].setCustomerID(customer.getID());
        complain[0].setCustomerEmail(customer.getEmail());

        // kiểm tra nếu complain list của customer đang trống thì khởi tạo
        List<Complain> complainList = customer.getComplain();
        if (complainList == null) complainList = new ArrayList<Complain>();
        customer.setComplain(complainList);

        // thêm một complain vào cho customer
        complainList.add(complain[0]);
    }

//    @Override
    public boolean remove(Customer customer, Complain... model) {
        return false;
    }

    @Override
    public int count(Customer customer) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public Complain retrieve(Customer customer, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Complain> retrieve(Customer customer, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    /**
     * Gọi API Trả về danh sách complain của 1 customer
     *
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public List<Complain> retrieve(Customer customer) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // lấy danh sách khách hàng
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerComplainDataAccess customerDataAccess = factory.generateCustomerComplainDataAccess();

        // lấy danh sách khách hàng
        customer.setComplain(customerDataAccess.retrieve(customer));
        return customer.getComplain();
    }

    @Override
    public boolean update(Customer customer, Complain oldModel, Complain newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    /**
     * Gọi API, lưu complain vào cho khách hàng
     *
     * @param customer
     * @param complain
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public boolean insert(Customer customer, Complain... complain) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerComplainDataAccess customerDataAccess = factory.generateCustomerComplainDataAccess();

        // lấy danh sách khách hàng
        boolean success = customerDataAccess.insert(customer, complain);

        // nếu thành, chèn vào
        if (success) add(customer, complain);
        return success;
    }

    @Override
    public boolean delete(Customer customer, Complain... childs) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }
}
