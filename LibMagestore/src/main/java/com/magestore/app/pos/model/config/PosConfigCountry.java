package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfigCountry extends PosAbstractModel implements ConfigCountry {
    String country_id;
    String country_name;
    Map<String, ConfigRegion> regions;
    String id;

    @Override
    public String getID() {
        return country_id;
    }

    @Override
    public String getCountryID() {
        return country_id;
    }

    @Override
    public void setCountryID(String strCountryID) {
        country_id = strCountryID;
    }

    @Override
    public String getName() {
        return country_name;
    }

    @Override
    public void setCountryName(String strCountryName) {
        country_name = strCountryName;
    }

    @Override
    public Map<String, ConfigRegion> getRegions() {
        return regions;
    }

    @Override
    public void setRegions(Map<String, ConfigRegion> regions) {
        this.regions = regions;
    }

    @Override
    public String getKey() {
        return id;
    }

    @Override
    public void setKey(String strKey) {
        id = strKey;
    }

    @Override
    public String getDisplayContent() {
        return country_name;
    }
}
