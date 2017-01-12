package com.magestore.app.lib.model.order;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;

/**
 * Interface của Order Billing address
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderBillingAddress extends CustomerAddress {
    String getAddressType();

    String getEmail();

}
