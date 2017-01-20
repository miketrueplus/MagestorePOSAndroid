package com.magestore.app.lib.resourcemodel.customer;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.util.List;

/**
 * Giao diện các API trả về danh mục khách hàng customer
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerDataAccess extends DataAccess {
    /**
     * Đếm tổng số customer trong hệ thống
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    int countCustomer() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về danh sách customers theo phân trang
     * @param pageSize
     * @param currentPage
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    List<Customer> retrieveCustomers(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về 1 customer chi tiết theo id
     * @param customerID
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    Customer retrieveCustomer(String customerID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Cập nhật thông tin 1 customer
     * @param customer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    void updateCustomer(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Thêm 1 customer
     * @param customer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    void insertCustomer(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Đếm số address của customer trong hệ thống
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    int countAddress() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    List<CustomerAddress> retrieveCustomerAddress(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    List<CustomerAddress> retrieveCustomerAddress(String customerID)
            throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    List<CustomerAddress> retrieveCustomerAddress(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    void updateCustomerAddress(Customer customer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    void insertCustomerAddress(Customer customer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    void deleteCustomerAddress(Customer pcustomer, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Đếm số complain của toàn hệ thống
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    int countCustomerComplain() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    List<Complain> retrieveCustomerComplain(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

    /**
     * Trả về complain của 1 customer
     * @param strCustomerID
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    List<Complain> retrieveCustomerComplain(String strCustomerID) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    List<Complain> retrieveCustomerComplain(Customer customer) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    void updateCustomerComplain(Customer customer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;

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
    void insertCustomerComplain(Customer customer, Complain complain) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
