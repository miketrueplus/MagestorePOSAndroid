package com.magestore.app.pos.api.odoo;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSAPIOdoo {
    // API get product
    public static final String REST_PRODUCT_GET_LISTING = "api/pos-products/?";

    // API get category
    public static final String REST_GET_CATEGORY_LISTING = "api/pos-categories/";

    // API check platform
    public static final String REST_CHECK_PLATFORM = "webpos/index/platform/";

    // API staff
    public static final String REST_LOGIN = "api/login/";

    // API customer
    public static final String REST_CUSOMTER_GET_LISTING = "api/pos-customers/?";
    public static final String REST_CUSOMTER_CREATE_CUSTOMER = "api/pos-create-customer/";
    public static final String REST_CUSOMTER_UPDATE = "api/pos-update-customer/";

    // API Place Auto Complete
    public static final String REST_DOMAIN_GOOGLE_MAP_API = "https://maps.googleapis.com";
    public static final String REST_PLACE_AUTO_COMPLETE = "/maps/api/place/autocomplete/json?";
    public static final String REST_PLACE_DETAIL = "/maps/api/place/details/json?";

    // API order
    public static final String REST_ORDER_GET_LISTING = "api/pos-orders/?";
    public static final String REST_ORDER_COMMENTS = "api/pos-add-note-order/";
    public static final String REST_ORDER_INVOICE = "api/pos-create-invoice/";

    // API Checkout
    public static final String REST_CHECK_OUT_PLACE_ORDER = "api/pos-create-order/";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING = "api/pos-sessions/?";
    public static final String REST_REGISTER_SHIFTS_OPEN_SESSION = "api/pos-create-session/";
    public static final String REST_REGISTER_SHIFTS_CLOSE_SESSION = "api/pos-close-session/";
    public static final String REST_REGISTER_SHIFTS_VALIDATE_SESSION = "api/pos-validate-session/";
    public static final String REST_REGISTER_SHIFTS_MAKE_ADJUSTMENT = "api/pos-take-money-in-out/";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING_POS = "api/pos/?";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "api/system-config/";

    // API Account
    public static final String REST_SETTING_ACCOUNT = "api/change-staff-info/";
}
