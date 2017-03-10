package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by folio on 3/3/2017.
 */

public class PosProductOptionCustom extends PosAbstractModel implements ProductOptionCustom {
    String option_id;
    String option_code;
    String product_id;
    String type;
    String is_require;
    String sku;
    String max_characters;
    String file_extension;
    String image_size_x;
    String image_size_y;
    String sort_order;
    String default_title;
    String store_title;
    String title;
    String default_price;
    String default_price_type;
    String store_price;
    String store_price_type;
    String price;
    String price_type;
    List<PosProductOptionCustomValue> values;

    @Override
    public String getID() {
        return option_id;
    }

    @Override
    public String getDisplayContent() {
        return (title != null) ? title : default_title;
    }

    @Override
    public String getSubDisplayContent() {
        return (price != null) ? price : store_title;
    }

    @Override
    public String getOptionID() {
        return option_id;
    }

    @Override
    public String getOptionCode() {
        return option_code;
    }

    @Override
    public String getProductID() {
        return product_id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isRequired() {
        return "1".equals(is_require);
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public int getMaxCharacters() {
        return (max_characters == null) ? -1 : Integer.parseInt(max_characters);
    }

    @Override
    public String getFileExtension() {
        return file_extension;
    }

    @Override
    public String getImageSizeX() {
        return image_size_x;
    }

    @Override
    public String getImageSizeY() {
        return image_size_y;
    }

    @Override
    public String getSortOrder() {
        return sort_order;
    }

    @Override
    public String getDefaultTitle() {
        return default_title;
    }

    @Override
    public String getStoreTitle() {
        return store_title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDefaultPrice() {
        return default_price;
    }

    @Override
    public String getDefaultPriceType() {
        return default_price_type;
    }

    @Override
    public String getStorePrice() {
        return store_price;
    }

    @Override
    public String getStorePriceType() {
        return store_price_type;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public String getPriceType() {
        return price_type;
    }

    @Override
    public boolean isPriceTypeFixed() {
        return !"percent".equals(price_type);
    }

    @Override
    public boolean isPriceTypePercent() {
        return "percent".equals(price_type);
    }

    @Override
    public List<PosProductOptionCustomValue> getOptionValueList() {
        return values;
    }

    @Override
    public boolean isTypeSelectOne() {
        return "radio".equals(type) || "drop_down".equals(type);
    }

    @Override
    public boolean isTypeSelectMultipe() {
        return "checkbox".equals(type) || "multiple".equals(type);
    }

    @Override
    public boolean isTypeChooseQuantity() {
        return "field".equals(type);
    }

    @Override
    public boolean isTypeTime() {
        return "time".equals(type);
    }

    @Override
    public boolean isTypeDate() {
        return "date".equals(type);
    }

    @Override
    public boolean isTypeDateTime() {
        return "date_time".equals(type);
    }

    @Override
    public void setOptionID(String option_id) {
        this.option_id = option_id;
    }

    @Override
    public void setOptionCode(String option_code) {
        this.option_code = option_code;
    }

    @Override
    public void setProductID(String product_id) {
        this.product_id = product_id;
    }

    @Override
    public void setOptionValueList(List<PosProductOptionCustomValue> values) {
        this.values = values;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setRequire(boolean isRequire) {
        this.is_require = isRequire ? "1" : "0";
    }

    @Override
    public void setDefaultTitle(String default_title) {
        this.default_title = default_title;
    }

    @Override
    public void setStoreTitle(String store_title) {
        this.store_title = store_title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

//
//    public void setSku(String sku) {
//        this.sku = sku;
//    }
//
//    public void setMax_characters(String max_characters) {
//        this.max_characters = max_characters;
//    }
//
//    public void setFile_extension(String file_extension) {
//        this.file_extension = file_extension;
//    }
//
//    public void setImage_size_x(String image_size_x) {
//        this.image_size_x = image_size_x;
//    }
//
//    public void setImage_size_y(String image_size_y) {
//        this.image_size_y = image_size_y;
//    }
//
//    public void setSort_order(String sort_order) {
//        this.sort_order = sort_order;
//    }
//

//
//    public void setDefault_price(String default_price) {
//        this.default_price = default_price;
//    }
//
//    public void setDefault_price_type(String default_price_type) {
//        this.default_price_type = default_price_type;
//    }
//
//    public void setStore_price(String store_price) {
//        this.store_price = store_price;
//    }
//
//    public void setStore_price_type(String store_price_type) {
//        this.store_price_type = store_price_type;
//    }
//
//    public void setPrice(String price) {
//        this.price = price;
//    }
//
//    public void setPrice_type(String price_type) {
//        this.price_type = price_type;
//    }
}
