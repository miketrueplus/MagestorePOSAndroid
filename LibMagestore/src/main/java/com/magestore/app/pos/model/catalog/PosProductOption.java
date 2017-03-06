package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.pos.model.PosAbstractModel;

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

    @Override
    public Map<String, PosProductOptionConfigOption> getConfigurableOptions() {
        return configurable_options;
    }

    @Override
    public void setConfigurableOptions(Map<String, PosProductOptionConfigOption> configurable_options) {
        this.configurable_options = configurable_options;
    }

    @Override
    public List<PosProductOptionCustom> getCustomOptions() {
        return custom_options;
    }

    @Override
    public void setCustomOptions(List<PosProductOptionCustom> custom_options) {
        this.custom_options = custom_options;
    }

    @Override
    public PosProductOptionJsonConfig getJson_config() {
        return json_config;
    }

    @Override
    public void setJsonConfig(PosProductOptionJsonConfig json_config) {
        this.json_config = json_config;
    }

    @Override
    public PosProductOptionPriceConfig getPriceConfig() {
        return price_config;
    }

    @Override
    public void setPriceConfig(PosProductOptionPriceConfig price_config) {
        this.price_config = price_config;
    }
}
