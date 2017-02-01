package com.magestore.app.lib.resourcemodel.customer;

import android.location.Address;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.resourcemodel.ChildListDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.ParentListDataAccess;
import com.magestore.app.lib.service.ChildListService;

/**
 * Created by Mike on 1/25/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerAddressDataAccess extends DataAccess, ChildListDataAccess<Customer, CustomerAddress> {
}
