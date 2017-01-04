package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.Region;

/**
 * Quản lý region của Magestore
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosRegion extends AbstractEntity implements Region {
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
