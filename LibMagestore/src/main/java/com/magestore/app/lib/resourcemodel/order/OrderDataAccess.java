package com.magestore.app.lib.resourcemodel.order;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.order.Order;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.parse.ParseException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderDataAccess extends DataAccess {
    List<Order> getOrders(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
