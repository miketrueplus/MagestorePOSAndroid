package com.magestore.app.lib.model.checkout;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface PaymentMethod {
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
