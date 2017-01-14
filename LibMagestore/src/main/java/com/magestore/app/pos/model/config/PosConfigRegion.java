package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfigRegion extends PosAbstractModel implements ConfigRegion {
    String code;
    String name;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
