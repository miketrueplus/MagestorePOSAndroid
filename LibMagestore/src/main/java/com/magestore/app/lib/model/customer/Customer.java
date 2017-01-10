package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Customer extends Model {
    String getName();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getTelephone();
    String getGroupID();
    List<CustomerAddress> getAddress();
    List<CustomerAddress> newAddressList();
}