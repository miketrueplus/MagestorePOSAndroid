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

    // API get product
    public static final String REST_PRODUCT_GET_LISTING = "api/rest/webpos/products/?";

    // API get product options
    public static final String REST_PRODUCT_GET_OPTION = "api/rest/webpos/product/getoptions?";

    // API get category
    public static final String REST_GET_CATEGORY_LISTING = "api/rest/webpos/categories/?";

    // API staff
    public static final String REST_LOGIN = "api/rest/webpos/staff/login";

    // API customer
    public static final String REST_CUSOMTER_GET_LISTING = "api/rest/webpos/customers/find?";

    // API order
    public static final String REST_ORDER_GET_LISTING = "api/rest/webpos/order/find?";

    // API checkout
    public static final String REST_CHECK_OUT_SAVE_CART = "api/rest/webpos/cart/save?";
    public static final String REST_CHECK_OUT_SAVE_SHIPPING = "api/rest/webpos/checkout/saveShippingMethod?";
    public static final String REST_CHECK_OUT_PLACE_ORDER = "api/rest/webpos/checkout/placeOrder?";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "api/rest/webpos/configurations?";
    public static final String REST_POS_ASSIGN = "api/rest/webpos/posassign?";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING_POS = "api/rest/webpos/poslist?";
}
