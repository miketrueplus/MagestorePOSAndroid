package com.magestore.app.pos.service.customer;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.resourcemodel.ChildListDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.customer.CustomerAddressDataAccess;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 1/29/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerAddressService extends AbstractService implements CustomerAddressService {

    /**
     * Đếm số address trong customer
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
        if (customer.getAddress() == null) return 0;
        return customer.getAddress().size();
    }

    /**
     * Khởi tạo address theo customer
     *
     * @param customer
     * @return
     */
    @Override
    public CustomerAddress create(Customer customer) {
        // cho address tham chiếu đến customer
        CustomerAddress customerAddress = new PosCustomerAddress();
//        customerAddress.setCustomer(customer);
//        customerAddress.setCustomer(customer.getID());
        return customerAddress;
    }

    /**
     * Khởi tạo address theo customer
     *
     * @param customer
     * @return
     */
//    @Override
    public void add(Customer customer, CustomerAddress... customerAddress) {
        // cho address tham chiếu đến customer
        customerAddress[0].setCustomer(customer);
        customerAddress[0].setCustomer(customer.getID());

        // Kiểm tra danh sách địa chỉ, Nếu chưa có địa chỉ, khởi tạo danh sách
        List<CustomerAddress> customerAddressList = customer.getAddress();
        if (customerAddressList == null)
            customerAddressList = new ArrayList<CustomerAddress>();
        customer.setAddressList(customerAddressList);

        // Thêm địa chỉ mới vào
        customerAddressList.add(customerAddress[0]);
//        return customerAddress;
    }

    @Override
    public CustomerAddress retrieve(Customer customer, String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return customer.getAddress().get(0);
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return customer.getAddress();
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return customer.getAddress();
    }

    @Override
    public List<CustomerAddress> retrieve(Customer customer, String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return customer.getAddress();
    }

    /**
     * Gỡ 1 address ra khỏi customer, không cập nhật API
     *
     * @param customer
     * @param customerAddress
     */
    private boolean remove(Customer customer, CustomerAddress... customerAddress) {
        // Tìm customerAddress
        if (customer != null && customerAddress[0] != null & customer.getAddress() != null)
            return customer.getAddress().remove(customerAddress[0]);
        return false;
    }

    /**
     * Xóa 1 address khỏi customer, cập nhật API
     *
     * @param customer
     * @param customerAddress
     * @throws IOException
     * @throws InstantiationException
     * @throws ParseException
     * @throws IllegalAccessException
     */
    @Override
    public boolean delete(Customer customer, CustomerAddress... customerAddress) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerAddressDataAccess customerAddressDataAccess = factory.generateCustomerAddressDataAccess();

        // Thực hiện xóa
        boolean success = customerAddressDataAccess.delete(customer, customerAddress);

        // Xóa address trong danh sách
        if (success) remove(customer, customerAddress);
        return success;
    }

    @Override
    public Region createRegion() {
        PosRegion region = new PosRegion();
        return region;
    }

    @Override
    public Customer addAddressDefaultToCustomer(Customer customer, Customer guest_customer, String userAddressDefault) {
        Customer newGuestCustomer = guest_customer;
        newGuestCustomer.getAddress().get(0).setShortAddress(userAddressDefault);
        newGuestCustomer.getAddress().get(0).setIsStoreAddress(true);
        List<CustomerAddress> listCustomerAddress = customer.getAddress();
        if (listCustomerAddress != null && listCustomerAddress.size() > 0) {
            boolean default_shipping = false;
            boolean default_billing = false;
            for (CustomerAddress address : listCustomerAddress) {
                if (!StringUtil.isNullOrEmpty(address.isShipping()) && address.isShipping().equals("true") && !StringUtil.isNullOrEmpty(address.isBilling()) && address.isBilling().equals("true")) {
                    listCustomerAddress.set(0, address);
                    listCustomerAddress.add(newGuestCustomer.getAddress().get(0));
                    customer.setUseOneAddress(true);
                    default_shipping = true;
                    default_billing = true;
                    customer.setHasDefaultShipping(true);
                    customer.setHasDefaultBilling(true);
                    break;
                } else if (!StringUtil.isNullOrEmpty(address.isShipping()) && address.isShipping().equals("true")) {
                    listCustomerAddress.set(0, address);
                    default_shipping = true;
                    customer.setHasDefaultShipping(true);
                } else if (!StringUtil.isNullOrEmpty(address.isBilling()) && address.isBilling().equals("true")) {
                    if (listCustomerAddress.size() >= 2) {
                        listCustomerAddress.set(1, address);
                    }
                    default_billing = true;
                    customer.setHasDefaultBilling(true);
                }
            }

            if (!default_shipping && !default_billing) {
                listCustomerAddress.add(0, newGuestCustomer.getAddress().get(0));
            } else {
                listCustomerAddress.add(newGuestCustomer.getAddress().get(0));
            }
        } else {
            listCustomerAddress.add(newGuestCustomer.getAddress().get(0));
            customer.setUseOneAddress(true);
        }

        return customer;
    }

    /**
     * Cập nhật 1 address vào customer, cập nhật API
     *
     * @param customer
     * @param customerAddress
     */
    @Override
    public boolean update(Customer customer, CustomerAddress oldCustomerAddress, CustomerAddress customerAddress) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerAddressDataAccess customerDataAccess = factory.generateCustomerAddressDataAccess();

        if (oldCustomerAddress.getID() != null) {
            customerAddress.setId(oldCustomerAddress.getID());
        }

        // lấy danh sách khách hàng
        boolean success = customerDataAccess.update(customer, oldCustomerAddress, customerAddress);

        if (success) {
            // thay bằng address mới
            int oldIndex = customer.getAddress().indexOf(oldCustomerAddress);
            customerAddress.setCustomer(customer);
            customerAddress.setCustomer(customer.getID());
            customer.getAddress().set(oldIndex, customerAddress);
        }

        return success;
    }

    /**
     * Thêm 1 address cho customer, cập nhật API
     *
     * @param customer
     * @param customerAddress
     */
    @Override
    public boolean insert(Customer customer, CustomerAddress... customerAddress) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CustomerAddressDataAccess customerDataAccess = factory.generateCustomerAddressDataAccess();

        // lấy danh sách khách hàng
        boolean success = customerDataAccess.insert(customer, customerAddress);

        // chèn address vào customer
        if (success) add(customer, customerAddress);

        // return
        return success;
    }
}
