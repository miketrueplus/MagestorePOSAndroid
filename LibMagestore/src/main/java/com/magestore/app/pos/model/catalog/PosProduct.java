package com.magestore.app.pos.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.inventory.Stock;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.inventory.PosStock;
import com.magestore.app.pos.model.PosTierPrice;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosProduct extends PosAbstractModel implements Product {
    private String is_custom_sale = StringUtil.STRING_ZERO;
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
    private List<PosStock> stock;
    private PosTierPrice[] tier_prices;
    private String qty_increment = StringUtil.STRING_ONE;
    private float price_increment = 0.1f;
    private ProductOption productOption;
    private int options;

    private String is_in_stock = StringUtil.STRING_ONE;
    private String minimum_qty = StringUtil.STRING_ONE;
    private String maximum_qty = StringUtil.STRING_TEN_THOUSAND;
    private String qty = maximum_qty;

    @Override
    public boolean isInStock() {
        return StringUtil.STRING_ONE.equals(is_in_stock);
    }

    @Override
    public float getMinimumQty() {
        return Float.parseFloat(minimum_qty);
    }

    @Override
    public void setMinimumQty(float minimum_qty) {
        this.minimum_qty = Float.toString(minimum_qty);
    }

    @Override
    public float getMaximumQty() {
        return Float.parseFloat(maximum_qty);
    }

    @Override
    public void setMaximumQty(float maximum_qty) {
        this.maximum_qty = Float.toString(maximum_qty);
    }

    @Override
    public int getAllowMinQty() {
        return getQuantityIncrement();
    }

    @Override
    public int getAllowMaxQty() {
        int allowMax = (int) (getQty() - getMinimumQty());
        int maxQty = (int) getMaximumQty();
        return (int) allowMax < maxQty ? allowMax : maxQty;
    }

    @Override
    public float getQty() {
        return Float.parseFloat(qty);
    }

    @Override
    public void setQty(float qty) {
        this.qty = Float.toString(qty);
    }

    @Gson2PosExclude
    boolean isSaveCart;
    @Gson2PosExclude
    String item_id;

    @Override
    public boolean isCustomSale() {
        return StringUtil.STRING_ONE.equals(is_custom_sale);
    }

    @Override
    public void setCustomSale() {
        is_custom_sale = StringUtil.STRING_ONE;
    }

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
        return (description == null) ? "" : description;
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
    public String getBarcodeString() {
        return null;
    }

    @Override
    public String getName() {return name; }

    @Override
    public void setName(String name) {this.name = name; }

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
        if (qty_increment == null || StringUtil.STRING_EMPTY.equals(qty_increment)) return 1;
        return (int) Float.parseFloat(qty_increment);
    }

    @Override
    public void setQuantityIncrement(int quantityIncrement) {
        qty_increment = Integer.toString(quantityIncrement);
    }

    @Override
    public float getPriceIncrement() {
        return price_increment;
    }

    @Override
    public void setQuantityIncrement(float quantityIncrement) {
        this.qty_increment =  Float.toString(quantityIncrement);
    }

    @Override
    public String getJsonConfigOption() {
        return json_config;
    }

    @Override
    public boolean getIsSaveCart() {
        return isSaveCart;
    }

    @Override
    public void setIsSaveCart(boolean isSaveCart) {
        this.isSaveCart = isSaveCart;
    }

    @Override
    public String getItemId() {
        return item_id;
    }

    @Override
    public void setItemId(String strItemId) {
        item_id = strItemId;
    }

    @Override
    public List<Stock> getStock() {
        return (List<Stock>) (List<?>) stock;
    }
}