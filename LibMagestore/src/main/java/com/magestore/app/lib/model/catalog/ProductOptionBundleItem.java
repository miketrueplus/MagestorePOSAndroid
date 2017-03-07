package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

/**
 * Created by folio on 3/7/2017.
 */

public interface ProductOptionBundleItem extends Model {
    String getEntityId();

    String getAttributeSetId();

    String getTypeId();

    String getSku();

    String getHasOptions();

    String getRequiredOptions();

    String getCreatedAt();

    String getUpdatedAt();

    String getUpdatedDatetime();

    String getSelectionId();

    String getOptionId();

    String getParentProductId();

    String getProductId();

    String getPosition();

    boolean isDefault();

    String getSelectionPriceType();

    String getSelectionPriceValue();

    String getSelectionQty();

    String getSelectionCanChangeQty();

    String getColor();

    String getStatus();

    String getTaxClassId();

    String getSize();

    String getName();

    String getImage();

    String getSmallImage();

    String getThumbnail();

    String getUrlKey();

    String getPrice();

    String getSpecialFromDate();

    String getNewsFromDate();

    int getStoreDd();
}
