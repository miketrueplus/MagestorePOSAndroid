package com.magestore.app.pos.model.magento.catalog;

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
}
