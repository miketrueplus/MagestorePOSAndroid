package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface PaymentMethod extends Model {
    String getName();

    void setName(String name);

    void setID(String id);

    void setCashOnDelivery();

    void setCashIn();

    boolean isCreditCardDirect();

    void setCreditCardDirect();

    void setCreditCardIn();

    void setCustomerPayment();
}
