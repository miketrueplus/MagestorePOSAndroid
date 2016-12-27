package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.gateway.CustomerGateway;
import com.magestore.app.lib.gateway.GatewayException;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.parse.gson2pos.Gson2PosListCustomer;

import java.io.IOException;
import java.util.List;

/**
 * Các API về customer
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCustomerGateway extends POSAbstractGateway implements CustomerGateway {
    /**
     * Trả về list customer
     * @param pageSize Số customer trên 1 page
     * @param currentPage Trang hiện lại
     * @return Danh sách customer
     * @throws GatewayException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public List<Customer> getCustomers(int pageSize, int currentPage) throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException {
        Gson2PosListCustomer listCustomer = (Gson2PosListCustomer) doAPI(Gson2PosListCustomer.class,
                POSAPI.REST_CUSOMTER_GET_LISTING,
                null,
                POSAPI.PARAM_CURRENT_PAGE, "" + currentPage,
                POSAPI.PARAM_PAGE_SIZE, "" + pageSize,
                POSAPI.PARAM_SESSION_ID, POSGatewaySession.REST_SESSION_ID
        );
        List<Customer> list = (List<Customer>) (List<?>) (listCustomer.items);
        return list;
    }
}
