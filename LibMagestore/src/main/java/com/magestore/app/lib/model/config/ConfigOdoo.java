package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

import java.util.Map;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigOdoo extends Model {
    Map<String, ConfigCountry> getCountry();
    void setCountry(Map<String, ConfigCountry> mCountry);
}
