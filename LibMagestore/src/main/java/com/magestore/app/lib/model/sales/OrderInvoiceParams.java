package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.CartItem;

import java.util.List;

/**
 * Created by Johan on 2/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderInvoiceParams extends Model {
    void setEmailSent(String strEmailSent);

    String getEmailSent();

    String getBaseCurrencyCode();

    void setBaseCurrencyCode(String strBaseCurrencyCode);

    float getaseDiscountAmount();

    void setBaseDiscountAmount(float baseDiscountAmount);

    float getBaseGrandTotal();

    void setBaseGrandTotal(float baseGrandTotal);

    float getBaseShippingAmount();

    void setBaseShippingAmount(float baseShippingAmount);

    float getBaseShippingInclTax();

    void setBaseShippingInclTax(float baseShippingInclTax);

    float getBaseShippingTaxAmount();

    void setBaseShippingTaxAmount(float baseShippingTaxAmount);

    float getBaseSubtotal();

    void setBaseSubtotal(float baseSubtotal);

    float getBaseSubtotalInclTax();

    void setBaseSubtotalInclTax(float baseSubtotalInclTax);

    float getBaseTaxAmount();

    void setBaseTaxAmount(float baseTaxAmount);

    String getBaseToGlobalRate();

    void setBaseToGlobalRate(String strBaseToGlobalRate);

    String getBaseToOrderRate();

    void setBaseToOrderRate(String strBaseToOrderRate);

    String getBillingAddressId();

    void setBillingAddressId(String strBillingAddressId);

    List<OrderCommentParams> getComments();

    void setComments(List<OrderCommentParams> comments);

    String getCreatedAt();

    void setCreatedAt(String strCreatedAt);

    float getDiscountAmount();

    void setDiscountAmount(float discountAmount);

    String getGlobalCurrencyCode();

    void setGlobalCurrencyCode(String strGlobalCurrencyCode);

    float getGrandTotal();

    void setGrandTotal(float grandTotal);

    String getOrderCurrencyCode();

    void setOrderCurrencyCode(String strOrderCurrencyCode);

    String getOrderId();

    void setOrderId(String strOrderId);

    String getShippingAddressId();

    void setShippingAddressId(String strShippingAddressId);

    float getShippingAmount();

    void setShippingAmount(float shippingAmount);

    float getShippingInclTax();

    void setShippingInclTax(float shippingInclTax);

    float getShippingTaxAmount();

    void setShippingTaxAmount(float shippingTaxAmount);

    String getState();

    void setState(String strState);

    String getStoreCurrencyCode();

    void setStoreCurrencyCode(String strStoreCurrencyCode);

    String getStoreId();

    void setStoreId(String strStoreId);

    String getStoreToBaseRate();

    void setStoreToBaseRate(String strStoreToBaseRate);

    String getStoreToOrderRate();

    void setStoreToOrderRate(String strStoreToOrderRate);

    float getSubtotal();

    void setSubtotal(float subtotal);

    float getSubtotalInclTax();

    void setSubtotalInclTax(float subtotalInclTax);

    float getTaxAmount();

    void setTaxAmount(float taxAmount);

    int getTotalQty();

    void setTotalQty(int totalQty);

    String getUpdatedAt();

    void setUpdatedAt(String strUpdatedAt);

    OrderAttributes getExtensionAttributes();

    void setExtensionAttributes(OrderAttributes extension_attributes);

    List<CartItem> getItems();

    void setItems(List<CartItem> items);
}
