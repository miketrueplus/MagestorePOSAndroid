package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.directory.Region;

import java.util.List;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigCountry extends Model {
    String getCountryID();
    String getName();
    List<Region> getRegions();
}
