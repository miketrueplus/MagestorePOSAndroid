package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfigCountry extends PosAbstractModel implements ConfigCountry {
    String country_id;
    String country_name;
    List<PosConfigRegion> regions;

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
    public List<Region> getRegions() {
        return (List<Region>)(List<?>) (regions);
    }

    @Override
    public void setRegions(List<PosConfigRegion> regions) {
        this.regions = regions;
    }

    @Override
    public String getDisplayContent() {
        return country_name;
    }
}
