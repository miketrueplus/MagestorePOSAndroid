package com.magestore.app.lib.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.parse.ParseModel;

import java.util.Date;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Product extends Model, ParseModel {
    String getID();
    String getName();

    boolean isInStock();
    void setInStock(boolean bInStock);

    boolean isBackOrders();

    float getMinimumQty();
    void setMinimumQty(float minimum_qty);
    float getMaximumQty();
    void setMaximumQty(float maximum_qty);

    float getAllowMinQty();
    float getAllowMaxQty();

    float getQty();
    void setQty(float qty);

    boolean isCustomSale();

    void setCustomSale();

    boolean haveProductOption();

    ProductOption getProductOption();

    void setProductOption(ProductOption productOption);

//    boolean haveProductionOption();

    String getTypeID();
    String getSKU();
    float getPrice();
    float getFinalPrice();
    float getSpecialPrice();
    String getDescription();
    String getStatus();
    Date getUpdateAt();
    String getExtensionAttributes();
    String getCategoryIDs();
    String getImage();
    void setImage(String strImage);
    String getBarcodeString();

    void setName(String name);

    Bitmap getBitmap();
    void setBitmap(Bitmap bmp);

    int getQuantityIncrement();

    void setQuantityIncrement(int quantityIncrement);

    float getPriceIncrement();

    void setQuantityIncrement(float quantityIncrement);

    String getJsonConfigOption();

    boolean getIsSaveCart();
    void setIsSaveCart(boolean isSaveCart);

    String getItemId();
    void setItemId(String strItemId);

    boolean isDecimal();

//    List<Stock> getStock();
}
