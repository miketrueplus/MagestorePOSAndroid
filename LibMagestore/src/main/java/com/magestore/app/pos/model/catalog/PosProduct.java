package com.magestore.app.pos.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.PosStock;
import com.magestore.app.pos.model.PosTierPrice;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosProduct extends PosAbstractModel implements Product {
    private String type_id;
    private String sku;
    private float price;
    private float final_price;
    private String description;
    private String name;
    private String status;
    private String image;
    private List<String> images;
    private Bitmap bitmap;
    private String json_config;
    private float special_price;
    private String special_from_date;
    private String updated_at;
    private int max_characters;
    private PosProduct[] custom_options;
    private String category_ids;
    private PosStock[] stock;
    private PosTierPrice[] tier_prices;
    private float qty_increment = 1;
    private ProductOption productOption;
    private int options;

    @Override
    public boolean haveProductOption() {
        return options > 0;
    }

    @Override
    public ProductOption getProductOption() {
        return productOption;
    }

    @Override
    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

//    @Override
//    public boolean haveProductionOption() {
//        if (productOption == null) return false;
//        if (productOption.attributes == null) return false;
//        if (productOption.attributes.size() == 0) return false;
//        return true;
//    }

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

    @Override
    public String getDisplayContent() {
        return getName();
    }

    @Override
    public String getSubDisplayContent() {
        return getSKU();
    }

    @Override
    public Bitmap getDisplayBitmap() {
        return getBitmap();
    }

    @Override
    public int getQuantityIncrement() {
        return 1;
    }

    @Override
    public void setQuantityIncrement(int quantityIncrement) {
//        this.qty_increment = quantityIncrement;
    }

    @Override
    public String getJsonConfigOption() {
        return json_config;
    }
}