package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import java.util.List;

/**
 * Interface của Order Billing address
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderBillingAddress extends Model {
    String getAddressType();

    String getEmail();
    void setEmail(String strEmail);

    String getFirstName();
    void setFirstName(String strFirstName);

    String getLastName();
    void setLastName(String strLastName);

    String getPostCode();
    void setPostCode(String strPostCode);

    String getName();

    String getFullAddress();

    String getCountry();
    void setCountry(String strCountry);

    String getCity();
    void setCity(String strCity);

    List<String> getStreet();
    void setStreet(List<String> listStreet);

    String getRegion();
    void setRegion(String strRegion);

    String getRegionId();
    void setRegionId(String strRegionId);

    String getRegionCode();
    void setRegionCode(String strRegionCode);

    String getTelephone();
    void setTelephone(String strTelephone);

    String getCompany();
    void setCompany(String strCompany);

    String getVat();
    void setVat(String strVat);
}
