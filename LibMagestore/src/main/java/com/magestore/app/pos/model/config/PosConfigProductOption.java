package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigOptionSwatch;
import com.magestore.app.lib.model.config.ConfigProductOption;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.Map;

/**
 * Created by Johan on 7/6/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosConfigProductOption extends PosAbstractModel implements ConfigProductOption {
    String attribute_id;
    String attribute_code;
    String attribute_label;
    Map<String, ConfigOptionSwatch> colorSwatch;
}
