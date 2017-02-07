package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.model.config.PosConfigRegion;

import java.util.List;

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
    List<Region> getRegions();
    void setRegions(List<PosConfigRegion> regions);
}
