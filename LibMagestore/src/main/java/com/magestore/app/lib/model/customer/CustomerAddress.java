package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.model.directory.PosRegion;

import java.util.List;

/**
 * Interface của address
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerAddress extends Model {
    void setId(String strId);

    String getFirstName();
    void setFirstName(String strFirstName);

    String getLastName();
    void setLastName(String strLastName);

    String getName();

    String getFullAddress();

    String getCheckoutAddress();

    String getPostCode();
    void setPostCode(String strPostCode);

    String getTelephone();
    void setTelephone(String strTelephone);

    String getCountry();
    void setCountry(String strCountry);

    String getCity();
    void setCity(String strCity);

    String getState();
    void setState(String strState);

    String getProvince();
    void setProvince(String strProvince);

    String getVAT();
    void setVAT(String strState);

    List<String> getStreet();

    String getStreet1();
    void setStreet1(String strStreet1);

    String getStreet2();
    void setStreet2(String strStreet2);

    String getCompany();
    void setCompany(String strCompany);

    String isShipping();
    void setDefaultShipping(String strShipping);

    String isBilling();
    void setDefaultBilling(String strBilling);

    Region getRegion();
    String getRegionCode();
    void setRegionID(String strRegionID);
    void setRegion(Region region);

    void setCustomer(String strCustomerID);
    void setCustomer(Customer customer);

    String getCustomerID();
    Customer getCustomer();

    String getShortAddress();
    void setShortAddress(String strShortAddress);

    boolean getIsStoreAddress();
    void setIsStoreAddress(boolean bIsStoreAddress);

    boolean getUseShippingToCheckout();
    void setUseShippingToCheckout(boolean bUseShippingToCheckout);

    boolean getUseBillingToCheckout();
    void setUseBillingToCheckout(boolean bUseBillingToCheckout);
}
