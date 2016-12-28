package com.magestore.app.lib.entity.pos;

import com.google.gson.annotations.SerializedName;
import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.parse.ParseEntity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Nắm các biến config của hệ thống
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfig extends AbstractEntity implements Config, ParseEntity {
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
