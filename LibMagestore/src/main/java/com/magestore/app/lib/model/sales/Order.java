package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.pos.model.sales.PosOrderAttributes;
import com.magestore.app.pos.model.sales.PosOrderBillingAddress;
import com.magestore.app.pos.model.sales.PosOrderPayment;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Order extends Model {
    Order newInstance();

    List<CartItem> newOrderItems();

    List<CartItem> getOrderItems();

    void setOrderItem(List<CartItem> listTtem);

    OrderItemsInfoBuy getItemsInfoBuy();

    float getWebposChange();

    float getWebposBaseChange();

    float getGiftVoucherDiscount();

    float getRewardPointsEarn();

    float getRewardPointsSpent();

    float getRewardPointsDiscount();

    float getBaseGiftVoucherDiscount();

    float getBaseDiscountAmount();
    void setBaseDiscountAmount(float fBaseDiscountAmount);

    float getBaseGrandTotal();
    void setBaseGrandTotal(float fBaseGrandTotal);

    float getBaseShippingAmount();
    void setBaseShippingAmount(float fBaseShippingAmount);

    float getBaseShippingInclTax();
    void setBaseShippingInclTax(float fBaseShippingInclTax);

    float getBaseShippingTaxAmount();
    void setBaseShippingTaxAmount(float fBaseShippingTaxAmount);

    float getBaseTaxAmount();
    void setBaseTaxAmount(float fBaseTaxAmount);

    float getBaseSubtotal();
    void setBaseSubtotal(float fBaseSubtotal);

    float getBaseSubtotalInclTax();
    void setBaseSubtotalInclTax(float fBaseSubtotalInclTax);

    float getBaseShippingRefunded();

    String getBillingAddressId();

    String getBaseToGlobalRate();

    String getBaseToOrderRate();

    String getGlobalCurrencyCode();

    String getOrderCurrencyCode();

    float getShippingInclTax();
    void setShippingInclTax(float fShippingInclTax);

    float getShippingTaxAmount();
    void setShippingTaxAmount(float fShippingTaxAmount);

    String getState();

    String getStoreId();

    String getStoreToBaseRate();

    String getStoreToOrderRate();

    float getSubtotalInclTax();
    void setSubtotalInclTax(float fSubtotalInclTax);

    int getTotalQtyOrdered();

    float getSubTotal();

    float getDiscountTotal();

    float getLastTotal();

    float getTaxTotal();

    PosOrderAttributes getExtensionAttributes();

    PosOrderBillingAddress getBillingAddress();

    PosOrderPayment getPayment();

    String getIncrementId();

    String getCustomerFirstname();

    String getCustomerLastname();

    String getCustomerId();

    float getGrandTotal();
    void setGrandTotal(float fGrandTotal);

    String getCreatedAt();

    String getCustomerEmail();

    void setCustomerEmail(String strEmail);

    String getStatus();

    String getWebposStaffName();

    String getWebposDeliveryDate();

    String getBillingAddressName();

    String getBillingAddressFullAddress();

    String getBillingAddressTelephone();

    String getShippingAddressName();

    String getShippingFullAddress();

    String getShippingTelePhone();

    String getShippingDescription();

    List<OrderWebposPayment> getWebposOrderPayments();

    List<OrderStatus> getOrderStatus();

    boolean checkComment();

    float getTaxAmount();
    void setTaxAmount(float fTaxAmount);

    float getShippingAmount();
    void setShippingAmount(float fShippingAmount);

    float getDiscountAmount();
    void setDiscountAmount(float fDiscountAmount);

    float getTotalDue();

    float getTotalPaid();

    float getBaseTotalPaid();

    float getTotalRefunded();

    float getBaseTotalRefunded();

    float getOrderHistorySubtotal();
    void setOrderHistorySubtotal(float fOrderHistorySubtotal);

    boolean checkListShipment();

    boolean checkListRefund();

    boolean checkListInvoice();

    String getBaseCurrencyCode();

    String getStoreCurrencyCode();

    float getTotalInvoiced();

    String getIsVirtual();

    // param add comment history
    OrderStatus getParamStatus();

    void setParamStatus(OrderStatus orderStatus);

    // param create shipment
    OrderShipmentParams getParamShipment();

    void setParamShipment(OrderShipmentParams paramShipment);

    // param refund
    OrderRefundParams getParamRefund();

    void setParamRefund(OrderRefundParams paramRefund);

    // param invoice
    OrderInvoiceParams getParamInvoice();

    void setParamInvoice(OrderInvoiceParams paramInvoice);

    // param cancel
    OrderCommentParams getParamCancel();

    void setParamCancel(OrderCommentParams paramCancel);

    float getRealAmount();
    void setRealAmount(float fRealAmount);

    float getRemainMoney();
    void setRemainMoney(float fRemainMoney);

    float getExchangeMoney();
    void setExchangeMoney(float fExchangeMoney);

    List<Product> getListProductReorder();
    void setListProductReorder(List<Product> listProduct);

    float getMaxRefunded();

    float getMaxStoreCreditRefund();

    void setMaxStoreCreditRefund(float fMaxStoreCreditRefund);

    float getTotalPriceChangeQtyRefund();
    void setTotalPriceChangeQtyRefund(float fTotalPriceChangeQtyRefund);

    float getAdjustRefund();
    void setAdjustRefund(float fAdjustRefund);

    float getAdjustFree();
    void setAdjustFree(float fAdjustFree);

    float getRefundShipping();
    void setRefundShipping(float fRefundShipping);

    float getStoreCreditRefund();
    void setStoreCreditRefund(float fStoreCreditRefund);

    boolean checkRequestUpdateInvoice();
    void setCheckRequestUpdateInvoice(boolean bRequestUpdateInvoice);
}
