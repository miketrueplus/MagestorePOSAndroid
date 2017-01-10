package com.magestore.app.lib.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.parse.ParseEntity;

import java.util.Date;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Product extends Model, ParseEntity {
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
    Bitmap getBitmap();
    void setBitmap(Bitmap bmp);
}
