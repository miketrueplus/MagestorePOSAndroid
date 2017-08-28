package com.magestore.app.pos.model.odoo.config;

import com.magestore.app.lib.model.config.ConfigOptionSwatch;
import com.magestore.app.lib.model.config.ConfigProductOption;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.Map;

/**
 * Created by Johan on 7/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigProductOption extends PosAbstractModel implements ConfigProductOption {
    String attribute_id;
    String attribute_code;
    String attribute_label;
    Map<String, ConfigOptionSwatch> swatches;

    @Override
    public String getAttributeId() {
        return attribute_id;
    }

    @Override
    public void setAttributeId(String strAttributeId) {
        attribute_id = strAttributeId;
    }

    @Override
    public String getAttributeCode() {
        return attribute_code;
    }

    @Override
    public void setAttributeCode(String strAttributeCode) {
        attribute_code = strAttributeCode;
    }

    @Override
    public String getAttributeLabel() {
        return attribute_label;
    }

    @Override
    public void setAttributeLabel(String strAttributeLabel) {
        attribute_label = strAttributeLabel;
    }

    @Override
    public Map<String, ConfigOptionSwatch> getColorSwatch() {
        return swatches;
    }

    @Override
    public void setColorSwatch(Map<String, ConfigOptionSwatch> mColorSwatch) {
        swatches = mColorSwatch;
    }
}
