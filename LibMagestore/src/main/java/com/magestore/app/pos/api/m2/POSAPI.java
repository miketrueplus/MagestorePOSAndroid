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
    
    public static final String PATH = "/rest/default";

    // API get product
    public static final String REST_PRODUCT_GET_LISTING = "/V1/webpos/productlist/?";
//    public static final String REST_PRODUCT_GET_LISTING = "/V1/webpos/products/?";
//    public static final String REST_PRODUCT_GET = "/V1/webpos/products/?";
    public static final String REST_PRODUCT_GET = "/V1/webpos/productlist/?";

    // API get product options
    public static final String REST_PRODUCT_GET_OPTION = "/V1/webpos/products/${productID}/options?";

    // API get category
    public static final String REST_GET_CATEGORY_LISTING = "/V1/webpos/categories/?";

    // API check platform
    public static final String REST_CHECK_PLATFORM = "webpos/index/platform";

    // API staff
    public static final String REST_LOGIN = "/V1/webpos/staff/login";
    public static final String REST_STAFF_GET_LISTING = "/V1/webpos/staffs?";

    // API store
    public static final String REST_STORE_GET_LISTING = "rest/default/V1/webpos/storeInformation?";

    // API order
    public static final String REST_ORDER_GET_DETAIL = "/V1/webpos/orders/${orderID}?";
    public static final String REST_ORDER_GET_LISTING = "/V1/webpos/orders?";
    public static final String REST_ORDER_CANCEL = "/V1/webpos/orders/${orderID}/cancel?";
    public static final String REST_ORDER_EMAIL = "/V1/webpos/orders/${orderID}/emails?";
    public static final String REST_ORDER_COMMENTS = "/V1/webpos/orders/${orderID}/comments?";
    public static final String REST_ORDER_INVOICE_UPDATE_QTY = "/V1/webpos/invoices/updateqty?";
    public static final String REST_ORDER_INVOICE = "/V1/webpos/invoices/create?";
    public static final String REST_ORDER_SHIPMENT = "/V1/webpos/shipment/create?";
    public static final String REST_ORDER_BY_CREDIT = "rest/default/V1/webpos/integration/refundByCredit?";
    public static final String REST_ORDER_BY_GIFTCARD = "rest/default/V1/webpos/integration/refundGiftcardBalance?";
    public static final String REST_ORDER_REFUND = "/V1/webpos/creditmemo/create?";
    public static final String REST_PAYMENT_METHOD_GET_LISTING = "/V1/webpos/payments?";
    public static final String REST_ORDER_TAKE_PAYMENT = "/V1/webpos/orders/${orderID}/payments?";
    public static final String PARAM_ORDER_ID = "orderID";

    // API customer
    public static final String REST_CUSOMTER_GET_LISTING = "/V1/webpos/customers/search?";
    public static final String REST_CUSOMTER_GET_DETAIL = "/V1/webpos/customers/customerId?customerId=${customerID}";
    public static final String REST_CUSOMTER_UPDATE = "/V1/webpos/customers?";
    public static final String REST_CUSOMTER_ADD = "/V1/webpos/customers?";
    public static final String REST_CUSOMTER_ADDRESS_GET_LISTING = "/V1/webpos/customers/addresses/${customerID}?";
    public static final String REST_ADDRESS_GET_LISTING = "/V1/webpos/customers/addresses/?";
    public static final String REST_ADDRESS_UPDATE = "/V1/webpos/customers?";
    public static final String REST_ADDRESS_DELETE = "/V1/webpos/customers?";
    public static final String REST_ADDRESS_ADD = "/V1/webpos/customers?";
    public static final String REST_CUSOMTER_COMPLAIN_GET_LISTING = "/V1/webpos/customers/complain/search?searchCriteria";
    public static final String REST_COMPLAIN_GET_LISTING = "/V1/webpos/customers/complain/search?searchCriteria";
    public static final String REST_CUSOMTER_COMPLAIN_UPDATE = "/V1/webpos/customers/complain/${customerID}?";
    public static final String REST_CUSOMTER_COMPLAIN_DELETE = "/V1/webpos/customers/complain/${customerID}?";
    public static final String REST_CUSOMTER_COMPLAIN_ADD = "/V1/webpos/customers/complain/?";
    public static final String PARAM_CUSTOMER_ID = "customerID";

    // API Cart
    public static final String REST_CART_DELETE_ITEM = "/V1/webpos/checkout/removeItem?quote_id=${quoteID}&item_id=${itemID}";
    public static final String CART_QUOTE_ID = "quoteID";
    public static final String CART_ITEM_ID = "itemID";

    // API checkout
    public static final String REST_CHECKOUT_CREATE = "/V1/webpos/checkout/create?";
    public static final String REST_CHECK_OUT_SAVE_CART = "/V1/webpos/checkout/saveCart?";
    public static final String REST_CHECKOUT_SAVE_QUOTE = "/V1/webpos/checkout/saveQuoteData?";
    public static final String REST_CHECKOUT_ADD_COUPON_TO_QUOTE = "/V1/webpos/checkout/applyCoupon?";
    public static final String REST_CHECK_OUT_SAVE_SHIPPING = "/V1/webpos/checkout/saveShippingMethod?";
    public static final String REST_CHECK_OUT_SAVE_PAYMENT = "/V1/webpos/checkout/savePaymentMethod?";
    public static final String REST_CHECK_OUT_PLACE_ORDER = "/V1/webpos/checkout/placeOrder?";
    public static final String REST_CHECK_OUT_SEND_EMAIL = "rest/default/V1/webpos/checkout/sendEmail?";

    // API register shifts
    public static final String REST_REGISTER_SHIFTS_GET_LISTING_POS = "/V1/webpos/poslist?";
    public static final String REST_REGISTER_SHIFTS_GET_LISTING = "/V1/webpos/shifts/getlist?";
    public static final String REST_REGISTER_SHIFTS_SAVE = "rest/default/V1/webpos/shifts/save?";
    public static final String REST_REGISTER_SHIFTS_MAKE_ADJUSTMENT = "/V1/webpos/cash_transaction/save?";

    // API plugins
    public static final String REST_PLUGIN_APPLY_REWARD_POINT = "/V1/webpos/integration/spendPoint?";
    public static final String REST_PLUGIN_ADD_GIFTCARD = "/V1/webpos/integration/applyGiftcard?";
    public static final String REST_PLUGIN_REMOVE_GIFTCARD = "/V1/webpos/integration/removeGiftcard?";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "/V1/webpos/configurations?";
    public static final String REST_CONFIG_TAX_CLASS = "/V1/webpos/taxclass/list?";
    public static final String REST_CONFIG_COLOR_SWATCH = "/V1/webpos/products/swatch/search?";
    public static final String REST_CONFIG_GET_ = "/V1/webpos/configurations/${configID}?path=${configPath}&session=${sessionID}";
    public static final String REST_POS_ASSIGN = "/V1/webpos/posassign?";

    // API Account
    public static final String REST_SETTING_ACCOUNT = "/V1/webpos/staff/changepassword?";
    public static final String REST_SETTING_CHANGE_CURRENCY = "/V1/webpos/currencies/change?";

    // API approved payment paypal
    public static final String REST_APPROVED_PAYMENT_PAYPAL = "/V1/webpos/paypal/finishPayment?";
    public static final String REST_INVOICE_PAYMENT_AUTHORIZE = "/V1/webpos/invoices/${orderID}/create?";

    // API get access token paypal here
    public static final String REST_GET_ACCESS_TOKEN_PAYPAL_HERE = "/V1/webpos/paypal/getAccessToken?";

    // API approved payment stripe
    public static final String REST_APPROVED_PAYMENT_STRIPE = "/V1/webpos/stripe/finishPayment?";

    // API approved authorizenet
    public static final String REST_APPROVED_PAYMENT_AUTHORIZE = "/V1/webpos/authorizenet/finishPayment?";
}
