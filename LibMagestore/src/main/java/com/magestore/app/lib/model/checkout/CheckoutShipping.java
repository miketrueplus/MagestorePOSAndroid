package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.customer.CustomerAddress;

import java.util.ArrayList;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CheckoutShipping extends Model {
    String getTitle();

    String getDescription();

    String getErrorMessage();

    String getPriceType();

    float getPrice();

    void setCode(String strCode);

    String getCode();

    void setTrack(ArrayList<Model> tracks);

    ArrayList<Model> getTracks();

    String getDatetime();

    void setDatetime(String strDatetime);

    void setAddress(CustomerAddress address);

    CustomerAddress getAddress();

    String getIsDefault();
}
