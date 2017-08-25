package com.magestore.app.pos.model.magento.config;

import com.magestore.app.lib.model.config.ActiveKey;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 7/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosActiveKey extends PosAbstractModel implements ActiveKey {
    String type;
    int expired_time;
    String created_date;
    String license_domain;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String strType) {
        type = strType;
    }

    @Override
    public int getExpiredTime() {
        return expired_time;
    }

    @Override
    public void setExpiredTime(int intExpiredTime) {
        expired_time = intExpiredTime;
    }

    @Override
    public String getCreatedDate() {
        return created_date;
    }

    @Override
    public void setCreatedDate(String strCreatedDate) {
        created_date = strCreatedDate;
    }

    @Override
    public String getLicenseDomain() {
        return license_domain;
    }

    @Override
    public void setLicenseDomain(String strLicenseDomain) {
        license_domain = strLicenseDomain;
    }
}
