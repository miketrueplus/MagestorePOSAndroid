package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.lib.model.checkout.PlaceOrderExtensionParam;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationParam;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 2/15/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosPlaceOrderParams extends PosAbstractModel implements PlaceOrderParams {
    String quote_id;
    PlaceOrderActionParam actions;
    PlaceOrderQuoteDataParam quote_data;
    PlaceOrderPaymentParam payment;
    List<PlaceOrderExtensionParam> extension_data;
    List<PlaceOrderIntegrationParam> integration;
    String shipping_method = "";
    String currency_id;
    String customer_id;
    String store_id;
    String till_id;

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public void setIntegration(List<PlaceOrderIntegrationParam> placeOrderIntegration) {
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
    public void setDeliveryTime(String strDeliveryTime) {
        actions.delivery_time = strDeliveryTime;
    }

    @Override
    public String getMethod() {
        return payment.method;
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
    public void setPlaceOrderExtensionData(List<PlaceOrderExtensionParam> listExtension) {
        extension_data = listExtension;
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
    public List<PaymentMethodDataParam> createPaymentMethodData() {
        payment.method_data = new ArrayList<PaymentMethodDataParam>();
        return payment.method_data;
    }

    @Override
    public void setCurrencyId(String strCurrencyId) {
        currency_id = strCurrencyId;
    }

    @Override
    public void setCustomerId(String strCustomerId) {
        customer_id = strCustomerId;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public void setTillId(String strTillId) {
        till_id = strTillId;
    }

    public class PlaceOrderActionParam {
        String create_invoice;
        String create_shipment;
        String delivery_time;
    }

    public class PlaceOrderQuoteDataParam {
        String customer_note;
    }

    public class PlaceOrderPaymentParam {
        String method;
        List<PaymentMethodDataParam> method_data;
    }
}
