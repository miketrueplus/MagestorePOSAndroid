package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by folio on 5/3/2017.
 */

public interface OrderCartItem extends Model {

    String getChildId();

    int getQty();

    float getUnitPrice();

    float getBaseUnitPrice();

    float getOriginalPrice();

    float getBaseOriginalPrice();

    String getOptionsLabel();

    String getCustomSalesInfo();
}
