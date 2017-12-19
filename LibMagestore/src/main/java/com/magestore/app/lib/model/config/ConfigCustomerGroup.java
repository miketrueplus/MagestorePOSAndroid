package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigCustomerGroup extends Model {
    String getCode();
    void setCode(String strCode);
    String getTaxClassId();
    void setTaxClassId(String strTaxClassId);
}
