package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 7/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface ConfigOption extends Model {
    List<ConfigProductOption> getItems();
}
