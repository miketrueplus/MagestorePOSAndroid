package com.magestore.app.pos.api.m2.customer;

import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListAddress;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/29/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerAddressDataAccess extends POSAbstractDataAccess implements CustomerAddressDataAccess {
    // wrap object lại và chuyênr thành json
    private class Wrap {
        Customer customer;
        Complain complain;
    }

    /**
     * Đếm số lượng address
     *
     * @param customer
     * @return
     * @throws ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Override
    public int count(Customer customer) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setPage(1)
                    .setPageSize(1);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListAddress.class);
            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();

            // return
            return listAddress.total_count;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    /**
     * Lấy danh sách address theo phân trang
     *
     * @param customer
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Override
    public List<CustomerAddress> retrieve(Customer customer, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_GET_LISTING);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID)
                    .setPage(page)
                    .setPageSize(pageSize);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListAddress.class);
            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
            List<CustomerAddress> list = (List<CustomerAddress>) (List<?>) (listAddress.items);

            // return
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    /**
     * Lấy danh sách address toàn bộ theo customer
     *
     * @param customer
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<CustomerAddress> retrieve(Customer customer)
            throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CUSOMTER_ADDRESS_GET_LISTING);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, customer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
            //.setParam(POSAPI.PARAM_CUSTOMER_ID, customerID);

            // thực thi truy vấn và parse kết quả thành object
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListAddress.class);
            Gson2PosListAddress listAddress = (Gson2PosListAddress) rp.doParse();
            List<CustomerAddress> list = (List<CustomerAddress>) (List<?>) (listAddress.items);

            // return
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    /**
     * Trả về customer address theo id
     *
     * @param customer
     * @param strID
     * @return
     * @throws ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Override
    public CustomerAddress retrieve(Customer customer, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    /**
     * Search customer address theo chuỗi
     *
     * @param customer
     * @param searchString
     * @param page
     * @param pageSize
     * @return
     * @throws ParseException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    @Override
    public List<CustomerAddress> retrieve(Customer customer, String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return retrieve(customer);
    }

    /**
     * Cập nhật thông tin khách hàng
     *
     * @param pcustomer
     * @param oldCustomerAddress
     * @param address
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean update(final Customer pcustomer, CustomerAddress oldCustomerAddress, CustomerAddress address) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // Lưu address hiện tại tạm vào
        if (pcustomer.getAddress() == null) return false;
        if (!pcustomer.getAddress().contains(oldCustomerAddress)) return false;
        int indexAddress = pcustomer.getAddress().indexOf(oldCustomerAddress);
        pcustomer.getAddress().set(indexAddress, address);

        // gỡ tạm complain ra
        List<Complain> backupComplain = pcustomer.getComplain();
        pcustomer.setComplain(null);

        // Gỡ Address default


        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_UPDATE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
            //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = removeAddressDefault(pcustomer);
            rp = statement.execute(wrapCustomer);
            return true;
        } catch (ConnectionException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } catch (IOException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } finally {
            // cập nhật trả lại address
            pcustomer.getAddress().set(indexAddress, oldCustomerAddress);
            // khôi phục lại complain
            pcustomer.setComplain(backupComplain);
            // khôi phục address default
            addAddressDefault(pcustomer);

            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    /**
     * Thêm 1 địa chỉ cho customer
     *
     * @param pcustomer
     * @param address
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public boolean insert(final Customer pcustomer, CustomerAddress... address) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // chèn tạm vào address
        if (pcustomer.getAddress() == null) {
            List<CustomerAddress> addressList = new ArrayList<CustomerAddress>();
            pcustomer.setAddressList(addressList);
        }
        pcustomer.getAddress().add(address[0]);

        // gỡ tạm complain ra
        List<Complain> backupComplain = pcustomer.getComplain();
        pcustomer.setComplain(null);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_ADD);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
            //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = removeAddressDefault(pcustomer);

            rp = statement.execute(wrapCustomer);
            return true;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // gỡ address ra
            pcustomer.getAddress().remove(address[0]);
            // khôi phục lại complain
            pcustomer.setComplain(backupComplain);
            // khôi phục address default
            addAddressDefault(pcustomer);

            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public boolean delete(final Customer pcustomer, CustomerAddress... address) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;

        // xóa tạm khỏi address
        if (pcustomer.getAddress() == null) return false;
        if (!pcustomer.getAddress().contains(address[0])) return false;
        int indexAddress = pcustomer.getAddress().indexOf(address[0]);
        pcustomer.getAddress().remove(address[0]);

        // gỡ tạm complain ra
        List<Complain> backupComplain = pcustomer.getComplain();
        pcustomer.setComplain(null);

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_ADDRESS_DELETE);
            statement.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
            //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = pcustomer;

            rp = statement.execute(wrapCustomer);
            return true;
        } catch (ConnectionException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } catch (IOException ex) {
            pcustomer.getAddress().remove(address);
            throw ex;
        } finally {
            // chèn address lại
            pcustomer.getAddress().add(indexAddress, address[0]);
            // khôi phục complain lại
            pcustomer.setComplain(backupComplain);
            // khôi phục address default
            addAddressDefault(pcustomer);

            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    private Customer removeAddressDefault(Customer customer) {
        Customer nCustomer = customer;
        CustomerAddress guest_address = ConfigUtil.getCustomerGuest().getAddress().get(0);
        String guest_address_id = guest_address.getID();
        List<CustomerAddress> listAddress = nCustomer.getAddress();
        for (CustomerAddress customerAddress : listAddress) {
            if (customerAddress.getID().equals(guest_address_id)) {
                listAddress.remove(customerAddress);
                break;
            }
        }
        return nCustomer;
    }

    private void addAddressDefault(Customer customer) {
        CustomerAddress guest_address = ConfigUtil.getCustomerGuest().getAddress().get(0);
        customer.getAddress().add(0, guest_address);
    }
}
