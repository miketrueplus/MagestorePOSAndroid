package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigOdoo;
import com.magestore.app.lib.model.config.DataConfig;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataConfig extends PosAbstractModel implements DataConfig {
    List<PosConfigOdoo> items;
    int total_count;

    @Override
    public List<ConfigOdoo> getItems() {
        return (List<ConfigOdoo>) (List<?>) items;
    }

    @Override
    public int getTotalCount() {
        return total_count;
    }
}
