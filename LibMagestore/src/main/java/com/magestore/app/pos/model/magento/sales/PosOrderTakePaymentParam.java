package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.lib.model.sales.OrderTakePaymentParam;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 4/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderTakePaymentParam extends PosAbstractModel implements OrderTakePaymentParam {
    String order_increment_id;
    String order_id;
    PlaceOrderPaymentParam payment;
    String currency_id;

    @Override
    public void setOrderIncrementId(String strOrderIncrementId) {
        order_increment_id = strOrderIncrementId;
    }

    @Override
    public void setOrderId(String strOrderId) {
        order_id = strOrderId;
    }

    @Override
    public void setPayment(PlaceOrderPaymentParam placeOrderPaymentParam) {
        payment = placeOrderPaymentParam;
    }

    @Override
    public PlaceOrderPaymentParam createPlaceOrderPaymentParam() {
        payment = new PlaceOrderPaymentParam();
        return payment;
    }

    @Override
    public List<PaymentMethodDataParam> createPaymentMethodData() {
        payment.method_data = new ArrayList<PaymentMethodDataParam>();
        return payment.method_data;
    }

    @Override
    public void setMethod(String strMethod) {
        payment.method = strMethod;
    }

    @Override
    public void setMethodData(List<PaymentMethodDataParam> methodData) {
        payment.method_data = methodData;
    }

    @Override
    public void setCurrencyId(String strCurrencyId) {
        currency_id = strCurrencyId;
    }

    public class PlaceOrderPaymentParam {
        String method;
        List<PaymentMethodDataParam> method_data;
    }
}
