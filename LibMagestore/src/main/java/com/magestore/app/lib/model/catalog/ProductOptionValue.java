package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ProductOptionValue extends Model {
    String getOptionTypeID();

    String getOptionID();

    String getSku();

    String getSortOrder();

    String getDefaultTitle();

    String getStoreTitle();

    String getTitle();

    String getDefaultPrice();

    String getDefaultPriceType();

    String getStorePrice();

    String getStorePriceType();

    String getPrice();

    String getPriceType();
}
