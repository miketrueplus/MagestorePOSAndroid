package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteCustomerAddress extends Model {
    String getCountryId();

    void setCountryId(String strCountryId);

    int getRegionId();

    void setRegionId(int intRegionId);

    String getPostcode();

    void setPostcode(String strPoscode);

    List<String> getStreet();

    void setStreet(List<String> strStreet);

    String getTelephone();

    void setTelephone(String strTelephone);

    String getCity();

    void setCity(String strCity);

    String getFirstname();

    void setFirstname(String strFirstname);

    String getLastname();

    void setLastname(String strLastname);

    String getEmail();

    void setEmail(String strEmail);
}
