package com.magestore.app.lib.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.pos.model.catalog.PosProductOption;

import java.util.Date;
import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Product extends Model, ParseModel {
    String getID();
    String getName();

    boolean haveProductOption();

    ProductOption getProductOption();

    void setProductOption(ProductOption productOption);

//    boolean haveProductionOption();

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

    int getQuantityIncrement();

    float getPriceIncrement();

    void setQuantityIncrement(float quantityIncrement);

    String getJsonConfigOption();
}
