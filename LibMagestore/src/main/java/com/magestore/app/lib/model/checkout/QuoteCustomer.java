package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.checkout.PosQuoteCustomerAddress;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteCustomer extends Model {
    void setCustomerId(String strCustomerId);

    PosQuoteCustomerAddress getBillingAddress();

    PosQuoteCustomerAddress getShippingAddress();
}
