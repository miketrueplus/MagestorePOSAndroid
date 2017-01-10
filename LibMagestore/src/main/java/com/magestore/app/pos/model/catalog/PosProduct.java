package com.magestore.app.pos.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.model.AbstractModel;
import com.magestore.app.pos.model.PosStock;
import com.magestore.app.pos.model.PosTierPrice;

import java.util.Date;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class PosProduct extends AbstractModel implements Product {
    private String type_id;
    private String sku;
    private float price;
    private float final_price;
    private String description;
    private String name;
    private String status;
    private String image;
    private Bitmap bitmap;

    private float special_price;
    private String special_from_date;
    private String updated_at;
    private int max_characters;
    private PosProduct[] custom_options;
    private String[] images;
    private String category_ids;
    private PosStock[] stock;
    private PosTierPrice[] tier_prices;
    @Override
    public String getTypeID() {
        return type_id;
    }

    @Override
    public String getSKU() {
        return sku;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public float getFinalPrice() {
        return final_price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public Date getUpdateAt() {
        return null;
    }

    @Override
    public String getExtensionAttributes() {
        return null;
    }

    @Override
    public String getCategoryIDs() {
        return null;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public float getStock() {
        return 0;
    }

    @Override
    public String getBarcodeString() {
        return null;
    }

    @Override
    public String getName() {return name; }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void setBitmap(Bitmap bmp) {
        bitmap = bmp;
    }
}