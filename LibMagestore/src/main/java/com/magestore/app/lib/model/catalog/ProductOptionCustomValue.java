package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ProductOptionCustomValue extends Model {
    void setID(String id);

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

    void setOptionTypeId(String option_type_id);

    void setOptionId(String option_id);

    void setSku(String sku);

    void setDefaultTitle(String default_title);

    void setStoreTitle(String store_title);

    void setTitle(String title);

    void setDefaultPrice(String default_price);

    void setStorePrice(String store_price);

    void setStorePriceType(String store_price_type);

    void setPrice(String price);

    void setPriceType(String price_type);

    boolean isChosen();

    void setChosen(boolean is_chosen);
}
