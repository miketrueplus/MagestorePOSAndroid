package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.directory.PosRegion;

import java.util.List;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteCustomerAddress extends PosAbstractModel implements QuoteCustomerAddress {
    String country_id;
    int region_id;
    PosRegion region;
    String postcode;
    List<String> street;
    String telephone;
    String city;
    String firstname;
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
    public int getRegionId() {
        return region_id;
    }

    @Override
    public void setRegionId(int intRegionId) {
        region_id = intRegionId;
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
    public List<String> getStreet() {
        return street;
    }

    @Override
    public void setStreet(List<String> strStreet) {
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
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String strFirstname) {
        firstname = strFirstname;
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

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public void setRegion(Region strRegion) {
        region = (PosRegion) strRegion;
    }
}
