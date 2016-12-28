package com.magestore.app.lib.gateway.pos;

import com.magestore.app.lib.BuildConfig;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class POSAPI {
    // Page
    protected static final String PARAM_ID = "ID";
    protected static final String PARAM_CURRENT_PAGE = "currentPage";
    protected static final String PARAM_PAGE_SIZE = "pageSize";
    protected static final String PARAM_SESSION_ID = "sessionID";
    protected static final String PARAM_CONFIG_ID = "configID";
    protected static final String PARAM_CONFIG_PATH = "configPath";



    // API get product
    protected static final String REST_PRODUCT_GET_LISTING = "/rest/default/V1/webpos/products/?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";

    // API get category
    protected static final String REST_GET_CATEGORY_LISTING = "/rest/default/V1/webpos/categories/?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";

    // API staff
    protected static final String REST_LOGIN = "rest/default/V1/webpos/staff/login";

    // API order
    protected static final String REST_ORDER_GET_DETAIL = "/rest/default/V1/webpos/orders/${orderID}?session=${sessionID}";
    protected static final String REST_ORDER_GET_LISTING = "/rest/default/V1/webpos/orders?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";
    protected static final String REST_ORDER_CANCEL = "/rest/default/V1/webpos/orders/${orderID}/cancel?session=${sessionID}";
    protected static final String REST_ORDER_EMAIL = "/rest/default/V1/webpos/orders/${orderID}/email?session=${sessionID}";
    protected static final String REST_ORDER_COMMENTS = "/rest/default/V1/webpos/orders/${orderID}/comments?session=${sessionID}";
    protected static final String PARAM_ORDER_ID = "orderID";

    // API customer
    protected static final String REST_CUSOMTER_GET_LISTING = "/rest/default/V1/webpos/customers/search?searchCriteria[current_page]=${currentPage}&searchCriteria[page_size]=${pageSize}&session=${sessionID}";

    // API config
    protected static final String REST_CONFIG_GET_LISTING = "/rest/default/V1/webpos/configurations?session=${sessionID}";
    protected static final String REST_CONFIG_GET_ = "/rest/default/V1/webpos/configurations/${configID}?path=${configPath}&session=${sessionID}";

}
