package com.magestore.app.pos.model.odoo.config;

import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Nắm các biến config của hệ thống
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfig extends PosAbstractModel implements Config, ParseModel {
    Map<String, Object> items;

    @Override
    public boolean setValue(String key, Object value) {
        if (items == null) items = new HashMap<String, Object>();
        items.put(key, value);
        return true;
    }

    @Override
    public Object getValue(String key) {
        if (items == null) return null;
        return items.get(key);
    }
}
