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

    // API check platform
    public static final String REST_CHECK_PLATFORM = "webpos/index/platform";

    // API staff
    public static final String REST_LOGIN = "api/rest/webpos/staff/login";
    public static final String REST_STAFF_GET_LISTING = "api/rest/webpos/staffs?";

    // API customer
    public static final String REST_CUSOMTER_GET_LISTING = "api/rest/webpos/customers/find?";
    public static final String REST_CUSOMTER_UPDATE = "api/rest/webpos/customer/create?";
    public static final String REST_CUSOMTER_ADD = "api/rest/webpos/customer/create?";
    public static final String REST_ADDRESS_UPDATE = "api/rest/webpos/customer/create?";
    public static final String REST_ADDRESS_DELETE = "api/rest/webpos/customer/create?";
    public static final String REST_ADDRESS_ADD = "api/rest/webpos/customer/create?";

    // API order
    public static final String REST_ORDER_GET_LISTING = "api/rest/webpos/order/find?";
    public static final String REST_ORDER_CANCEL = "api/rest/webpos/order/cancel?";
    public static final String REST_ORDER_EMAIL = "api/rest/webpos/order/email?";
    public static final String REST_ORDER_COMMENTS = "api/rest/webpos/order/comments?";
    public static final String REST_ORDER_INVOICE_UPDATE_QTY = "api/rest/webpos/invoices/updateqty?";
    public static final String REST_ORDER_INVOICE = "api/rest/webpos/invoices/create?";
    public static final String REST_ORDER_SHIPMENT = "api/rest/webpos/shipment/create?";
    public static final String REST_ORDER_BY_CREDIT = "api/rest/webpos/integration/refundByCredit?";
    public static final String REST_ORDER_BY_GIFTCARD = "api/rest/webpos/integration/refundGiftcardBalance?";
    public static final String REST_ORDER_REFUND = "api/rest/webpos/creditmemo/create?";
    public static final String REST_ORDER_TAKE_PAYMENT = "api/rest/webpos/order/create?";

    // API Cart
    public static final String REST_CART_DELETE_ITEM = "api/rest/webpos/cart/removeItem?";

    // API checkout
    public static final String REST_CHECK_OUT_SAVE_CART = "api/rest/webpos/cart/save?";
    public static final String REST_CHECKOUT_SAVE_QUOTE = "api/rest/webpos/checkout/saveQuoteData?";
    public static final String REST_CHECKOUT_ADD_COUPON_TO_QUOTE = "api/rest/webpos/checkout/applyCoupon?";
    public static final String REST_CHECK_OUT_SAVE_SHIPPING = "api/rest/webpos/checkout/saveShippingMethod?";
    public static final String REST_CHECK_OUT_PLACE_ORDER = "api/rest/webpos/checkout/placeOrder?";
    public static final String REST_CHECK_OUT_SEND_EMAIL = "api/rest/webpos/checkout/sendEmail?";

    // API plugins
    public static final String REST_PLUGIN_APPLY_REWARD_POINT = "api/rest/webpos/integration/spendPoint?";
    public static final String REST_PLUGIN_ADD_GIFTCARD = "api/rest/webpos/integration/applyGiftcard?";
    public static final String REST_PLUGIN_REMOVE_GIFTCARD = "api/rest/webpos/integration/removeGiftcard?";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "api/rest/webpos/configurations?";
    public static final String REST_POS_ASSIGN = "api/rest/webpos/posassign?";

    public static final String REST_SETTING_ACCOUNT = "api/rest/webpos/staff/changepassword?";
    public static final String REST_SETTING_CHANGE_CURRENCY = "api/rest/webpos/currencies/change?";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING_POS = "api/rest/webpos/poslist?";
}
