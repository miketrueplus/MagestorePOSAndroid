package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.BuildConfig;
import com.magestore.app.lib.gateway.GatewaySession;

/**
 * Quản lý session của Gateway POS
 * Magestore
 * mike@trueplus.vn
 */

public class POSGatewaySession implements GatewaySession {
    // session id truy cập gateway
    public static String REST_SESSION_ID = null;

    // cấu hình url và user password khi có proxy
    public static String REST_BASE_URL = BuildConfig.REST_BASE_URL;
    protected static final String REST_USER_NAME = BuildConfig.REST_USER_NAME;
    protected static final String REST_PASSWORD = BuildConfig.REST_PASSWORD;

    // cấu hình date time và currency format
    protected static final String REST_DATEFORMAT = "DD/MM/YYYY";
    protected static final String REST_TIMEFORMAT = "HH:mm:ss";
    protected static final String REST_CURRENCY_SYMBOL = "$";
}