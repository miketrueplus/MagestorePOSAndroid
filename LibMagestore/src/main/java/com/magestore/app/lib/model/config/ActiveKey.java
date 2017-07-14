package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 7/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ActiveKey extends Model {
    String getType();
    void setType(String strType);
    int getExpiredTime();
    void setExpiredTime(int intExpiredTime);
    String getCreatedDate();
    void setCreatedDate(String strCreatedDate);
    String getLicenseDomain();
    void setLicenseDomain(String strLicenseDomain);
}
