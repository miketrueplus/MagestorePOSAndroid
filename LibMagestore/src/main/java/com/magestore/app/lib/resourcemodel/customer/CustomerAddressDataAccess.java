package com.magestore.app.lib.resourcemodel.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.ParentListDataAccess;

/**
 * Created by Mike on 1/25/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerAddressDataAccess extends DataAccess, ParentListDataAccess<Customer, Complain> {
}
