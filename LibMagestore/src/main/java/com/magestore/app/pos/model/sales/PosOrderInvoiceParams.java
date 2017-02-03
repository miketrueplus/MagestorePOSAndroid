package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosItems;

import java.util.List;

/**
 * Created by Johan on 2/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderInvoiceParams extends PosAbstractModel implements OrderInvoiceParams {
    String baseCurrencyCode;
    float baseDiscountAmount;
    float baseGrandTotal;
    float baseShippingAmount;
    float baseShippingInclTax;
    float baseShippingTaxAmount;
    float baseSubtotal;
    float baseSubtotalInclTax;
    float baseTaxAmount;
    String baseToGlobalRate;
    String baseToOrderRate;
    String billingAddressId;
    List<OrderCommentParams> comments;
    String createdAt;
    float discountAmount;
    String globalCurrencyCode;
    float grandTotal;
    String orderCurrencyCode;
    String orderId;
    String shippingAddressId;
    float shippingAmount;
    float shippingInclTax;
    float shippingTaxAmount;
    String state;
    String storeCurrencyCode;
    String storeId;
    String storeToBaseRate;
    String storeToOrderRate;
    float subtotal;
    float subtotalInclTax;
    float taxAmount;
    int totalQty;
    String updatedAt;
    PosOrderAttributes extension_attributes;
    List<PosItems> items;
}
