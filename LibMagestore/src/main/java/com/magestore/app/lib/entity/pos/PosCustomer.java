package com.magestore.app.lib.entity.pos;

import android.os.Parcel;
import android.os.Parcelable;

import com.magestore.app.lib.entity.Address;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.entity.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Cấu trúc dữ liệu cho customers
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCustomer extends AbstractEntity implements Customer {
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
    List<PosAddress> addresses;

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
    public List<Address> getAddress() {
        return (List<Address>)(List<?>) addresses;
    }

    @Override
    public List<Address> newAddressList() {
        addresses = new ArrayList<PosAddress>();
        return getAddress();
    }
}
