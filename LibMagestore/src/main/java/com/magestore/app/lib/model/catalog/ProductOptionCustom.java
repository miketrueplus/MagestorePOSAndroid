package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomValue;

import java.util.List;

/**
 * Created by folio on 3/3/2017.
 */

public interface ProductOptionCustom extends Model {
    String getOptionID();

    String getOptionCode();

    String getProductID();

    String getType();

    boolean isRequired();

    String getSku();

    int getMaxCharacters();

    String getFileExtension();

    String getImageSizeX();

    String getImageSizeY();

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

    List<PosProductOptionCustomValue> getOptionValueList();

    boolean isTypeSelectOne();

    boolean isTypeSelectMultipe();

    boolean isTypeChooseQuantity();

    boolean isTypeTime();

    boolean isTypeDate();

    boolean isTypeDateTime();

    void setOptionID(String option_id);

    void setOptionCode(String option_code);

    void setProductID(String product_id);

    void setOptionValueList(List<PosProductOptionCustomValue> values);

    void setType(String type);

    void setRequire(boolean isRequire);

    void setDefaultTitle(String default_title);

    void setStoreTitle(String store_title);

    void setTitle(String title);
}
