package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 12/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigTaxRates extends Model {
    String getCountry();
    void setCountry(String strCountry);
    String getRegionId();
    void setRegionId(String strRegionId);
    String getPostCode();
    void setPostCode(String strPostCode);
    float getRate();
    void setRate(String strRate);
    String getZipIsRange();
    void setZipIsRange(String strZipIsRange);
    String getZipFrom();
    void setZipFrom(String strZipFrom);
    String getZipTo();
    void setZipTo(String strZipTo);
}
