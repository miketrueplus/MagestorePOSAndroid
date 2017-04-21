package com.magestore.app.lib.model.setting;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.directory.Currency;

/**
 * Created by Johan on 4/17/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ChangeCurrency extends Model {
    ConfigPriceFormat getPriceFormat();
    Currency getCurrency();
    void setCurrency(Currency currency);
}
