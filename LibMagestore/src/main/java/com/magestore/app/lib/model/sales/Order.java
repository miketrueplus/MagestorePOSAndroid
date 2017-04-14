package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
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

    OrderItemsInfoBuy getItemsInfoBuy();

    float getGiftVoucherDiscount();

    float getRewardPointsEarn();

    float getRewardPointsSpent();

    float getRewardPointsDiscount();

    float getBaseGiftVoucherDiscount();

    float getBaseDiscountAmount();

    float getBaseGrandTotal();

    float getBaseShippingAmount();

    float getBaseShippingInclTax();

    float getBaseShippingTaxAmount();

    float getBaseSubtotal();

    float getBaseSubtotalInclTax();

    float getBaseShippingRefunded();

    String getBillingAddressId();

    String getBaseToGlobalRate();

    String getBaseToOrderRate();

    String getGlobalCurrencyCode();

    String getOrderCurrencyCode();

    float getShippingInclTax();

    float getShippingTaxAmount();

    String getState();

    String getStoreId();

    String getStoreToBaseRate();

    String getStoreToOrderRate();

    float getSubtotalInclTax();

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

    float getGrandTotal();

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

    float getShippingAmount();

    float getDiscountAmount();

    float getTotalDue();

    float getTotalPaid();

    float getTotalRefunded();

    float getOrderHistorySubtotal();

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
}
