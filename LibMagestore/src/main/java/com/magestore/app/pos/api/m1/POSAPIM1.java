package com.magestore.app.pos.api.m1;

/**
 * Created by Johan on 8/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSAPIM1 {
    // Page
    public static final String PARAM_ID = "ID";
    public static final String PARAM_CURRENT_PAGE = "currentPage";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_SESSION_ID = "sessionID";
    public static final String PARAM_CONFIG_ID = "configID";
    public static final String PARAM_CONFIG_PATH = "configPath";
    public static final String PARAM_PRODUCT_ID = "productID";

    public static final String REST_PRODUCT_GET_LISTING = "api/rest/webpos/products/?";

    // API staff
    public static final String REST_LOGIN = "api/rest/webpos/staff/login";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "api/rest/webpos/configurations?";
    public static final String REST_POS_ASSIGN = "api/rest/webpos/posassign?";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING_POS = "api/rest/webpos/poslist?";
}
