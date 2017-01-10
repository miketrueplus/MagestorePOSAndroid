package com.magestore.app.lib.resourcemodel.customer;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.util.List;

/**
 * Giao diện các API trả về danh mục khách hàng customer
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerDataAccess extends DataAccess {
    List<Customer> getCustomers(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
