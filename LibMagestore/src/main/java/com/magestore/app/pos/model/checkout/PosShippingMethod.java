package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 2/8/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosShippingMethod extends PosAbstractModel implements ShippingMethod {
    public static final String TYPE_FIXED_RATE = "TYPE_FIXED_RATE";
    public static final String TYPE_PROPORTION_RATE = "TYPE_PROPORTION_RATE";
    public static final String TYPE_FREE_RATE = "TYPE_FREE_RATE";

    String name;
    String type;
    float rate;

    @Override
    public float getRate() {
        return rate;
    }

    @Override
    public void setName(String strName) {
        this.name = strName;
    }

    @Override
    public String getDisplayContent() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setID(String id) {
        this.id = id;
    }

    @Override
    public boolean isFixedRate() {
        return TYPE_FIXED_RATE.equals(type);
    }

    @Override
    public void setFixedRate(float rate) {
        type = TYPE_FIXED_RATE;
        this.rate = rate;
    }

    @Override
    public boolean isFreeRate() {
        return TYPE_FREE_RATE.equals(type);
    }

    @Override
    public void setFreeRate() {
        type = TYPE_FREE_RATE;
        this.rate = 0;
    }

    @Override
    public boolean isProportionRate() {
        return TYPE_PROPORTION_RATE.equals(type);
    }

    @Override
    public void setProportionRate(float rate) {
        type = TYPE_PROPORTION_RATE;
        this.rate = rate;
    }


}
