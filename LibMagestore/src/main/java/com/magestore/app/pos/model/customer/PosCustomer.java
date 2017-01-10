package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.model.AbstractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Cấu trúc dữ liệu cho customers
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCustomer extends AbstractModel implements Customer {
    String group_id;
    String default_billing;
    String default_shipping;
    String created_at;
    String updated_at;
    String created_in;
    String email;
    String firstname;
    String lastname;
    String full_name;
    String gender;
    String store_id;
    String website_id;
    String subscriber_status;
    String telephone;
    String disable_auto_group_change;
    List<PosCustomerAddress> addresses;

    @Override
    public String getName() {
        return full_name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    @Override
    public String getFirstName() {
        return firstname;
    }

    @Override
    public String getLastName() {
        return lastname;
    }

    @Override
    public String getGroupID() {
        return group_id;
    }

    @Override
    public List<CustomerAddress> getAddress() {
        return (List<CustomerAddress>)(List<?>) addresses;
    }

    @Override
    public List<CustomerAddress> newAddressList() {
        addresses = new ArrayList<PosCustomerAddress>();
        return getAddress();
    }
}
