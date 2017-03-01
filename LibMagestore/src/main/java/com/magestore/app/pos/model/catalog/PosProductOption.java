package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductOptionValue;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 2/16/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosProductOption extends PosAbstractModel implements ProductOption {
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
    List<PosProductOptionValue> values;

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
    public List<PosProductOptionValue> getOptionValueList() {
        return values;
    }

    //    public Map<String, PosProductConfigOptionAttributes> attributes;
//    public String template;
//    public String productId;
//    public String chooseText;
//    public Map<String, PosProductConfigOptionPrice> optionPrices;
//    public Map<String, List<PosProductConfigOptionImage>> images;
//    public Map<String, Map<String, String>> index;
//    public PosProductConfigOptionPrice prices;
//
//    /**
//     * Trả lại ảnh của option
//     * @param optionID
//     * @return
//     */
//    public List<PosProductConfigOptionImage> getOptionImageList(String optionID) {
//        if (images == null) return null;
//        return images.get(optionID);
//    }
//
//    /**
//     * Trả lại ảnh là ảnh main
//     * @param optionID
//     * @return
//     */
//    public PosProductConfigOptionImage getMainOptionImage(String optionID) {
//        List<PosProductConfigOptionImage> imageList = getOptionImageList(optionID);
//        if (imageList == null) return null;
//        if (imageList.size() == 0) return null;
//        for (PosProductConfigOptionImage image: imageList) {
//            if (image.isMain()) return image;
//        }
//        return imageList.get(0);
//    }
//
//    /**
//     * Trả lại thông tin về price cho 1 option
//     * @param optionID
//     * @return
//     */
//    public PosProductConfigOptionPrice getOptionPrice(String optionID) {
//        return optionPrices.get(optionID);
//    }
//
//    /**
//     * Trả lại list obtion theo a
//     * @param atttributeID
//     * @return
//     */
//    public List<PosProductConfigOptionAttributes.Option> getOptionList(String atttributeID) {
//        return attributes.get(atttributeID).options;
//    }
}
