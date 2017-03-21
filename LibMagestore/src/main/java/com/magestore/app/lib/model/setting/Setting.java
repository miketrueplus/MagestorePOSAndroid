package com.magestore.app.lib.model.setting;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Setting extends Model {
    String getName();
    void setName(String strName);

    int getType();
    void setType(int intType);
}
