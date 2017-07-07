package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

import java.util.Map;

/**
 * Created by Johan on 7/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigProductOption extends Model {
    String getAttributeId();
    void setAttributeId(String strAttributeId);
    String getAttributeCode();
    void setAttributeCode(String strAttributeCode);
    String getAttributeLabel();
    void setAttributeLabel(String strAttributeLabel);
    Map<String, ConfigOptionSwatch> getColorSwatch();
    void setColorSwatch(Map<String, ConfigOptionSwatch> mColorSwatch);
}
