package com.magestore.app.lib.service.order;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderHistoryService extends Service {
    List<Order> retrieveOrderList(int size) throws InstantiationException, IllegalAccessException, IOException, ParseException;
}
