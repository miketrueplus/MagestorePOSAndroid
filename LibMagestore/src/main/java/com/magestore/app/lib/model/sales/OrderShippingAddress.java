package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShippingAddress extends Model {
    String getAddressType();

    String getEmail();

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

    String getRegion();

    String getRegionId();

    String getRegionCode();

    String getTelephone();
    void setTelephone(String strTelephone);
}
