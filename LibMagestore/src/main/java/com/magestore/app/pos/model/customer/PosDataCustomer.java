package com.magestore.app.pos.model.customer;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.DataCustomer;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataCustomer extends PosAbstractModel implements DataCustomer {
    List<PosCustomer> items;
    int total_count;

    @Override
    public List<Customer> getItems() {
        return (List<Customer>) (List<?>) items;
    }

    @Override
    public int getTotalCount() {
        return total_count;
    }
}
