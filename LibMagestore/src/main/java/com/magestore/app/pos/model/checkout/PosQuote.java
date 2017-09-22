package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuote extends PosAbstractModel implements Quote {
    String quote_id;
    String store_id;
    String customer_id;
    String currency_id;
    String till_id;
    List<QuoteItems> items;
    PosQuoteCustomer customer;
    String shipping_method;
    String shift_id;

    @Override
    public String getID() {
        return quote_id;
    }

    @Override
    public void setID(String id) {
        super.setID(id);
        quote_id = id;
    }

    @Override
    public void setQuoteId(String strId) {
        quote_id = strId;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getCustomerId() {
        return customer_id;
    }

    @Override
    public void setCustomerId(String strCustomerId) {
        customer_id = strCustomerId;
    }

    @Override
    public String getCurrencyId() {
        return currency_id;
    }

    @Override
    public void setCurrencyId(String strCurrencyId) {
        currency_id = strCurrencyId;
    }

    @Override
    public String getTillId() {
        return till_id;
    }

    @Override
    public void setTillId(String strTillId) {
        till_id = strTillId;
    }

    @Override
    public List<QuoteItems> getItems() {
        return items;
    }

    @Override
    public void setItems(List<QuoteItems> items) {
        this.items = items;
    }

    @Override
    public QuoteCustomer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(QuoteCustomer customer) {
        this.customer = (PosQuoteCustomer) customer;
    }

    @Override
    public String getShippingMethod() {
        return shipping_method;
    }

    @Override
    public void setShiftId(String strShiftId) {
        shift_id = strShiftId;
    }
}
