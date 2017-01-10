package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.directory.Region;

import java.util.List;

/**
 * Interface của address
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CustomerAddress extends Model {
    String getFirstName();
    void setFirstName(String strFirstName);

    String getLastName();
    void setLastName(String strLastName);

    String getName();

    String getFullAddress();

    String getPostCode();
    void setPostCode(String strPostCode);

    String getTelephone();
    void setTelephone(String strTelephone);

    String getCountry();
    void setCountry(String strCountry);

    String getCity();
    void setCity(String strCity);

    String getState();

    String getProvince();

    String getVAT();
    void setVAT(String strState);

    List<String> getStreet();

    String getStreet1();
    void setStreet1(String strStreet1);

    String getStreet2();
    void setStreet2(String strStreet2);

    String getCompany();
    void setCompany(String strCompany);

    boolean isShipping();
    void setDefaultShipping(boolean blnShipping);

    boolean isBilling();
    void setDefaultBilling(boolean blnBilling);

    Region getRegion();
    String getRegionCode();
}
