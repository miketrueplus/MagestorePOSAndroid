package com.magestore.app.lib.model.inventory;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.parse.ParseModel;

/**
 * Created by folio on 3/28/2017.
 */

public interface Stock extends Model, ParseModel {
    int getQuantity();

    boolean isUseConfigMinQuantity();

    int getMinQuantity();

    boolean isUseConfigMinSaleQuantity();

    int getMinSaleQuantity();

    boolean isUseConfigMaxSaleQuantity();

    int getMaxSaleQuantity();

    int getQuantityIncrement();
}
