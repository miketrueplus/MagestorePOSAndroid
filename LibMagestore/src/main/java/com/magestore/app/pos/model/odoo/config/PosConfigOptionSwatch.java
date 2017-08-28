package com.magestore.app.pos.model.odoo.config;

import com.magestore.app.lib.model.config.ConfigOptionSwatch;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 7/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigOptionSwatch extends PosAbstractModel implements ConfigOptionSwatch {
    String swatch_id;
    String option_id;
    String store_id;
    String type;
    String value;

    @Override
    public String getID() {
        return option_id;
    }

    @Override
    public String getSwatchId() {
        return swatch_id;
    }

    @Override
    public void setSwatchId(String strSwatchId) {
        swatch_id = strSwatchId;
    }

    @Override
    public void setOptionId(String strOptionId) {
        option_id = strOptionId;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String strType) {
        type = strType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String strValue) {
        value = strValue;
    }
}
