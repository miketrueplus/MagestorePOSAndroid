package com.magestore.app.lib.resourcemodel.customer;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.PlaceAddressComponent;
import com.magestore.app.lib.model.customer.PlaceAutoComplete;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.ListDataAccess;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.ParentListService;

import java.io.IOException;
import java.util.List;

/**
 * Giao diện các API trả về danh mục khách hàng customer
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerDataAccess extends DataAccess,
        ListDataAccess<Customer> {
    /**
     * Đếm số address của customer trong hệ thống
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    int countAddress() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về địa chỉ của customer trong hệ thống, được phân trang
     * @param pageSize
     * @param currentPage
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    List<CustomerAddress> retrieveCustomerAddress(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về danh sách địa chỉ của 1 customer
     * @param customerID
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    List<CustomerAddress> retrieveCustomerAddress(String customerID)
//            throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về danh sách địa chỉ của 1 customer
     * @param customer
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    List<CustomerAddress> retrieveCustomerAddress(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Cập nhật địa chỉ của customer
     * @param customer
     * @param address
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    boolean updateCustomerAddress(Customer customer, CustomerAddress oldAddress, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Thêm địa chỉ cho 1 customer
     * @param customer
     * @param address
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    boolean insertCustomerAddress(Customer customer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

//    boolean deleteCustomerAddress(Customer pcustomer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Đếm số complain của toàn hệ thống
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    int countCustomerComplain() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về danh sách complain. có phân trang
     * @param pageSize
     * @param currentPage
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    List<Complain> retrieveCustomerComplain(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về complain của 1 customer
     * @param customer
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    List<Complain> retrieveCustomerComplain(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Cập nhật complain cho 1 customer
     * @param customer
     * @param complain
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    boolean updateCustomerComplain(Customer customer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Thêm complain cho 1 customer
     * @param customer
     * @param complain
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
//    boolean insertCustomerComplain(Customer customer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

//    boolean deleteCustomerComplain(Customer pcustomer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    List<PlaceAutoComplete> placeAutoComplete(String input) throws java.text.ParseException, InstantiationException, IllegalAccessException, IOException;
    List<PlaceAddressComponent> placeDetail(String detailId) throws java.text.ParseException, InstantiationException, IllegalAccessException, IOException;
}
