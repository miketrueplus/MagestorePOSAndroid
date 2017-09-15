package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderBillingAddress;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.util.StringUtil;

import java.util.List;

/**
 * Quản lý order billing address
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderBillingAddress extends PosAbstractModel implements OrderBillingAddress {
    String address_type;
    String city;
    String country_id;
    String customer_address_id;
    String email;
    String entity_id;
    String firstname;
    String lastname;
    String parent_id;
    String postcode;
    List<String> street;
    String telephone;
    String region;
    String region_code;
    String region_id;
    String company;
    String vat_id;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public void setID(String id) {
        super.setID(id);
        entity_id = id;
    }

    @Override
    public String getAddressType() {
        return address_type;
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
    public String getFirstName() {
        return firstname;
    }

    @Override
    public void setFirstName(String strFirstName) {
        firstname = strFirstName;
    }

    @Override
    public String getLastName() {
        return lastname;
    }

    @Override
    public void setLastName(String strLastName) {
        lastname = strLastName;
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
    public String getName() {
        return firstname + " " + lastname;
    }

    @Override
    public String getFullAddress() {
        StringBuilder builder = new StringBuilder();
        if (!StringUtil.isNullOrEmpty(city)) {
            builder.append(city);
            builder.append(", ");
        }
        if (!StringUtil.isNullOrEmpty(region)) {
            builder.append(region);
            builder.append(", ");
        }
        if (!StringUtil.isNullOrEmpty(postcode)) {
            builder.append(postcode);
            builder.append(", ");
        }
        if (!StringUtil.isNullOrEmpty(country_id)) {
            builder.append(country_id);
        }
        return builder.toString();
    }

    @Override
    public String getCountry() {
        return country_id;
    }

    @Override
    public void setCountry(String strCountry) {
        country_id = strCountry;
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
    public List<String> getStreet() {
        return street;
    }

    @Override
    public void setStreet(List<String> listStreet) {
        street = listStreet;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public void setRegion(String strRegion) {
        region = strRegion;
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
    public String getRegionCode() {
        return region_code;
    }

    @Override
    public void setRegionCode(String strRegionCode) {
        region_code = strRegionCode;
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
    public String getCompany() {
        return company;
    }

    @Override
    public void setCompany(String strCompany) {
        company = strCompany;
    }

    @Override
    public String getVat() {
        return vat_id;
    }

    @Override
    public void setVat(String strVat) {
        vat_id = strVat;
    }
}
