package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.Entity;
import com.magestore.app.lib.parse.ParseEntity;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class AbstractEntity implements Entity, ParseEntity {
    protected String id;
    protected HashMap<String, Object> mRefer;

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setRefer(String key, Object value) {
        if (mRefer == null) mRefer = new HashMap<String, Object>();
        mRefer.put(key, value);
    }

    @Override
    public Object getRefer(String key) {
        if (mRefer == null) return null;
        return mRefer.get(key);
    }

    @Override
    public boolean setValue(String key, Object value) {
        Class clazz = this.getClass();
        if (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                field.set(this, value);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    @Override
    public Object getValue(String key) {
        Class clazz = this.getClass();
        if (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                return field.get(this);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    @Override
    public Object getValue(String key, Object defaultValue) {
        Object value = getValue(key);
        if (value == null) return defaultValue;
        else return value;
    }
}
