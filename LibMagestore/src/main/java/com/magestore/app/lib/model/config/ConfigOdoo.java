package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.directory.Currency;

import java.util.Map;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigOdoo extends Model {
    Map<String, ConfigCountry> getCountry();
    void setCountry(Map<String, ConfigCountry> mCountry);
    Map<String, String> getCustomerGroup();
    void setCustomerGroup(Map<String, String> mCustomerGroup);
    ConfigPriceFormat getPriceFormat();
    void setPriceFormat(ConfigPriceFormat mConfigPriceFormat);
    ConfigQuantityFormat getQuantityFormat();
    void setQuantityFormat(ConfigQuantityFormat mQuantityFormat);
    Currency getDefaultCurrency();
    void setDefaultCurrency(Currency mDefaultCurrency);
}
