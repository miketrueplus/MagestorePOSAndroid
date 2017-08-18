package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.OrderAttributes;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 2/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderInvoiceParams extends PosAbstractModel implements OrderInvoiceParams {
    String emailSent;
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
    float totalQty;
    String updatedAt;
    OrderAttributes extension_attributes;
    List<CartItem> items;

    @Override
    public void setEmailSent(String strEmailSent) {
        emailSent = strEmailSent;
    }

    @Override
    public String getEmailSent() {
        return emailSent;
    }

    @Override
    public String getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    @Override
    public void setBaseCurrencyCode(String strBaseCurrencyCode) {
        baseCurrencyCode = strBaseCurrencyCode;
    }

    @Override
    public float getBaseDiscountAmount() {
        return baseDiscountAmount;
    }

    @Override
    public void setBaseDiscountAmount(float baseDiscountAmount) {
        this.baseDiscountAmount = baseDiscountAmount;
    }

    @Override
    public float getBaseGrandTotal() {
        return baseGrandTotal;
    }

    @Override
    public void setBaseGrandTotal(float baseGrandTotal) {
        this.baseGrandTotal = baseGrandTotal;
    }

    @Override
    public float getBaseShippingAmount() {
        return baseShippingAmount;
    }

    @Override
    public void setBaseShippingAmount(float baseShippingAmount) {
        this.baseShippingAmount = baseShippingAmount;
    }

    @Override
    public float getBaseShippingInclTax() {
        return baseShippingInclTax;
    }

    @Override
    public void setBaseShippingInclTax(float baseShippingInclTax) {
        this.baseShippingInclTax = baseShippingInclTax;
    }

    @Override
    public float getBaseShippingTaxAmount() {
        return baseShippingTaxAmount;
    }

    @Override
    public void setBaseShippingTaxAmount(float baseShippingTaxAmount) {
        this.baseShippingTaxAmount = baseShippingTaxAmount;
    }

    @Override
    public float getBaseSubtotal() {
        return baseSubtotal;
    }

    @Override
    public void setBaseSubtotal(float baseSubtotal) {
        this.baseSubtotal = baseSubtotal;
    }

    @Override
    public float getBaseSubtotalInclTax() {
        return baseSubtotalInclTax;
    }

    @Override
    public void setBaseSubtotalInclTax(float baseSubtotalInclTax) {
        this.baseSubtotalInclTax = baseSubtotalInclTax;
    }

    @Override
    public float getBaseTaxAmount() {
        return baseTaxAmount;
    }

    @Override
    public void setBaseTaxAmount(float baseTaxAmount) {
        this.baseTaxAmount = baseTaxAmount;
    }

    @Override
    public String getBaseToGlobalRate() {
        return baseToGlobalRate;
    }

    @Override
    public void setBaseToGlobalRate(String strBaseToGlobalRate) {
        baseToGlobalRate = strBaseToGlobalRate;
    }

    @Override
    public String getBaseToOrderRate() {
        return baseToOrderRate;
    }

    @Override
    public void setBaseToOrderRate(String strBaseToOrderRate) {
        baseToOrderRate = strBaseToOrderRate;
    }

    @Override
    public String getBillingAddressId() {
        return billingAddressId;
    }

    @Override
    public void setBillingAddressId(String strBillingAddressId) {
        billingAddressId = strBillingAddressId;
    }

    @Override
    public List<OrderCommentParams> getComments() {
        return comments;
    }

    @Override
    public void setComments(List<OrderCommentParams> comments) {
        this.comments = comments;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(String strCreatedAt) {
        createdAt = strCreatedAt;
    }

    @Override
    public float getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String getGlobalCurrencyCode() {
        return globalCurrencyCode;
    }

    @Override
    public void setGlobalCurrencyCode(String strGlobalCurrencyCode) {
        globalCurrencyCode = strGlobalCurrencyCode;
    }

    @Override
    public float getGrandTotal() {
        return grandTotal;
    }

    @Override
    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    @Override
    public String getOrderCurrencyCode() {
        return orderCurrencyCode;
    }

    @Override
    public void setOrderCurrencyCode(String strOrderCurrencyCode) {
        orderCurrencyCode = strOrderCurrencyCode;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(String strOrderId) {
        orderId = strOrderId;
    }

    @Override
    public String getShippingAddressId() {
        return shippingAddressId;
    }

    @Override
    public void setShippingAddressId(String strShippingAddressId) {
        shippingAddressId = strShippingAddressId;
    }

    @Override
    public float getShippingAmount() {
        return shippingAmount;
    }

    @Override
    public void setShippingAmount(float shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    @Override
    public float getShippingInclTax() {
        return shippingInclTax;
    }

    @Override
    public void setShippingInclTax(float shippingInclTax) {
        this.shippingInclTax = shippingInclTax;
    }

    @Override
    public float getShippingTaxAmount() {
        return shippingTaxAmount;
    }

    @Override
    public void setShippingTaxAmount(float shippingTaxAmount) {
        this.shippingTaxAmount = shippingTaxAmount;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String strState) {
        state = strState;
    }

    @Override
    public String getStoreCurrencyCode() {
        return storeCurrencyCode;
    }

    @Override
    public void setStoreCurrencyCode(String strStoreCurrencyCode) {
        storeCurrencyCode = strStoreCurrencyCode;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void setStoreId(String strStoreId) {
        storeId = strStoreId;
    }

    @Override
    public String getStoreToBaseRate() {
        return storeToBaseRate;
    }

    @Override
    public void setStoreToBaseRate(String strStoreToBaseRate) {
        storeToBaseRate = strStoreToBaseRate;
    }

    @Override
    public String getStoreToOrderRate() {
        return storeToOrderRate;
    }

    @Override
    public void setStoreToOrderRate(String strStoreToOrderRate) {
        storeToOrderRate = strStoreToOrderRate;
    }

    @Override
    public float getSubtotal() {
        return subtotal;
    }

    @Override
    public void setSubtotal(float subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public float getSubtotalInclTax() {
        return subtotalInclTax;
    }

    @Override
    public void setSubtotalInclTax(float subtotalInclTax) {
        this.subtotalInclTax = subtotalInclTax;
    }

    @Override
    public float getTaxAmount() {
        return taxAmount;
    }

    @Override
    public void setTaxAmount(float taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Override
    public float getTotalQty() {
        return totalQty;
    }

    @Override
    public void setTotalQty(float totalQty) {
        this.totalQty = totalQty;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(String strUpdatedAt) {
        updatedAt = strUpdatedAt;
    }

    @Override
    public OrderAttributes getExtensionAttributes() {
        return extension_attributes;
    }

    @Override
    public void setExtensionAttributes(OrderAttributes extension_attributes) {
        this.extension_attributes = extension_attributes;
    }

    @Override
    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
