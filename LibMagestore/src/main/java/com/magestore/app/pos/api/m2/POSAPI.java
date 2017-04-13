package com.magestore.app.pos.api.m2;

/**
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSAPI {
    // Page
    public static final String PARAM_ID = "ID";
    public static final String PARAM_CURRENT_PAGE = "currentPage";
    public static final String PARAM_PAGE_SIZE = "pageSize";
    public static final String PARAM_SESSION_ID = "sessionID";
    public static final String PARAM_CONFIG_ID = "configID";
    public static final String PARAM_CONFIG_PATH = "configPath";
    public static final String PARAM_PRODUCT_ID = "productID";

    // API get product
    public static final String REST_PRODUCT_GET_LISTING = "/rest/default/V1/webpos/productlist/?";
    //        public static final String REST_PRODUCT_GET_LISTING = "/rest/default/V1/webpos/products/?";
    public static final String REST_PRODUCT_GET = "/rest/default/V1/webpos/products/?";


    // API get product options
    public static final String REST_PRODUCT_GET_OPTION = "/rest/default/V1/webpos/products/${productID}/options?";

    // API get category
    public static final String REST_GET_CATEGORY_LISTING = "/rest/default/V1/webpos/categories/?";

    // API staff
    public static final String REST_LOGIN = "rest/default/V1/webpos/staff/login";

    // API store
    public static final String REST_STORE_GET_LISTING = "rest/default/V1/webpos/storeInformation?";

    // API order
    public static final String REST_ORDER_GET_DETAIL = "/rest/default/V1/webpos/orders/${orderID}?";
    public static final String REST_ORDER_GET_LISTING = "/rest/default/V1/webpos/orders?";
    public static final String REST_ORDER_CANCEL = "/rest/default/V1/webpos/orders/${orderID}/cancel?";
    public static final String REST_ORDER_EMAIL = "/rest/default/V1/webpos/orders/${orderID}/emails?";
    public static final String REST_ORDER_COMMENTS = "/rest/default/V1/webpos/orders/${orderID}/comments?";
    public static final String REST_ORDER_INVOICE_UPDATE_QTY = "/rest/default/V1/webpos/invoices/updateqty?";
    public static final String REST_ORDER_INVOICE = "/rest/default/V1/webpos/invoices/create?";
    public static final String REST_ORDER_SHIPMENT = "/rest/default/V1/webpos/shipment/create?";
    public static final String REST_ORDER_REFUND = "/rest/default/V1/webpos/creditmemo/create?";
    public static final String REST_PAYMENT_METHOD_GET_LISTING = "/rest/default/V1/webpos/payments?";
    public static final String REST_ORDER_TAKE_PAYMENT = "/rest/default/V1/webpos/orders/${orderID}/payments?";
    public static final String PARAM_ORDER_ID = "orderID";

    // API customer
    public static final String REST_CUSOMTER_GET_LISTING = "/rest/default/V1/webpos/customers/search?";
    public static final String REST_CUSOMTER_GET_DETAIL = "/rest/default/V1/webpos/customers/customerId?customerId=${customerID}";
    public static final String REST_CUSOMTER_UPDATE = "/rest/default/V1/webpos/customers?";
    public static final String REST_CUSOMTER_ADD = "/rest/default/V1/webpos/customers?";
    public static final String REST_CUSOMTER_ADDRESS_GET_LISTING = "/rest/default/V1/webpos/customers/addresses/${customerID}?";
    public static final String REST_ADDRESS_GET_LISTING = "/rest/default/V1/webpos/customers/addresses/?";
    public static final String REST_ADDRESS_UPDATE = "/rest/default/V1/webpos/customers?";
    public static final String REST_ADDRESS_DELETE = "/rest/default/V1/webpos/customers?";
    public static final String REST_ADDRESS_ADD = "/rest/default/V1/webpos/customers?";
    public static final String REST_CUSOMTER_COMPLAIN_GET_LISTING = "/rest/default/V1/webpos/customers/complain/search?searchCriteria";
    public static final String REST_COMPLAIN_GET_LISTING = "/rest/default/V1/webpos/customers/complain/search?searchCriteria";
    public static final String REST_CUSOMTER_COMPLAIN_UPDATE = "/rest/default/V1/webpos/customers/complain/${customerID}?";
    public static final String REST_CUSOMTER_COMPLAIN_DELETE = "/rest/default/V1/webpos/customers/complain/${customerID}?";
    public static final String REST_CUSOMTER_COMPLAIN_ADD = "/rest/default/V1/webpos/customers/complain/?";
    public static final String PARAM_CUSTOMER_ID = "customerID";

    // API Cart
    public static final String REST_CART_DELETE_ITEM = "/rest/default/V1/webpos/checkout/removeItem?quote_id=${quoteID}&item_id=${itemID}";
    public static final String CART_QUOTE_ID = "quoteID";
    public static final String CART_ITEM_ID = "itemID";

    // API checkout
    public static final String REST_CHECKOUT_CREATE = "/rest/default/V1/webpos/checkout/create?";
    public static final String REST_CHECK_OUT_SAVE_CART = "/rest/default/V1/webpos/checkout/saveCart?";
    public static final String REST_CHECKOUT_SAVE_QUOTE = "/rest/default/V1/webpos/checkout/saveQuoteData?";
    public static final String REST_CHECKOUT_ADD_COUPON_TO_QUOTE = "/rest/default/V1/webpos/checkout/applyCoupon?";
    public static final String REST_CHECK_OUT_SAVE_SHIPPING = "/rest/default/V1/webpos/checkout/saveShippingMethod?";
    public static final String REST_CHECK_OUT_SAVE_PAYMENT = "/rest/default/V1/webpos/checkout/savePaymentMethod?";
    public static final String REST_CHECK_OUT_PLACE_ORDER = "/rest/default/V1/webpos/checkout/placeOrder?";
    public static final String REST_CHECK_OUT_SEND_EMAIL = "rest/default/V1/webpos/checkout/sendEmail?";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING = "/rest/default/V1/webpos/shifts/getlist?";
    public static final String REST_REGISTER_SHIFTS_MAKE_ADJUSTMENT = "/rest/default/V1/webpos/cash_transaction/save?";

    // API plugins
    public static final String REST_PLUGIN_APPLY_REWARD_POINT = "/rest/default/V1/webpos/integration/spendPoint?";
    public static final String REST_PLUGIN_ADD_GIFTCARD = "/rest/default/V1/webpos/integration/applyGiftcard?";
    public static final String REST_PLUGIN_REMOVE_GIFTCARD = "/rest/default/V1/webpos/integration/removeGiftcard?";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "/rest/default/V1/webpos/configurations?";
    public static final String REST_CONFIG_GET_ = "/rest/default/V1/webpos/configurations/${configID}?path=${configPath}&session=${sessionID}";

    // API Account
    public static final String REST_SETTING_ACCOUNT = "rest/default/V1/webpos/staff/changepassword?";
}
