package com.magestore.app.pos.api.m2;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSAPI {
    // Page
    public static final String PARAM_ID = "ID";
    public static final String PARAM_CURRENT_PAGE = "currentPage";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_SESSION_ID = "sessionID";
    public static final String PARAM_CONFIG_ID = "configID";
    public static final String PARAM_CONFIG_PATH = "configPath";

    // API get product
    public static final String REST_PRODUCT_GET_LISTING = "/rest/default/V1/webpos/products/?session=${sessionID}";

    // API get category
    public static final String REST_GET_CATEGORY_LISTING = "/rest/default/V1/webpos/categories/?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";

    // API staff
    public static final String REST_LOGIN = "rest/default/V1/webpos/staff/login";

    // API order
    public static final String REST_ORDER_GET_DETAIL = "/rest/default/V1/webpos/orders/${orderID}?session=${sessionID}";
    public static final String REST_ORDER_GET_LISTING = "/rest/default/V1/webpos/orders?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";
    public static final String REST_ORDER_CANCEL = "/rest/default/V1/webpos/orders/${orderID}/cancel?session=${sessionID}";
    public static final String REST_ORDER_EMAIL = "/rest/default/V1/webpos/orders/${orderID}/email?session=${sessionID}";
    public static final String REST_ORDER_COMMENTS = "/rest/default/V1/webpos/orders/${orderID}/comments?session=${sessionID}";
    public static final String REST_ORDER_INVOICE = "/rest/default/V1/webpos/invoices?session=${sessionID}";
    public static final String PARAM_ORDER_ID = "orderID";

    // API customer
    public static final String REST_CUSOMTER_GET_LISTING = "/rest/default/V1/webpos/customers/search?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING = "/rest/default/V1/webpos/shifts/getlist?session=${sessionID}";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "/rest/default/V1/webpos/configurations?session=${sessionID}";
    public static final String REST_CONFIG_GET_ = "/rest/default/V1/webpos/configurations/${configID}?path=${configPath}&session=${sessionID}";

}
