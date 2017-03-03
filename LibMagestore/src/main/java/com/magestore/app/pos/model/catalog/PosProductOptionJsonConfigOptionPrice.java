package com.magestore.app.pos.model.catalog;

import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 2/16/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class PosProductOptionJsonConfigOptionPrice extends PosAbstractModel {
    float oldPrice;
    float basePrice;
    float finalPrice;

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }
}
