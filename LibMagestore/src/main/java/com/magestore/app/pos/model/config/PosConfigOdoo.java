package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOdoo;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.Map;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigOdoo extends PosAbstractModel implements ConfigOdoo {
    Map<String, ConfigCountry> country;

    @Override
    public Map<String, ConfigCountry> getCountry() {
        return country;
    }

    @Override
    public void setCountry(Map<String, ConfigCountry> mCountry) {
        country = mCountry;
    }
}
