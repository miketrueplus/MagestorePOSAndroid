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

    // API Checkout
    public static final String REST_CHECK_OUT_PLACE_ORDER = "api/pos-create-order/";

    // API config
    public static final String REST_CONFIG_GET_LISTING = "api/pos-configs/";
}
