package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteCustomer extends PosAbstractModel implements QuoteCustomer {
    String customer_id;
    PosQuoteCustomerAddress billing_address;
    PosQuoteCustomerAddress shipping_address;

    @Override
    public void setCustomerId(String strCustomerId) {
        id = customer_id;
    }

    @Override
    public PosQuoteCustomerAddress getBillingAddress() {
        return billing_address;
    }

    @Override
    public PosQuoteCustomerAddress getShippingAddress() {
        return shipping_address;
    }
}
