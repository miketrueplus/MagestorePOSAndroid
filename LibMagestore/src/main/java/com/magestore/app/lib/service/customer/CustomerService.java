package com.magestore.app.lib.service.customer;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.PlaceAddressComponent;
import com.magestore.app.lib.model.customer.PlaceAutoComplete;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.ParentListService;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerService extends ListService<Customer> {
//    void updateAddress(Customer mSelectedCustomer,CustomerAddress oldAddress, CustomerAddress newAddress);
//    void insertAddress(Customer mSelectedCustomer,CustomerAddress insertAddress);
//    void deleteAddress(Customer mSelectedCustomer,CustomerAddress deleteAddress);
    List<PlaceAutoComplete> placeAutoComplete(String input) throws ParseException, InstantiationException, IllegalAccessException, IOException;
    List<PlaceAddressComponent> placeDetail(String detailId) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}
