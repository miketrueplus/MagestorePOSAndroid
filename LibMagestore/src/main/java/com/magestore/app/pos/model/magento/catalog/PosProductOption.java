package com.magestore.app.pos.model.magento.catalog;

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
    Map<String, PosProductOptionConfigOption> config_options;
    List<PosProductOptionCustom> custom_options;
    List<PosProductOptionBundle> bundle_options;
    List<PosProductOptionGrouped> grouped_options;
    PosProductOptionJsonConfig json_config;
    PosProductOptionPriceConfig price_config;

    @Override
    public Map<String, PosProductOptionConfigOption> getConfigurableOptions() {
        if (configurable_options != null) {
            return configurable_options;
        }
        if (config_options != null) {
            return config_options;
        }
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
    public List<PosProductOptionBundle> getBundleOptions() {
        return bundle_options;
    }

    @Override
    public void setBundleOptions(List<PosProductOptionBundle> bundle_options) {
        this.bundle_options = bundle_options;
    }

    @Override
    public List<PosProductOptionGrouped> getGroupedOptions() {
        return grouped_options;
    }

    @Override
    public PosProductOptionJsonConfig getJsonConfig() {
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
