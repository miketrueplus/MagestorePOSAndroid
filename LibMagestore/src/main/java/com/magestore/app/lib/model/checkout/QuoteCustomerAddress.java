package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteCustomerAddress extends Model {
    String getCountryId();

    void setCountryId(String strCountryId);

    String getRegionId();

    void setRegionId(String strRegionId);

    String getPostcode();

    void setPostcode(String strPoscode);

    String getStreet();

    void setStreet(String strStreet);

    String getTelephone();

    void setTelephone(String strTelephone);

    String getCity();

    void setCity(String strCity);

    String getFisrtname();

    void setFisrtname(String strFisrtname);

    String getLastname();

    void setLastname(String strLastname);

    String getEmail();

    void setEmail(String strEmail);
}
