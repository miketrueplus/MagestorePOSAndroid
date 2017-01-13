package com.magestore.app.pos.api.m2.sales;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListOrder;

import java.io.IOException;
import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSOrderDataAccess extends POSAbstractDataAccess implements OrderDataAccess {
    /**
     * Trả về list order
     * @param pageSize Số customer trên 1 page
     * @param currentPage Trang hiện lại
     * @return Danh sách order
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Order> getOrders(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Gson2PosListOrder listOrder = (Gson2PosListOrder) doAPI(Gson2PosListOrder.class,
                POSAPI.REST_ORDER_GET_DETAIL,
                null,
                POSAPI.PARAM_CURRENT_PAGE, "" + currentPage,
                POSAPI.PARAM_PAGE_SIZE, "" + pageSize,
                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID);
        List<Order> list  = (List<Order>)(List<?>)(listOrder.items);
        return list;
    }
}
