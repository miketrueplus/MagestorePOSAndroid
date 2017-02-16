package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.checkout.PosPlaceOrderParams;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceOrderParams extends Model {
    void setQuoteId(String strQuoteId);
    void setIntegration(PosPlaceOrderParams.PlaceOrderIntegration placeOrderIntegration);
    void setActions(PosPlaceOrderParams.PlaceOrderActionParam placeOrderActionParam);
    void setQuoteData(PosPlaceOrderParams.PlaceOrderQuoteDataParam placeOrderQuoteDataParam);
    void setPayment(PosPlaceOrderParams.PlaceOrderPaymentParam placeOrderPaymentParam);
    void setCreateInvoice(String strCreateInvoice);
    void setCreateShipment(String strCreateShipment);
    void setCustomerNote(String strCustomerNote);
    void setMethod(String strMethod);
    void setMethodData(PosPlaceOrderParams.PaymentMethodDataParam methodData);
    void setReferenceNumber(String strReferenceNumber);
    void setAmount(float fAmount);
    void setBaseAmount(float fBaseAmount);
    void setBaseRealAmount(float fBaseRealAmount);
    void setCode(String strCode);
    void setIsPayLater(String strIsPayLater);
    void setRealAmount(float fRealAmount);
    void setTitle(String strTitle);
    PosPlaceOrderParams.PlaceOrderActionParam createPlaceOrderActionParam();
    PosPlaceOrderParams.PlaceOrderQuoteDataParam createPlaceOrderQuoteDataParam();
    PosPlaceOrderParams.PlaceOrderPaymentParam createPlaceOrderPaymentParam();
    PosPlaceOrderParams.PaymentMethodDataParam createPaymentMethodData();
    PosPlaceOrderParams.PlaceOrderIntegration createPlaceOrderIntegration();
}
