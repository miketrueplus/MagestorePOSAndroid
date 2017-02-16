package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteCustomer extends PosAbstractModel implements QuoteCustomer {
    QuoteCustomerAddress billing_address;
    QuoteCustomerAddress shipping_address;

    @Override
    public QuoteCustomerAddress getBillingAddress() {
        return billing_address;
    }

    @Override
    public void setBillingAddress(QuoteCustomerAddress billingAddress) {
        billing_address = billingAddress;
    }

    @Override
    public QuoteCustomerAddress getShippingAddress() {
        return shipping_address;
    }

    @Override
    public void setShippingAddress(QuoteCustomerAddress shippingAddress) {
        shipping_address = shippingAddress;
    }
}
