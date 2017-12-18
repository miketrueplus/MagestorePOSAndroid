package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigTaxRates;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 12/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigTaxRates extends PosAbstractModel implements ConfigTaxRates {
    String country;
    String region_id;
    String postcode;
    String rate;
    String zip_is_range;
    String zip_from;
    String zip_to;

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String strCountry) {
        country = strCountry;
    }

    @Override
    public String getRegionId() {
        return region_id;
    }

    @Override
    public void setRegionId(String strRegionId) {
        region_id = strRegionId;
    }

    @Override
    public String getPostCode() {
        return postcode;
    }

    @Override
    public void setPostCode(String strPostCode) {
        postcode = strPostCode;
    }

    @Override
    public float getRate() {
        return Float.parseFloat(rate);
    }

    @Override
    public void setRate(String strRate) {
        rate = strRate;
    }

    @Override
    public String getZipIsRange() {
        return zip_is_range;
    }

    @Override
    public void setZipIsRange(String strZipIsRange) {
        zip_is_range = strZipIsRange;
    }

    @Override
    public String getZipFrom() {
        return zip_from;
    }

    @Override
    public void setZipFrom(String strZipFrom) {
        zip_from = strZipFrom;
    }

    @Override
    public String getZipTo() {
        return zip_to;
    }

    @Override
    public void setZipTo(String strZipTo) {
        zip_to = strZipTo;
    }
}
