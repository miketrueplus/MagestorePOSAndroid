package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigOption;
import com.magestore.app.lib.model.config.ConfigProductOption;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 7/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigOption extends PosAbstractModel implements ConfigOption {
    List<PosConfigProductOption> items;

    @Override
    public List<ConfigProductOption> getItems() {
        return (List<ConfigProductOption>) (List<?>) items;
    }
}
