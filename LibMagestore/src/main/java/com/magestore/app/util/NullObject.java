package com.magestore.app.util;

/**
 * Object đại diện cho các trường hợp Null
 * Created by Mike on 1/5/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class NullObject {
    private static final NullObject instance = new NullObject();

    @Override
    public String toString() {
        return "Null";
    }

    public static NullObject getInstance() {
        return instance;
    }
}
