package com.magestore.app.util;

/**
 * Object đại diện cho các trường hợp Not Found
 * Created by Mike on 1/5/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class NotFoundObject
{
    private static final NotFoundObject instance = new NotFoundObject();

    @Override
    public String toString() {
        return "Not found";
    }

    public static NotFoundObject getInstance() {
        return instance;
    }
}
