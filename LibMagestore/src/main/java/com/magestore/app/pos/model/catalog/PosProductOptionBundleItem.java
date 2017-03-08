package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOptionBundleItem;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by folio on 3/7/2017.
 */

public class PosProductOptionBundleItem extends PosAbstractModel implements ProductOptionBundleItem {
    String entity_id;
    String attribute_set_id;
    String type_id;
    String sku;
    String has_options;
    String required_options;
    String created_at;
    String updated_at;
    String updated_datetime;
    String selection_id;
    String option_id;
    String parent_product_id;
    String product_id;
    String position;
    String is_default;
    String selection_price_type;
    String selection_price_value;
    String selection_qty;
    String selection_can_change_qty;
    String color;
    String status;
    String tax_class_id;
    String size;
    String name;
    String image;
    String small_image;
    String thumbnail;
    String url_key;
    String price;
    String special_from_date;
    String news_from_date;
    int store_id;

    @Override
    public String getEntityId() {
        return entity_id;
    }

    @Override
    public String getAttributeSetId() {
        return attribute_set_id;
    }

    @Override
    public String getTypeId() {
        return type_id;
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getHasOptions() {
        return has_options;
    }

    @Override
    public String getRequiredOptions() {
        return required_options;
    }

    @Override
    public String getCreatedAt() {
        return created_at;
    }

    @Override
    public String getUpdatedAt() {
        return updated_at;
    }

    @Override
    public String getUpdatedDatetime() {
        return updated_datetime;
    }

    @Override
    public String getSelectionId() {
        return selection_id;
    }

    @Override
    public String getOptionId() {
        return option_id;
    }

    @Override
    public String getParentProductId() {
        return parent_product_id;
    }

    @Override
    public String getProductId() {
        return product_id;
    }

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public boolean isDefault() {
        return "1".equals(is_default);
    }

    @Override
    public String getSelectionPriceType() {
        return selection_price_type;
    }

    @Override
    public String getSelectionPriceValue() {
        return selection_price_value;
    }

    @Override
    public String getSelectionQty() {
        return selection_qty;
    }

    @Override
    public String getSelectionCanChangeQty() {
        return selection_can_change_qty;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getTaxClassId() {
        return tax_class_id;
    }

    @Override
    public String getSize() {
        return size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getSmallImage() {
        return small_image;
    }

    @Override
    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public String getUrlKey() {
        return url_key;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public String getSpecialFromDate() {
        return special_from_date;
    }

    @Override
    public String getNewsFromDate() {
        return news_from_date;
    }

    @Override
    public int getStoreDd() {
        return store_id;
    }
}
