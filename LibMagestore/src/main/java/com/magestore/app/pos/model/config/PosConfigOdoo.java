package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOdoo;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigQuantityFormat;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.staff.PosStaff;

import java.util.Map;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigOdoo extends PosAbstractModel implements ConfigOdoo {
    Map<String, ConfigCountry> country;
    Map<String, String> customer_group;
    PosConfigPriceFormat priceFormat;
    PosConfigQuantityFormat quantityFormat;
    PosCurrency default_currency;
    PosStaff staff;
    PosCustomer guest_customer;

    @Override
    public Map<String, ConfigCountry> getCountry() {
        return country;
    }

    @Override
    public void setCountry(Map<String, ConfigCountry> mCountry) {
        country = mCountry;
    }

    @Override
    public Map<String, String> getCustomerGroup() {
        return customer_group;
    }

    @Override
    public void setCustomerGroup(Map<String, String> mCustomerGroup) {
        customer_group = mCustomerGroup;
    }

    @Override
    public ConfigPriceFormat getPriceFormat() {
        return priceFormat;
    }

    @Override
    public void setPriceFormat(ConfigPriceFormat mConfigPriceFormat) {
        priceFormat = (PosConfigPriceFormat) mConfigPriceFormat;
    }

    @Override
    public ConfigQuantityFormat getQuantityFormat() {
        return quantityFormat;
    }

    @Override
    public void setQuantityFormat(ConfigQuantityFormat mQuantityFormat) {
        quantityFormat = (PosConfigQuantityFormat) mQuantityFormat;
    }

    @Override
    public Currency getDefaultCurrency() {
        return default_currency;
    }

    @Override
    public void setDefaultCurrency(Currency mDefaultCurrency) {
        default_currency = (PosCurrency) mDefaultCurrency;
    }

    @Override
    public Staff getStaff() {
        return staff;
    }

    @Override
    public void setStaff(Staff mStaff) {
        staff = (PosStaff) mStaff;
    }

    @Override
    public Customer getGuestCustomer() {
        return guest_customer;
    }

    @Override
    public void setGuestCustomer(Customer mGuestCustomer) {
        guest_customer = (PosCustomer) mGuestCustomer;
    }
}
