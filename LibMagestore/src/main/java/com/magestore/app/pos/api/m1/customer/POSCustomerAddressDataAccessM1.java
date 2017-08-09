package com.magestore.app.pos.api.m1.customer;

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
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 8/9/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCustomerAddressDataAccessM1 extends POSAbstractDataAccessM1 implements CustomerAddressDataAccess {
    // wrap object lại và chuyênr thành json
    private class Wrap {
        Customer customer;
        Complain complain;
    }

    @Override
    public int count(Customer customer) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public CustomerAddress retrieve(Customer customer, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer, String searchString, int page, int pageSize) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public boolean update(Customer pcustomer, CustomerAddress oldCustomerAddress, CustomerAddress address) throws ParseException, InstantiationException, IllegalAccessException, IOException {
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
        List<Complain> backupComplain = null;
        if (pcustomer.getComplain() != null) {
            backupComplain = pcustomer.getComplain();
            pcustomer.setComplain(null);
        }

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ADDRESS_UPDATE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = removeAddressDefault(pcustomer);

            rp = statement.execute(wrapCustomer);
            return true;
        } catch (ConnectionException ex) {
            // khôi phục address default
            addAddressDefault(pcustomer);
            pcustomer.getAddress().remove(address);
            throw ex;
        } catch (IOException ex) {
            // khôi phục address default
            addAddressDefault(pcustomer);
            pcustomer.getAddress().remove(address);
            throw ex;
        } finally {
            // cập nhật trả lại address
            pcustomer.getAddress().set(indexAddress, oldCustomerAddress);
            // khôi phục lại complain
            if (backupComplain != null) {
                pcustomer.setComplain(backupComplain);
            }
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
    public boolean insert(Customer pcustomer, CustomerAddress... address) throws ParseException, InstantiationException, IllegalAccessException, IOException {
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ADDRESS_ADD);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = removeAddressDefault(pcustomer);

            rp = statement.execute(wrapCustomer);
            return true;
        } catch (ConnectionException ex) {
            // khôi phục address default
            addAddressDefault(pcustomer);
            throw ex;
        } catch (IOException ex) {
            // khôi phục address default
            addAddressDefault(pcustomer);
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
    public boolean delete(Customer pcustomer, CustomerAddress... address) throws ParseException, InstantiationException, IllegalAccessException, IOException {
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_ADDRESS_DELETE);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);
            //.setParam(POSAPI.PARAM_CUSTOMER_ID, pcustomer.getID());

            // thực thi truy vấn và parse kết quả thành object
            Wrap wrapCustomer = new Wrap();
            wrapCustomer.customer = removeAddressSameId(removeAddressDefault(pcustomer));

            rp = statement.execute(wrapCustomer);
            return true;
        } catch (ConnectionException ex) {
            // khôi phục address default
            addAddressDefault(pcustomer);
            pcustomer.getAddress().remove(address);
            throw ex;
        } catch (IOException ex) {
            // khôi phục address default
            addAddressDefault(pcustomer);
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

    private Customer removeAddressSameId(Customer customer) {
        Customer nCustomer = customer;
        List<CustomerAddress> listAddress = nCustomer.getAddress();
        if (listAddress != null && listAddress.size() > 2) {
            if(listAddress.get(0).getID().equals(listAddress.get(1).getID())){
                listAddress.remove(1);
            }
        }
        return nCustomer;
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
