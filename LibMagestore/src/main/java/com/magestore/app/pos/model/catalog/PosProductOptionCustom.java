package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by folio on 3/3/2017.
 */

public class PosProductOptionCustom extends PosAbstractModel implements ProductOptionCustom {
    String option_id;
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
    List<PosProductOptionCustomerValue> values;
    PosProductOptionJsonConfig json_config;

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
    public List<PosProductOptionCustomerValue> getOptionValueList() {
        return values;
    }

    @Override
    public boolean isTypeSelectOne() {
        return "radio".equals(type) || "dropdown".equals(type);
    }

    @Override
    public boolean isTypeSelectMultipe() {
        return "checkbox".equals(type);
    }

    @Override
    public boolean isTypeChooseQuantity() {
        return false;
    }
}
