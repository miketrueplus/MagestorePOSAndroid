package com.magestore.app.lib.model.directory;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface Region extends Model {
    String getRegionCode();
    void setRegionCode(String strRegionCode);
    String getRegionName();
    void setRegionName(String strRegionName);
    void setID(String strID);
}
