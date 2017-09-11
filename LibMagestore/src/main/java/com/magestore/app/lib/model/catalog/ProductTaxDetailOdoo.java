package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 9/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ProductTaxDetailOdoo extends Model {
    String getName();
    void setName(String strName);
    float getAmount();
    void setAmount(float fAmount);
}
