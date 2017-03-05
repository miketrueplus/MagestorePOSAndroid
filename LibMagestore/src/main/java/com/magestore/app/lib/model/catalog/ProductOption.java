package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;
import com.magestore.app.pos.model.catalog.PosProductOptionConfigOption;
import com.magestore.app.pos.model.catalog.PosProductOptionCustom;
import com.magestore.app.pos.model.catalog.PosProductOptionJsonConfig;
import com.magestore.app.pos.model.catalog.PosProductOptionPriceConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ProductOption extends Model {
    Map<String, PosProductOptionConfigOption> getConfigurableOptions();

    void setConfigurableOptions(Map<String, PosProductOptionConfigOption> configurable_options);

    List<PosProductOptionCustom> getCustomOptions();

    void setCustomOptions(List<PosProductOptionCustom> custom_options);

    PosProductOptionJsonConfig getJson_config();

    void setJsonConfig(PosProductOptionJsonConfig json_config);

    PosProductOptionPriceConfig getPriceConfig();

    void setPriceConfig(PosProductOptionPriceConfig price_config);
}
