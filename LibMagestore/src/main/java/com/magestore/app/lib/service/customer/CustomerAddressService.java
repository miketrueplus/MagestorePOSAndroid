package com.magestore.app.lib.service.customer;

import android.location.Address;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.service.ChildListService;

/**
 * Created by Mike on 1/25/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerAddressService extends ChildListService<Customer, CustomerAddress> {
    Region createRegion();
}
