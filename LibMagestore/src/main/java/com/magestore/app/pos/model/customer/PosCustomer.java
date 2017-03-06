package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Cấu trúc dữ liệu cho customers
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCustomer extends PosAbstractModel implements Customer {
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
    List<Complain> complainsa;

    @Gson2PosExclude
    boolean use_one_address;

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setID(String strID) {
        id = strID;
    }

    @Override
    public String getName() {
        return full_name;
    }

    @Override
    public void setName(String strName) {
        full_name = strName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    @Override
    public void setTelephone(String strTelephone) {
        telephone = strTelephone;
    }

    @Override
    public String getFirstName() {
        return firstname;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }

    @Override
    public String getLastName() {
        return lastname;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    @Override
    public String getGroupID() {
        return group_id;
    }

    @Override
    public void setGroupID(String groupID) {
        this.group_id = groupID;
    }

    @Override
    public List<CustomerAddress> getAddress() {
        return (List<CustomerAddress>) (List<?>) addresses;
    }

    @Override
    public void setAddressList(List<CustomerAddress> addresses) {
        this.addresses = (List<PosCustomerAddress>) (List<?>) addresses;
    }


    @Override
    public List<Complain> getComplain() {
        return (List<Complain>) (List<?>) complainsa;
    }

    @Override
    public void setComplain(List<Complain> complains) {
        this.complainsa = complains;
    }

    @Override
    public String getSubscriber() {
        return subscriber_status;
    }

    @Override
    public void setSubscriber(String strSubscriber) {
        subscriber_status = strSubscriber;
    }

    @Override
    public boolean getUseOneAddress() {
        return use_one_address;
    }

    @Override
    public void setUseOneAddress(boolean bUseOnleAddress) {
        use_one_address = bUseOnleAddress;
    }

    @Override
    public String getDisplayContent() {
        return getName();
    }

    @Override
    public String getSubDisplayContent() {
        return getEmail();
    }
}
