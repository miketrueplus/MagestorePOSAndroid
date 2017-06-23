package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 5/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigTaxClass extends Model {
    void setClassID(String strClassID);
    String getClassName();
    String getClassType();
    void setClassName(String strClassName);
    void setClassType(String strClassType);
}
