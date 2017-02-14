package com.magestore.app.pos.api.m2;

import com.magestore.app.lib.BuildConfig;
import com.magestore.app.lib.resourcemodel.DataAccessSession;

/**
 * Quản lý session của DataAccess POS
 * Magestore
 * mike@trueplus.vn
 */

public class POSDataAccessSession implements DataAccessSession {
    // session id truy cập gateway
    public static String REST_SESSION_ID = null;

    // cấu hình url và user password khi có proxy
    public static String REST_BASE_URL = BuildConfig.DEFAULT_REST_BASE_URL;
    public static String REST_USER_NAME = BuildConfig.DEFAULT_REST_USER_NAME;
    public static String REST_PASSWORD = BuildConfig.DEFAULT_REST_PASSWORD;

    // cấu hình date time và currency format
    public static final String REST_DATEFORMAT = "DD/MM/YYYY";
    public static final String REST_TIMEFORMAT = "HH:mm:ss";
    public static final String REST_CURRENCY_SYMBOL = "$";
}