package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 7/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigOptionSwatch extends Model {
    String getSwatchId();
    void setSwatchId(String strSwatchId);
    void setOptionId(String strOptionId);
    String getStoreId();
    void setStoreId(String strStoreId);
    String getType();
    void setType(String strType);
    String getValue();
    void setValue(String strValue);
}
