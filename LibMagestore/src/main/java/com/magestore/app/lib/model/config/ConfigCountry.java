package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.config.PosConfigRegion;

import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigCountry extends Model {
    String getCountryID();
    void setCountryID(String strCountryID);
    String getName();
    void setCountryName(String strCountryName);
    Map<String, ConfigRegion> getRegions();
    void setRegions(Map<String, ConfigRegion> regions);
}
