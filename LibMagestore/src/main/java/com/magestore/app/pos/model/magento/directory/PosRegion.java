package com.magestore.app.pos.model.magento.directory;

import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Quản lý region của Magestore
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosRegion extends PosAbstractModel implements Region {
    String region_code;
    String region;
    int region_id;

    @Override
    public String getRegionCode() {
        return region_code;
    }

    @Override
    public void setRegionCode(String strRegionCode) {
        region_code = strRegionCode;
    }

    @Override
    public String getRegionName() {
        return region;
    }

    @Override
    public void setRegionName(String strRegionName) {
        region = strRegionName;
    }

    @Override
    public int getRegionID() {
        return region_id;
    }

    @Override
    public void setRegionID(int intID) {
        region_id = intID;
    }
}
