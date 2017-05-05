package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by mike on 5/4/2017.
 */
public interface OrderCustomSalesInfo extends Model {
    String getProductId();

    String getProductName();

    float getUnitPrice();

    String getTaxClassId();

    boolean isVirtual();

    int getQty();
}
