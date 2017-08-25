package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.magento.checkout.PosPlaceOrderParams;

import java.util.List;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PlaceOrderParams extends Model {
    void setQuoteId(String strQuoteId);
    void setIntegration(List<PlaceOrderIntegrationParam> placeOrderIntegration);
    void setActions(PosPlaceOrderParams.PlaceOrderActionParam placeOrderActionParam);
    void setQuoteData(PosPlaceOrderParams.PlaceOrderQuoteDataParam placeOrderQuoteDataParam);
    void setPayment(PosPlaceOrderParams.PlaceOrderPaymentParam placeOrderPaymentParam);
    void setCreateInvoice(String strCreateInvoice);
    void setCreateShipment(String strCreateShipment);
    void setCustomerNote(String strCustomerNote);
    void setDeliveryTime(String strDeliveryTime);
    String getMethod();
    void setMethod(String strMethod);
    void setMethodData(List<PaymentMethodDataParam> methodData);
    void setPlaceOrderExtensionData(List<PlaceOrderExtensionParam> listExtension);
    PosPlaceOrderParams.PlaceOrderActionParam createPlaceOrderActionParam();
    PosPlaceOrderParams.PlaceOrderQuoteDataParam createPlaceOrderQuoteDataParam();
    PosPlaceOrderParams.PlaceOrderPaymentParam createPlaceOrderPaymentParam();
    List<PaymentMethodDataParam> createPaymentMethodData();
    void setCurrencyId(String strCurrencyId);
    void setCustomerId(String strCustomerId);
    void setStoreId(String strStoreId);
    void setTillId(String strTillId);
}
