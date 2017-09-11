package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductTaxDetailOdoo;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 9/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosProductTaxDetailOdoo extends PosAbstractModel implements ProductTaxDetailOdoo {
    String name;
    float amount;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String strName) {
        name = strName;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public void setAmount(float fAmount) {
        amount = fAmount;
    }
}
