package com.magestore.app.lib.entity.pos;

import com.google.gson.annotations.SerializedName;
import com.magestore.app.lib.adapterview.AdapterViewAnnotiation;
import com.magestore.app.lib.entity.Address;
import com.magestore.app.lib.entity.Region;
import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý address
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosAddress extends AbstractEntity implements Address {
    String customer_id;
    String region_id;
    String country_id;
    List<String> street;
    PosRegion region;
    String telephone;
    String postcode;
    String city;
    @AdapterViewAnnotiation(resName = "fullname")
    String firstname;
    String lastname;
    String vat_id;
    String company;
    String state;
    boolean default_shipping;
    boolean default_billing;

    @Override
    public String getName() {
        return firstname + " " + lastname;
    }

    @Override
    public String getFullAddress() {
        if (street == null) return "";
        StringBuilder builder = new StringBuilder();
        for (String streetline : street) {
            if (streetline != null)
                builder.append(streetline);
        }
        builder.append(", ");
        builder.append(city);
        builder.append(", ");
        builder.append(country_id);
        return builder.toString();
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
    public String getTelephone() {
        return telephone;
    }
    @Override
    public void setTelephone(String strTelephone) {
        telephone = strTelephone;
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
    public String getFirstName() {
        return firstname;
    }
    @Override
    public void setFirstName(String strFirstName) {
        firstname = strFirstName;
    }

    @Override
    public String getRegionCode() {
        if (region == null) return null;
        return region.getRegionCode();
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
    public String getState() {
        return getRegionCode();
    }

    @Override
    public String getProvince() {
        return getRegionCode();
    }

    @Override
    public String getVAT() {
        return vat_id;
    }
    @Override
    public void setVAT(String strVat) {
        vat_id = strVat;
    }

    @Override
    public String getStreet1() {
        if (street == null) return "";
        return street.get(0);
    }
    @Override
    public void setStreet1(String strStreet1) {
        if (street == null) street = new ArrayList<String>();
        if (street.size() == 0) street.add(strStreet1);
        street.set(0, strStreet1);
    }

    @Override
    public void setStreet2(String strStreet2) {
        if (street == null) street = new ArrayList<String>();
        if (street.size() == 0) street.add("");
        if (street.size() == 1) street.add(strStreet2);
        else street.set(1, strStreet2);
    }

    @Override
    public String getStreet2() {
        if (street == null) return "";
        if (street.size() <= 1) return "";
        return street.get(1);
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
    public boolean isShipping() {
        return default_shipping;
    }
    @Override
    public void setDefaultShipping(boolean blnShipping) {
        default_shipping = blnShipping;
    }

    @Override
    public boolean isBilling() {
        return default_billing;
    }
    @Override
    public void setDefaultBilling(boolean blnBilling) {
        default_billing = blnBilling;
    }

    @Override
    public Region getRegion() {
        return region;
    }


}
