package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductOptionValue;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 2/16/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosProductOption extends PosAbstractModel implements ProductOption {
    Map<String, PosProductOptionConfigOption> configurable_options;
    List<PosProductOptionCustom> custom_options;
    PosProductOptionJsonConfig json_config;
    PosProductOptionPriceConfig price_config;
}
