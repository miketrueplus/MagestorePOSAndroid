package com.magestore.app.pos.model.catalog;

import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;
import java.util.Map;

/**
 * Created by folio on 3/3/2017.
 */

public class PosProductOptionJsonConfig extends PosAbstractModel {
    public Map<String, PosProductOptionJsonConfigAttributes> attributes;
    public String template;
    public String productId;
    public String chooseText;
    public Map<String, PosProductOptionJsonConfigOptionPrice> optionPrices;
    public Map<String, List<PosProductOptionJsonConfigOptionImage>> images;
    public Map<String, Map<String, String>> index;
//    public PosProductConfigOptionPrice prices;
//
//    /**
//     * Trả lại ảnh của option
//     *
//     * @param optionID
//     * @return
//     */
//    public List<PosProductOptionJsonConfigOptionImage> getOptionImageList(String optionID) {
//        if (images == null) return null;
//        return images.get(optionID);
//    }
//
//    /**
//     * Trả lại ảnh là ảnh main
//     *
//     * @param optionID
//     * @return
//     */
//    public PosProductOptionJsonConfigOptionImage getMainOptionImage(String optionID) {
//        List<PosProductOptionJsonConfigOptionImage> imageList = getOptionImageList(optionID);
//        if (imageList == null) return null;
//        if (imageList.size() == 0) return null;
//        for (PosProductOptionJsonConfigOptionImage image : imageList) {
//            if (image.isMain()) return image;
//        }
//        return imageList.get(0);
//    }
//
//    /**
//     * Trả lại thông tin về price cho 1 option
//     *
//     * @param optionID
//     * @return
//     */
//    public PosProductConfigOptionPrice getOptionPrice(String optionID) {
//        return optionPrices.get(optionID);
//    }
//
//    /**
//     * Trả lại list obtion theo a
//     *
//     * @param atttributeID
//     * @return
//     */
//    public List<PosProductConfigOptionAttributes.Option> getOptionList(String atttributeID) {
//        return attributes.get(atttributeID).options;
//    }
}
