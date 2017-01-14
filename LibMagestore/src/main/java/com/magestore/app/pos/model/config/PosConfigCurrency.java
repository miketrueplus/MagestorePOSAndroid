package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigCurrency;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfigCurrency extends PosAbstractModel implements ConfigCurrency {
    String code;
    String currency_name;
    String currency_symbol;
    boolean is_default;
    float currency_rate = (float) 1.0;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return currency_name;
    }

    @Override
    public String getSymbol() {
        return currency_symbol;
    }

    @Override
    public boolean isDefault() {
        return is_default;
    }

    @Override
    public float getRate() {
        return currency_rate;
    }
}
