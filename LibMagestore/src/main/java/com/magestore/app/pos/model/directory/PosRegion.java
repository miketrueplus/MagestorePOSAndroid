package com.magestore.app.pos.model.directory;

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
    String region_id;

    @Override
    public String getRegionCode() {
        return region_code;
    }

    @Override
    public String getRegionName() {
        return region;
    }

    @Override
    public String getID() {
        return region_id;
    }
}
