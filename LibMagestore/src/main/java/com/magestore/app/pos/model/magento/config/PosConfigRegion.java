package com.magestore.app.pos.model.magento.config;

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
    String id;

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String strName) {
        name = strName;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public String getDisplayContent() {
        return name;
    }
}
