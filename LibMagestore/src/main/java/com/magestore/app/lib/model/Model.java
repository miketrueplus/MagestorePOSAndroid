package com.magestore.app.lib.model;

import android.os.Parcelable;

/**
 * Created by Mike on 12/12/2016.
 */

public interface Model extends Parcelable {
    String getID();
    String getDisplayContent();
    void setRefer(String key, Object value);
    Object getRefer(String key);
    boolean setValue(String key, Object value);
    Object getValue(String key);
    Object getValue(String key, Object defaultValue);
}
