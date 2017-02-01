package com.magestore.app.lib.resourcemodel.customer;

import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.ChildListDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.ParentListService;

/**
 * Created by Mike on 1/25/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerComplainDataAccess extends DataAccess, ChildListDataAccess<Customer, Complain> {
}
