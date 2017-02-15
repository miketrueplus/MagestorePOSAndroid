package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteCustomer extends Model {
    void setCustomerId(String strCustomerId);

    QuoteCustomerAddress getBillingAddress();
    void setBillingAddress(QuoteCustomerAddress billingAddress);

    QuoteCustomerAddress getShippingAddress();
    void setShippingAddress(QuoteCustomerAddress shippingAddress);
}
