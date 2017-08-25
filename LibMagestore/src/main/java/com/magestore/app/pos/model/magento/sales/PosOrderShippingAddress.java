package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderShippingAddress;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Quản lý order shipping address
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShippingAddress extends PosAbstractModel implements OrderShippingAddress {
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
//    List<String> street;
    String telephone;
    String region;
    String region_code;
    String region_id;

    @Override
    public String getID() {
        return entity_id;
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
        builder.append(city);
        builder.append(", ");
        builder.append(region);
        builder.append(", ");
        builder.append(postcode);
        builder.append(", ");
        builder.append(country_id);
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
        return null;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public String getRegionId() {
        return region_id;
    }

    @Override
    public String getRegionCode() {
        return region_code;
    }

    @Override
    public String getTelephone() {
        return telephone;
    }

    @Override
    public void setTelephone(String strTelephone) {
        telephone = strTelephone;
    }
}
