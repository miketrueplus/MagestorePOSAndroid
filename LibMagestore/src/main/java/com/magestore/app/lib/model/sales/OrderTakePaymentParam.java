package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.pos.model.sales.PosOrderTakePaymentParam;

import java.util.List;

/**
 * Created by Johan on 4/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderTakePaymentParam extends Model {
    void setOrderIncrementId(String strOrderIncrementId);
    void setOrderId(String strOrderId);
    void setPayment(PosOrderTakePaymentParam.PlaceOrderPaymentParam placeOrderPaymentParam);
    PosOrderTakePaymentParam.PlaceOrderPaymentParam createPlaceOrderPaymentParam();
    List<PaymentMethodDataParam> createPaymentMethodData();
    void setMethod(String strMethod);
    void setMethodData(List<PaymentMethodDataParam> methodData);
    void setCurrencyId(String strCurrencyId);
}
