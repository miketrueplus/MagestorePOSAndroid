package com.magestore.app.pos.api.m2.customer;


import android.util.Log;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.resourcemodel.customer.CustomerDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListCustomer;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;

import java.io.IOException;
import java.util.List;

/**
 * Các API về customer
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerDataAccess extends POSAbstractDataAccess implements CustomerDataAccess {
    /**
     * Trả về list customer
     * @param pageSize Số customer trên 1 page
     * @param currentPage Trang hiện lại
     * @return Danh sách customer
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Customer> getCustomers(int pageSize, int currentPage) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Gson2PosListCustomer listCustomer = (Gson2PosListCustomer) doAPI(Gson2PosListCustomer.class,
                POSAPI.REST_CUSOMTER_GET_LISTING,
                null,
                POSAPI.PARAM_CURRENT_PAGE, "" + currentPage,
                POSAPI.PARAM_PAGE_SIZE, "" + pageSize,
                POSAPI.PARAM_SESSION_ID, POSDataAccessSession.REST_SESSION_ID
        );
        List<Customer> list = (List<Customer>) (List<?>) (listCustomer.items);
        Log.e("Customer list", list.size() + "");
        return list;
    }
}
