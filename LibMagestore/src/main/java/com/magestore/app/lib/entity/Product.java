package com.magestore.app.lib.entity;

import com.magestore.app.lib.parse.ParseEntity;

import java.util.Date;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Product extends Entity, ParseEntity {
    String getID();
    String getName();
    String getTypeID();
    String getSKU();
    float getPrice();
    float getFinalPrice();
    String getDescription();
    String getStatus();
    Date getUpdateAt();
    String getExtensionAttributes();
    String getCategoryIDs();
    String getImage();
    float getStock();
    String getBarcodeString();
}
