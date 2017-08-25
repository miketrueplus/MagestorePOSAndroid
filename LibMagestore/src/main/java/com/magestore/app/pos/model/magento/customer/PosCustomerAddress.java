package com.magestore.app.pos.model.magento.customer;

import android.text.TextUtils;

import com.magestore.app.lib.adapter.AdapterViewAnnotiation;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.magento.directory.PosRegion;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Quản lý address
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCustomerAddress extends PosAbstractModel implements CustomerAddress {
    @Gson2PosExclude
    Customer customer;

    String customer_id;
    String region_id;
    String country_id;
    List<String> street;
    PosRegion region;
    String telephone;
    String postcode;
    String city;
    String firstname;
    String lastname;
    String vat_id;
    String company;
    String state;
    String default_shipping;
    String default_billing;

    @Gson2PosExclude
    String short_address;
    @Gson2PosExclude
    boolean isStoreAddress;
    @Gson2PosExclude
    boolean use_shipping_to_checkout;
    @Gson2PosExclude
    boolean use_billing_to_checkout;

    @Override
    public String getID() {
        if (super.getID() == null) {
            return "" + this.hashCode();
        } else {
            return super.getID();
        }
    }

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
    public String getCheckoutAddress() {
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(getStreet1())) {
            builder.append(getStreet1());
            builder.append(", ");
        }
        if (!TextUtils.isEmpty(getStreet2())) {
            builder.append(getStreet2());
            builder.append(", ");
        }
        builder.append(getCity());
        builder.append(", ");
        if (getRegion() != null) {
            String state = getRegion().getRegionName();
            if (!TextUtils.isEmpty(state)) {
                builder.append(state);
                builder.append(", ");
            }
        }
        builder.append(getCountry());
        if (!TextUtils.isEmpty(getPostCode())) {
            builder.append(", ");
            builder.append(getPostCode());
        }
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
    public void setId(String strId) {
        id = strId;
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
    public void setRegionID(String strRegionID) {
        region_id = strRegionID;
    }

    @Override
    public void setRegion(Region region) {
        this.region = (PosRegion) region;
    }

    @Override
    public void setCustomer(String strCustomerID) {
        customer_id = strCustomerID;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
        setCustomer(customer.getID());
    }

    @Override
    public String getCustomerID() {
        return customer_id;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public String getShortAddress() {
        if(short_address != null && !StringUtil.STRING_EMPTY.equals(short_address)){
            return short_address;
        }
        String fullName = getFirstName() + " " + getLastName();
        StringBuilder builder = new StringBuilder();
        builder.append(fullName);
        builder.append(", ");
        if (!TextUtils.isEmpty(getStreet1())) {
            builder.append(getStreet1());
            builder.append(", ");
        }
        if (!TextUtils.isEmpty(getStreet2())) {
            builder.append(getStreet2());
            builder.append(", ");
        }
        builder.append(getCity());
        builder.append(", ");
        if (getRegion() != null) {
            String state = getRegion().getRegionName();
            if (!TextUtils.isEmpty(state)) {
                builder.append(state);
                builder.append(", ");
            }
        }
        builder.append(getCountry());
        builder.append(", ");
        builder.append(getPostCode());
        builder.append(", ");
        builder.append(getTelephone());
        short_address = builder.toString();
        return short_address;
    }

    @Override
    public void setShortAddress(String strShortAddress) {
        short_address = strShortAddress;
    }

    @Override
    public boolean getIsStoreAddress() {
        return isStoreAddress;
    }

    @Override
    public void setIsStoreAddress(boolean bIsStoreAddress) {
        isStoreAddress = bIsStoreAddress;
    }

    @Override
    public boolean getUseShippingToCheckout() {
        return use_shipping_to_checkout;
    }

    @Override
    public void setUseShippingToCheckout(boolean bUseShippingToCheckout) {
        use_shipping_to_checkout = bUseShippingToCheckout;
    }

    @Override
    public boolean getUseBillingToCheckout() {
        return use_billing_to_checkout;
    }

    @Override
    public void setUseBillingToCheckout(boolean bUseBillingToCheckout) {
        use_billing_to_checkout = bUseBillingToCheckout;
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
    public void setState(String strState) {
//        this.region_id = strState;
    }

    @Override
    public String getProvince() {
        return getRegionCode();
    }

    @Override
    public void setProvince(String strProvince) {
//        this.region_id = strProvince;
    }

    @Override
    public String getVAT() {
        return vat_id;
    }

    @Override
    public void setVAT(String strVat) {
        vat_id = strVat;
    }

    @AdapterViewAnnotiation(resName = "street1", methodType = AdapterViewAnnotiation.MethodType.GET)
    @Override
    public String getStreet1() {
        if (street == null) return "";
        return street.get(0);
    }

    @AdapterViewAnnotiation(resName = "street1", methodType = AdapterViewAnnotiation.MethodType.SET)
    @Override
    public void setStreet1(String strStreet1) {
        if (street == null) street = new ArrayList<String>();
        if (street.size() == 0) street.add(strStreet1);
        street.set(0, strStreet1);
    }

    @AdapterViewAnnotiation(resName = "street2", methodType = AdapterViewAnnotiation.MethodType.SET)
    @Override
    public void setStreet2(String strStreet2) {
        if (street == null) street = new ArrayList<String>();
        if (street.size() == 0) street.add("");
        if (street.size() == 1) street.add(strStreet2);
        else street.set(1, strStreet2);
    }

    @AdapterViewAnnotiation(resName = "street2", methodType = AdapterViewAnnotiation.MethodType.GET)
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
    public String isShipping() {
        return default_shipping;
    }

    @Override
    public void setDefaultShipping(String strShipping) {
        default_shipping = strShipping;
    }

    @Override
    public String isBilling() {
        return default_billing;
    }

    @Override
    public void setDefaultBilling(String strBilling) {
        default_billing = strBilling;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public String getDisplayContent() {
        return getShortAddress();
    }
}
