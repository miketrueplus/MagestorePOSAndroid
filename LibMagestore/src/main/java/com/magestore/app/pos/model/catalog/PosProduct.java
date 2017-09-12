package com.magestore.app.pos.model.catalog;

import android.graphics.Bitmap;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductTaxDetailOdoo;
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
    //    private List<PosStock> stock;
//    private PosTierPrice[] tier_prices;
    private float price_increment = 0.1f;
    private ProductOption productOption;
    private int options;

    private String is_in_stock;

    private String backorders = StringUtil.STRING_ONE;

    private String qty_increment = StringUtil.STRING_ONE;
    private String minimum_qty = StringUtil.STRING_ONE;
    private String maximum_qty;
    private String qty = maximum_qty;
    private String is_qty_decimal;
    @Gson2PosExclude
    private boolean isDecimal;

    private List<String> taxes_id;
    private List<ProductTaxDetailOdoo> taxes_detail;
    private float total_tax;

    @Override
    public boolean isInStock() {
        if (StringUtil.isNullOrEmpty(is_in_stock)) {
            return false;
        }
        if (StringUtil.STRING_ONE.equals(is_in_stock) || StringUtil.STRING_TRUE.equals(is_in_stock)) {
            return true;
        }
        return false;
    }

    @Override
    public void setInStock(boolean bInStock) {
        if (bInStock) {
            is_in_stock = "1";
        } else {
            is_in_stock = "0";
        }
    }

    @Override
    public boolean isBackOrders() {
        return StringUtil.STRING_ONE.equals(backorders);
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
        if (StringUtil.isNullOrEmpty(maximum_qty))
            return Float.parseFloat(StringUtil.STRING_TEN_THOUSAND);
        return Float.parseFloat(maximum_qty);
    }

    @Override
    public void setMaximumQty(float maximum_qty) {
        this.maximum_qty = Float.toString(maximum_qty);
    }

    @Override
    public float getAllowMinQty() {
        return (float) getMinimumQty();
    }

    @Override
    public float getAllowMaxQty() {
        return (float) ((getQty() > getMaximumQty()) ? getMaximumQty() : getQty());
//        return (int) getMaximumQty();
//        int allowMax = (int) (getQty() - getMinimumQty());
//        int maxQty = (int) getMaximumQty();
//        return (int) allowMax < maxQty ? allowMax : maxQty;
    }

    @Override
    public float getQty() {
        if (StringUtil.isNullOrEmpty(qty)) {
            qty = "0";
        }
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
    public void setTypeID(String strTypeID) {
        type_id = strTypeID;
    }

    @Override
    public String getSKU() {
        return sku;
    }

    @Override
    public void setSKU(String strSKU) {
        sku = strSKU;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void setPrice(float fPrice) {
        price = fPrice;
    }

    @Override
    public float getFinalPrice() {
        return final_price;
    }

    @Override
    public void setFinalPrice(float fFinalPrice) {
        final_price = fFinalPrice;
    }

    @Override
    public float getSpecialPrice() {
        return special_price;
    }

    @Override
    public String getDescription() {
        return (description == null) ? "" : description;
    }

    @Override
    public void setDescription(String strDescription) {
        description = strDescription;
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
    public void setImage(String strImage) {
        image = strImage;
    }

    @Override
    public String getBarcodeString() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

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
        if (qty_increment == null || StringUtil.STRING_EMPTY.equals(qty_increment) || StringUtil.STRING_FALSE.equals(qty_increment))
            return 1;
        int qty = (int) Float.parseFloat(qty_increment);
        return qty > 0 ? qty : 1;
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
        this.qty_increment = Float.toString(quantityIncrement);
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
    public boolean isDecimal() {
        if (!StringUtil.isNullOrEmpty(is_qty_decimal) && is_qty_decimal.equals("1")) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> getTaxId() {
        return taxes_id;
    }

    @Override
    public void setTaxId(List<String> listTaxId) {
        taxes_id = listTaxId;
    }

    @Override
    public List<ProductTaxDetailOdoo> getTaxDetail() {
        return taxes_detail;
    }

    @Override
    public void setTaxDetail(List<ProductTaxDetailOdoo> listTaxDetail) {
        taxes_detail = listTaxDetail;
    }

    @Override
    public float getTotalTax() {
        return total_tax;
    }

    @Override
    public void setTotalTax(float fTotalTax) {
        total_tax = fTotalTax;
    }

//    @Override
//    public List<Stock> getStock() {
//        return (List<Stock>) (List<?>) stock;
//    }
}