package com.magestore.app.lib.model.checkout;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ShippingMethod {
    float getRate();

    void setName(String strName);

    void setID(String id);

    boolean isFixedRate();

    void setFixedRate(float rate);

    boolean isFreeRate();

    void setFreeRate();

    boolean isProportionRate();

    void setProportionRate(float rate);
}
