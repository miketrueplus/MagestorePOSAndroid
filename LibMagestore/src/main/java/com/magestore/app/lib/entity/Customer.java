package com.magestore.app.lib.entity;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Customer extends Entity {
    String getName();
    String getEmail();
    String getFirstName();
    String getLastName();
    String getTelephone();
    String getGroupID();
    List<Address> getAddress();
    List<Address> newAddressList();
}