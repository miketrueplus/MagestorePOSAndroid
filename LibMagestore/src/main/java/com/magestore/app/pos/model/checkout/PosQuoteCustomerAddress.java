package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteCustomerAddress extends PosAbstractModel implements QuoteCustomerAddress {
    String country_id;
    String region_id;
    String postcode;
    String street;
    String telephone;
    String city;
    String fisrtname;
    String lastname;
    String email;

    @Override
    public String getCountryId() {
        return country_id;
    }

    @Override
    public void setCountryId(String strCountryId) {
        country_id = strCountryId;
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
    public String getPostcode() {
        return postcode;
    }

    @Override
    public void setPostcode(String strPoscode) {
        postcode = strPoscode;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String strStreet) {
        street = strStreet;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    @Override
    public void setTelephone(String strTelephone) {
        telephone = strTelephone;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String strCity) {
        city = strCity;
    }

    @Override
    public String getFisrtname() {
        return fisrtname;
    }

    @Override
    public void setFisrtname(String strFisrtname) {
        fisrtname = strFisrtname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String strLastname) {
        lastname = strLastname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String strEmail) {
        email = strEmail;
    }
}
