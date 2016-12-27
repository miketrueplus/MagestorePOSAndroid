package com.magestore.app.lib.gateway;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.parse.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Giao diện các API trả về danh mục khách hàng customer
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerGateway extends Gateway {
    List<Customer> getCustomers(int pageSize, int currentPage) throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
