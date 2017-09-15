package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 9/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataCustomer extends Model {
    List<Customer> getItems();
    int getTotalCount();
}
