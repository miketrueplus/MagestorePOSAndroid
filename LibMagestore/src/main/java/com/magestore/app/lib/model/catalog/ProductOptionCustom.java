package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.catalog.PosProductOptionCustomCustomValue;

import java.util.List;

/**
 * Created by folio on 3/3/2017.
 */

public interface ProductOptionCustom extends Model {
    String getOptionID();

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

    List<PosProductOptionCustomCustomValue> getOptionValueList();

    boolean isTypeSelectOne();

    boolean isTypeSelectMultipe();

    boolean isTypeChooseQuantity();

    boolean isTypeTime();

    boolean isTypeDate();

    boolean isTypeDateTime();
}
