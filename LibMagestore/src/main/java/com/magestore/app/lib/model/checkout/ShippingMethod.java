package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ShippingMethod extends Model {
    float getRate();

    void setName(String strName);
    String getName();

    void setID(String id);

    boolean isFixedRate();

    void setFixedRate(float rate);

    boolean isFreeRate();

    void setFreeRate();

    boolean isProportionRate();

    void setProportionRate(float rate);
}
