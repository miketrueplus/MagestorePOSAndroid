package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceOrderParams extends PosAbstractModel implements PlaceOrderParams {
    String quote_id;
    PlaceOrderIntegration integration;
    PlaceOrderActionParam actions;
    PlaceOrderQuoteDataParam quote_data;
    PlaceOrderPaymentParam payment;

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public void setIntegration(PlaceOrderIntegration placeOrderIntegration) {
        integration = placeOrderIntegration;
    }

    @Override
    public void setActions(PlaceOrderActionParam placeOrderActionParam) {
        actions = placeOrderActionParam;
    }

    @Override
    public void setQuoteData(PlaceOrderQuoteDataParam placeOrderQuoteDataParam) {
        quote_data = placeOrderQuoteDataParam;
    }

    @Override
    public void setPayment(PlaceOrderPaymentParam placeOrderPaymentParam) {
        payment = placeOrderPaymentParam;
    }

    @Override
    public void setCreateInvoice(String strCreateInvoice) {
        actions.create_invoice = strCreateInvoice;
    }

    @Override
    public void setCreateShipment(String strCreateShipment) {
        actions.create_shipment = strCreateShipment;
    }

    @Override
    public void setCustomerNote(String strCustomerNote) {
        quote_data.customer_note = strCustomerNote;
    }

    @Override
    public void setMethod(String strMethod) {
        payment.method = strMethod;
    }

    @Override
    public void setMethodData(PaymentMethodDataParam methodData) {
        payment.method_data = methodData;
    }

    @Override
    public void setReferenceNumber(String strReferenceNumber) {
        payment.method_data.reference_number = strReferenceNumber;
    }

    @Override
    public void setAmount(float fAmount) {
        payment.method_data.amount = fAmount;
    }

    @Override
    public void setBaseAmount(float fBaseAmount) {
        payment.method_data.base_amount = fBaseAmount;
    }

    @Override
    public void setBaseRealAmount(float fBaseRealAmount) {
        payment.method_data.base_real_amount = fBaseRealAmount;
    }

    @Override
    public void setCode(String strCode) {
        payment.method_data.code = strCode;
    }

    @Override
    public void setIsPayLater(String strIsPayLater) {
        payment.method_data.is_pay_later = strIsPayLater;
    }

    @Override
    public void setRealAmount(float fRealAmount) {
        payment.method_data.real_amount = fRealAmount;
    }

    @Override
    public void setTitle(String strTitle) {
        payment.method_data.title = strTitle;
    }

    @Override
    public PlaceOrderActionParam createPlaceOrderActionParam() {
        actions = new PlaceOrderActionParam();
        return actions;
    }

    @Override
    public PlaceOrderQuoteDataParam createPlaceOrderQuoteDataParam() {
        quote_data = new PlaceOrderQuoteDataParam();
        return quote_data;
    }

    @Override
    public PlaceOrderPaymentParam createPlaceOrderPaymentParam() {
        payment = new PlaceOrderPaymentParam();
        return payment;
    }

    @Override
    public PaymentMethodDataParam createPaymentMethodData() {
        payment.method_data = new PaymentMethodDataParam();
        return payment.method_data;
    }

    @Override
    public PlaceOrderIntegration createPlaceOrderIntegration() {
        integration = new PlaceOrderIntegration();
        return integration;
    }

    public class PlaceOrderIntegration {

    }

    public class PlaceOrderActionParam {
        String create_invoice;
        String create_shipment;
    }

    public class PlaceOrderQuoteDataParam {
        String customer_note;
    }

    public class PlaceOrderPaymentParam {
        String method;
        PaymentMethodDataParam method_data;
    }

    public class PaymentMethodDataParam {
        PaymentMethodAdditionalParam additional_data;
        String reference_number;
        float amount;
        float base_amount;
        float base_real_amount;
        String code;
        String is_pay_later;
        float real_amount;
        String title;
    }

    public class PaymentMethodAdditionalParam {

    }
}
