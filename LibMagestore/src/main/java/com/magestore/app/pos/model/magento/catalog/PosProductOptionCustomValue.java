package com.magestore.app.pos.model.magento.catalog;

import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 2/16/2017.
 * Magestore
 * mike@trueplus.vn
 */
public class PosProductOptionCustomValue extends PosAbstractModel implements ProductOptionCustomValue {
    String option_type_id;
    String option_id;
    String sku;
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

    int quantity;
    boolean is_choosen = false;
    boolean is_quantity = false;

    public boolean isQuantity() {
        return is_quantity;
    }

    public void setIsQuantity(boolean is_quantity) {
        this.is_quantity = is_quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getID() {
        return option_type_id;
    }

    @Override
    public void setID(String id) {
        this.option_type_id = id;
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
    public String getOptionTypeID() {
        return option_type_id;
    }

    @Override
    public String getOptionID() {
        return option_id;
    }

    @Override
    public String getSku() {
        return sku;
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
    public void setOptionTypeId(String option_type_id) {
        this.option_type_id = option_type_id;
    }

    @Override
    public void setOptionId(String option_id) {
        this.option_id = option_id;
    }

    @Override
    public void setSku(String sku) {
        this.sku = sku;
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

    @Override
    public void setDefaultPrice(String default_price) {
        this.default_price = default_price;
    }

    @Override
    public void setStorePrice(String store_price) {
        this.store_price = store_price;
    }

    @Override
    public void setStorePriceType(String store_price_type) {
        this.store_price_type = store_price_type;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public void setPriceType(String price_type) {
        this.price_type = price_type;
    }

    @Override
    public boolean isChosen() {
        return is_choosen;
    }

    @Override
    public void setChosen(boolean is_chosen) {
        this.is_choosen = is_chosen;
    }
}
