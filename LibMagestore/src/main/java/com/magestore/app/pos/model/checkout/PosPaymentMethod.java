package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosPaymentMethod extends PosAbstractModel implements PaymentMethod {
    public static String PAYMENT_METHOD_CC_DIRECT_POST = "CC_DIRECT";
    public static String PAYMENT_METHOD_CASH_IN = "CASH_IN";
    public static String PAYMENT_METHOD_CC_CARD = "CC_CARDN";
    public static String PAYMENT_METHOD_CASH_COD = "CASH_ON_DELIVERY";
    public static String PAYMENT_METHOD_CUSTOM_PAYMENT = "CUSTOM_PAYMENT";

    String name;
    String type;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayContent() {
        return name;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    public boolean isCashOnDelivery() {
        return PAYMENT_METHOD_CASH_COD.equals(type);
    }

    @Override
    public void setCashOnDelivery() {
        type = PAYMENT_METHOD_CASH_COD;
    }

    public boolean isCashIn() {
        return PAYMENT_METHOD_CASH_IN.equals(type);
    }

    @Override
    public void setCashIn() {
        type = PAYMENT_METHOD_CASH_IN;
    }

    @Override
    public boolean isCreditCardDirect() {
        return PAYMENT_METHOD_CC_DIRECT_POST.equals(type);
    }

    @Override
    public void setCreditCardDirect() {
        type = PAYMENT_METHOD_CC_DIRECT_POST;
    }

    public boolean isCreditCardIn() {
        return PAYMENT_METHOD_CC_CARD.equals(type);
    }

    @Override
    public void setCreditCardIn() {
        type = PAYMENT_METHOD_CC_CARD;
    }

    public boolean isCustomerPayment() {
        return PAYMENT_METHOD_CUSTOM_PAYMENT.equals(type);
    }

    @Override
    public void setCustomerPayment() {
        type = PAYMENT_METHOD_CUSTOM_PAYMENT;
    }
}
