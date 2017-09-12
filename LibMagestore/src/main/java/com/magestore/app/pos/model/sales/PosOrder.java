package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderItemsInfoBuy;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;
import com.magestore.app.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Quảng lý các thông tin của order
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosOrder extends PosAbstractModel implements Order {
    String entity_id;
    int rewardpoints_earn = 0;
    int rewardpoints_spent = 0;
    float rewardpoints_discount = 0;
    float rewardpoints_base_discount = 0;
    float gift_voucher_discount = 0;
    float base_gift_voucher_discount = 0;
    float base_customercredit_discount = 0;
    float customercredit_discount = 0;
    float webpos_change = 0;
    float webpos_base_change = 0;
    private String webpos_staff_id;
    private String webpos_staff_name;
    private String webpos_delivery_date;

    private List<PosCartItem> items;
    private PosOrderItemsInfoBuy items_info_buy;

    float sub_total = 0;
    float tax_total = 0;
    float discount_total = 0;
    float last_total = 0;

    String base_currency_code;
    float base_discount_amount;
    float base_discount_invoiced;
    float base_grand_total;
    float base_discount_tax_compensation_amount;
    float base_discount_tax_compensation_invoiced;
    float base_discount_tax_compensation_refunded;
    float base_shipping_amount;
    float base_shipping_discount_amount;
    float base_shipping_discount_tax_compensation_amnt;
    float base_shipping_incl_tax;
    float base_shipping_invoiced;
    float base_shipping_refunded;
    float base_shipping_tax_amount;
    float base_subtotal;
    float base_subtotal_incl_tax;
    float base_subtotal_invoiced;
    float base_tax_amount;
    float base_tax_invoiced;
    float base_total_due;
    float base_total_invoiced;
    float base_total_invoiced_cost;
    float base_total_refunded;
    float base_total_paid;
    String base_to_global_rate;
    String base_to_order_rate;
    String billing_address_id;
    String created_at;
    String customer_email;
    String customer_firstname;
    String customer_gender;
    String customer_group_id;
    String customer_id;
    String customer_lastname;
    float discount_amount;
    float discount_invoiced;
    String email_sent;
    String global_currency_code;
    float grand_total = 0;
    float discount_tax_compensation_amount;
    float discount_tax_compensation_invoiced;
    float discount_tax_compensation_refunded;
    String increment_id;
    String invoice_id;
    String is_virtual;
    String order_currency_code;
    String protect_code;
    String quote_id;
    float shipping_amount;
    String shipping_description;
    float shipping_discount_amount;
    float shipping_discount_tax_compensation_amount;
    float shipping_incl_tax;
    float shipping_invoiced;
    float shipping_tax_amount;
    String state;
    String status;
    String store_currency_code;
    String store_id;
    String store_name;
    String store_to_base_rate;
    String store_to_order_rate;
    float subtotal;
    float subtotal_incl_tax;
    float subtotal_invoiced;
    float tax_amount;
    float tax_invoiced;
    float total_due;
    float total_refunded;
    float total_invoiced;
    float total_item_count;
    float total_paid;
    float total_qty_ordered;
    String updated_at;
    String weight;
    String total_count;
    PosOrderBillingAddress billing_address;
    PosOrderPayment payment;
    PosOrderAttributes extension_attributes;
    List<PosOrderWebposPayment> webpos_order_payments;
    List<PosOrderStatus> status_histories;

    // param request add comment history;
    PosOrderStatus param_status;

    // param request create shipment
    PosOrderShipmentParams param_shipment;

    // Param request refund
    PosOrderRefundParams param_refund;

    // Param request invoice
    PosOrderInvoiceParams param_invoice;

    // Param request cancel
    PosOrderCommentParams param_cancel;

    // list product re-order
    List<Product> list_product_reorder;

    @Gson2PosExclude
    float real_amount;
    @Gson2PosExclude
    float remain_money;
    @Gson2PosExclude
    float exchange_money;
    @Gson2PosExclude
    float max_storecredit_refunded;
    @Gson2PosExclude
    float total_price_change_qty_refund;
    @Gson2PosExclude
    float adjust_refund;
    @Gson2PosExclude
    float adjust_free;
    @Gson2PosExclude
    float refund_shipping;
    @Gson2PosExclude
    float store_credit_refund;
    @Gson2PosExclude
    boolean check_request_update_invoice;
    @Gson2PosExclude
    float max_giftcard_refund;
    @Gson2PosExclude
    float gift_card_refund;
    @Gson2PosExclude
    float max_refunded;

    @Gson2PosExclude
    boolean isCreatAtView;

    @Override
    public String getID() {
        return entity_id;
    }

    @Override
    public void setID(String id) {
        super.setID(id);
        entity_id = id;
    }

    @Override
    public Order newInstance() {
        return new PosOrder();
    }

    @Override
    public List<CartItem> getOrderItems() {
        return (List<CartItem>) (List<?>) items;
    }

    @Override
    public void setOrderItem(List<CartItem> listTtem) {
        items = (List<PosCartItem>) (List<?>) listTtem;
    }

    @Override
    public OrderItemsInfoBuy getItemsInfoBuy() {
        return items_info_buy;
    }

    @Override
    public float getWebposChange() {
        return webpos_change;
    }

    @Override
    public void setWebposChange(float fWebposChange) {
        webpos_base_change = fWebposChange;
    }

    @Override
    public float getWebposBaseChange() {
        return webpos_base_change;
    }

    @Override
    public void setWebposBaseChange(float fWebposBaseChange) {
        webpos_base_change = fWebposBaseChange;
    }

    @Override
    public float getGiftVoucherDiscount() {
        return gift_voucher_discount;
    }

    @Override
    public int getRewardPointsEarn() {
        return rewardpoints_earn;
    }

    @Override
    public int getRewardPointsSpent() {
        return rewardpoints_spent;
    }

    @Override
    public float getRewardPointsDiscount() {
        return rewardpoints_discount;
    }

    @Override
    public float getRewardPointsBaseDiscount() {
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_1)) {
            if (rewardpoints_base_discount > 0) {
                return (0 - rewardpoints_base_discount);
            }
        }
        return rewardpoints_base_discount;
    }

    @Override
    public float getBaseGiftVoucherDiscount() {
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_1)) {
            if (base_gift_voucher_discount > 0) {
                return (0 - base_gift_voucher_discount);
            }
        }
        return base_gift_voucher_discount;
    }

    @Override
    public float getBaseDiscountAmount() {
        return base_discount_amount;
    }

    @Override
    public void setBaseDiscountAmount(float fBaseDiscountAmount) {
        base_discount_amount = fBaseDiscountAmount;
    }

    @Override
    public float getBaseGrandTotal() {
        return base_grand_total;
    }

    @Override
    public void setBaseGrandTotal(float fBaseGrandTotal) {
        base_grand_total = fBaseGrandTotal;
    }

    @Override
    public float getBaseShippingAmount() {
        return base_shipping_amount;
    }

    @Override
    public void setBaseShippingAmount(float fBaseShippingAmount) {
        base_shipping_amount = fBaseShippingAmount;
    }

    @Override
    public float getBaseShippingInclTax() {
        return base_shipping_incl_tax;
    }

    @Override
    public void setBaseShippingInclTax(float fBaseShippingInclTax) {
        base_shipping_incl_tax = fBaseShippingInclTax;
    }

    @Override
    public float getBaseShippingTaxAmount() {
        return base_shipping_tax_amount;
    }

    @Override
    public void setBaseShippingTaxAmount(float fBaseShippingTaxAmount) {
        base_shipping_tax_amount = fBaseShippingTaxAmount;
    }

    @Override
    public float getBaseTaxAmount() {
        return base_tax_amount;
    }

    @Override
    public void setBaseTaxAmount(float fBaseTaxAmount) {
        base_tax_amount = fBaseTaxAmount;
    }

    @Override
    public float getBaseSubtotal() {
        return base_subtotal;
    }

    @Override
    public void setBaseSubtotal(float fBaseSubtotal) {
        base_subtotal = fBaseSubtotal;
    }

    @Override
    public float getBaseSubtotalInclTax() {
        return base_subtotal_incl_tax;
    }

    @Override
    public void setBaseSubtotalInclTax(float fBaseSubtotalInclTax) {
        base_subtotal_incl_tax = fBaseSubtotalInclTax;
    }

    @Override
    public float getBaseShippingRefunded() {
        return base_shipping_refunded;
    }

    @Override
    public String getBillingAddressId() {
        return billing_address_id;
    }

    @Override
    public String getBaseToGlobalRate() {
        return base_to_global_rate;
    }

    @Override
    public String getBaseToOrderRate() {
        return base_to_order_rate;
    }

    @Override
    public String getGlobalCurrencyCode() {
        return global_currency_code;
    }

    @Override
    public String getOrderCurrencyCode() {
        return order_currency_code;
    }

    @Override
    public float getShippingInclTax() {
        return shipping_incl_tax;
    }

    @Override
    public void setShippingInclTax(float fShippingInclTax) {
        shipping_incl_tax = fShippingInclTax;
    }

    @Override
    public float getShippingTaxAmount() {
        return shipping_tax_amount;
    }

    @Override
    public void setShippingTaxAmount(float fShippingTaxAmount) {
        shipping_tax_amount = fShippingTaxAmount;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public String getStoreToBaseRate() {
        return store_to_base_rate;
    }

    @Override
    public String getStoreToOrderRate() {
        return store_to_order_rate;
    }

    @Override
    public float getSubtotalInclTax() {
        return subtotal_incl_tax;
    }

    @Override
    public void setSubtotalInclTax(float fSubtotalInclTax) {
        subtotal_incl_tax = fSubtotalInclTax;
    }

    @Override
    public float getTotalQtyOrdered() {
        return total_qty_ordered;
    }

    @Override
    public float getSubTotal() {
        return sub_total;
    }

    @Override
    public float getDiscountTotal() {
        return discount_total;
    }

    @Override
    public float getLastTotal() {
        return last_total;
    }

    @Override
    public float getTaxTotal() {
        return tax_total;
    }

    @Override
    public PosOrderAttributes getExtensionAttributes() {
        return extension_attributes;
    }

    @Override
    public PosOrderBillingAddress getBillingAddress() {
        return billing_address;
    }

    @Override
    public PosOrderPayment getPayment() {
        return payment;
    }

    @Override
    public String getInvoiceId() {
        return invoice_id;
    }

    @Override
    public String getIncrementId() {
        return increment_id;
    }

    @Override
    public void setIncrementId(String strIncrementId) {
        increment_id = strIncrementId;
    }

    @Override
    public String getCustomerFirstname() {
        return customer_firstname;
    }

    @Override
    public String getCustomerLastname() {
        return customer_lastname;
    }

    @Override
    public String getCustomerId() {
        return customer_id;
    }

    @Override
    public float getGrandTotal() {
        return grand_total;
    }

    @Override
    public void setGrandTotal(float fGrandTotal) {
        grand_total = fGrandTotal;
    }

    @Override
    public String getCreatedAt() {
        return created_at;
    }

    @Override
    public void setCreateAt(String strCreateAt) {
        created_at = strCreateAt;
    }

    @Override
    public String getCustomerEmail() {
        return customer_email;
    }

    @Override
    public void setCustomerEmail(String strEmail) {
        customer_email = strEmail;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String strStatus) {
        status = strStatus;
    }

    @Override
    public String getWebposStaffId() {
        return webpos_staff_id;
    }

    @Override
    public void setWebposStaffId(String strWebposStaffId) {
        webpos_staff_id = strWebposStaffId;
    }

    @Override
    public String getWebposStaffName() {
        return webpos_staff_name;
    }

    @Override
    public void setWebposStaffName(String strWebposStaffName) {
        webpos_staff_name = strWebposStaffName;
    }

    @Override
    public String getWebposDeliveryDate() {
        return webpos_delivery_date;
    }

    @Override
    public String getBillingAddressName() {
        if (billing_address != null) {
            return billing_address.getName();
        }
        return "";
    }

    @Override
    public String getBillingAddressFullAddress() {
        if (billing_address != null) {
            return billing_address.getFullAddress();
        }
        return "";
    }

    @Override
    public String getBillingAddressTelephone() {
        if (billing_address != null) {
            return billing_address.getTelephone();
        }
        return "";
    }

    @Override
    public String getShippingAddressName() {
        PosOrderShippingAddress shippingAddress = getShippingAddress();
        if (shippingAddress != null) {
            return shippingAddress.getName();
        }
        return "";
    }

    @Override
    public String getShippingFullAddress() {
        PosOrderShippingAddress shippingAddress = getShippingAddress();
        if (shippingAddress != null) {
            return shippingAddress.getFullAddress();
        }
        return "";
    }

    @Override
    public String getShippingTelePhone() {
        PosOrderShippingAddress shippingAddress = getShippingAddress();
        if (shippingAddress != null) {
            return shippingAddress.getTelephone();
        }
        return "";
    }

    @Override
    public String getShippingDescription() {
        return shipping_description;
    }

    @Override
    public List<OrderWebposPayment> getWebposOrderPayments() {
        return (List<OrderWebposPayment>) (List<?>) webpos_order_payments;
    }

    @Override
    public List<OrderStatus> getOrderStatus() {
        List<OrderStatus> list_status = new ArrayList<OrderStatus>();
        if (status_histories != null && status_histories.size() > 0) {
            for (OrderStatus status : status_histories) {
                if (status.getComment() != null && !status.getComment().equals("")) {
                    list_status.add(status);
                }
            }
        }
        return (List<OrderStatus>) (List<?>) list_status;
    }

    @Override
    public boolean checkComment() {
        String mStatus = "";
        if (status_histories != null && status_histories.size() > 0) {
            for (OrderStatus status : status_histories) {
                if (status.getComment() != null && !status.getComment().equals("")) {
                    mStatus = mStatus + status.getComment();
                }
            }
        }

        if (!mStatus.equals("")) {
            return true;
        }
        return false;
    }

    @Override
    public float getTaxAmount() {
        return tax_amount;
    }

    @Override
    public void setTaxAmount(float fTaxAmount) {
        tax_amount = fTaxAmount;
    }

    @Override
    public float getShippingAmount() {
        return shipping_amount;
    }

    @Override
    public void setShippingAmount(float fShippingAmount) {
        shipping_amount = fShippingAmount;
    }

    @Override
    public float getDiscountAmount() {
        return discount_amount;
    }

    @Override
    public void setDiscountAmount(float fDiscountAmount) {
        discount_amount = fDiscountAmount;
    }

    @Override
    public float getTotalDue() {
        return total_due;
    }

    @Override
    public float getBaseTotalDue() {
        return base_total_due;
    }

    @Override
    public float getBaseTotalInvoiced() {
        return base_total_invoiced;
    }

    @Override
    public float getTotalPaid() {
        return total_paid;
    }

    @Override
    public void setTotalPaid(float fTotalPaid) {
        total_paid = fTotalPaid;
    }

    @Override
    public float getBaseTotalPaid() {
        return base_total_paid;
    }

    @Override
    public void setBaseTotalPaid(float fBaseTotalPaid) {
        base_total_paid = fBaseTotalPaid;
    }

    @Override
    public float getTotalRefunded() {
        return total_refunded;
    }

    @Override
    public float getBaseTotalRefunded() {
        return base_total_refunded;
    }

    @Override
    public float getOrderHistorySubtotal() {
        return subtotal;
    }

    @Override
    public void setOrderHistorySubtotal(float fOrderHistorySubtotal) {
        subtotal = fOrderHistorySubtotal;
    }

    @Override
    public boolean checkListShipment() {
        boolean ischeck = false;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                if (item.QtyShip() > 0) {
                    ischeck = true;
                }
            }
        }
        if (ischeck) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkListRefund() {
        boolean ischeck = false;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                if (item.QtyRefund() > 0) {
                    ischeck = true;
                }
            }
        }
        if (ischeck) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkListInvoice() {
        boolean ischeck = false;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                if (item.QtyInvoice() > 0) {
                    ischeck = true;
                }
            }
        }
        if (ischeck) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getBaseCurrencyCode() {
        return base_currency_code;
    }

    @Override
    public String getStoreCurrencyCode() {
        return store_currency_code;
    }

    @Override
    public float getTotalInvoiced() {
        return total_invoiced;
    }

    @Override
    public String getIsVirtual() {
        return is_virtual;
    }

    @Override
    public OrderStatus getParamStatus() {
        return param_status;
    }

    @Override
    public void setParamStatus(OrderStatus orderStatus) {
        param_status = (PosOrderStatus) orderStatus;
    }

    @Override
    public OrderShipmentParams getParamShipment() {
        return param_shipment;
    }

    @Override
    public void setParamShipment(OrderShipmentParams paramShipment) {
        param_shipment = (PosOrderShipmentParams) paramShipment;
    }

    @Override
    public OrderRefundParams getParamRefund() {
        return param_refund;
    }

    @Override
    public void setParamRefund(OrderRefundParams paramRefund) {
        param_refund = (PosOrderRefundParams) paramRefund;
    }

    @Override
    public OrderInvoiceParams getParamInvoice() {
        return param_invoice;
    }

    @Override
    public void setParamInvoice(OrderInvoiceParams paramInvoice) {
        param_invoice = (PosOrderInvoiceParams) paramInvoice;
    }

    @Override
    public OrderCommentParams getParamCancel() {
        return param_cancel;
    }

    @Override
    public void setParamCancel(OrderCommentParams paramCancel) {
        param_cancel = (PosOrderCommentParams) paramCancel;
    }

    @Override
    public List<CartItem> newOrderItems() {
        items = new ArrayList<PosCartItem>();
        return (List<CartItem>) (List<?>) items;
    }

    private PosOrderShippingAddress getShippingAddress() {
        PosOrderShippingAddress shippingAddress = null;
        if (extension_attributes != null) {
            List<PosOrderShippingAssignments> shipping_assignments = extension_attributes.getShippingAssignments();
            if (shipping_assignments != null && shipping_assignments.size() > 0) {
                if (shipping_assignments.size() >= 2) {
                    shippingAddress = shipping_assignments.get(shipping_assignments.size() - 1).getShipping().getAddress();
                } else {
                    shippingAddress = shipping_assignments.get(0).getShipping().getAddress();
                }
            }
        }
        return shippingAddress;
    }

    @Override
    public String getDisplayContent() {
        return getIncrementId();
    }

    @Override
    public String getSubDisplayContent() {
        if (getCustomerFirstname() != null) {
            String name = getCustomerFirstname() + " " + getCustomerLastname();
            return name;
        } else {
            return getCustomerEmail();
        }
    }

    @Override
    public float getRealAmount() {
        return real_amount;
    }

    @Override
    public void setRealAmount(float fRealAmount) {
        real_amount = fRealAmount;
    }

    @Override
    public float getRemainMoney() {
        return remain_money;
    }

    @Override
    public void setRemainMoney(float fRemainMoney) {
        remain_money = fRemainMoney;
    }

    @Override
    public float getExchangeMoney() {
        return exchange_money;
    }

    @Override
    public void setExchangeMoney(float fExchangeMoney) {
        exchange_money = fExchangeMoney;
    }

    @Override
    public List<Product> getListProductReorder() {
        return list_product_reorder;
    }

    @Override
    public void setListProductReorder(List<Product> listProduct) {
        list_product_reorder = listProduct;
    }

    @Override
    public float getMaxRefunded() {
        return max_refunded;
    }

    @Override
    public void setMaxRefunded(float maxRefunded) {
        max_refunded = maxRefunded;
    }

    @Override
    public float getMaxStoreCreditRefund() {
        return max_storecredit_refunded;
    }

    @Override
    public void setMaxStoreCreditRefund(float fMaxStoreCreditRefund) {
        max_storecredit_refunded = fMaxStoreCreditRefund;
    }

    @Override
    public float getTotalPriceChangeQtyRefund() {
        return total_price_change_qty_refund;
    }

    @Override
    public void setTotalPriceChangeQtyRefund(float fTotalPriceChangeQtyRefund) {
        total_price_change_qty_refund = fTotalPriceChangeQtyRefund;
    }

    @Override
    public float getAdjustRefund() {
        return adjust_refund;
    }

    @Override
    public void setAdjustRefund(float fAdjustRefund) {
        adjust_refund = fAdjustRefund;
    }

    @Override
    public float getAdjustFree() {
        return adjust_free;
    }

    @Override
    public void setAdjustFree(float fAdjustFree) {
        adjust_free = fAdjustFree;
    }

    @Override
    public float getRefundShipping() {
        return refund_shipping;
    }

    @Override
    public void setRefundShipping(float fRefundShipping) {
        refund_shipping = fRefundShipping;
    }

    @Override
    public float getStoreCreditRefund() {
        return store_credit_refund;
    }

    @Override
    public void setStoreCreditRefund(float fStoreCreditRefund) {
        store_credit_refund = fStoreCreditRefund;
    }

    @Override
    public float getMaxGiftCardRefund() {
        return max_giftcard_refund;
    }

    @Override
    public void setMaxGiftCardRefund(float fMaxGiftCardRefund) {
        max_giftcard_refund = fMaxGiftCardRefund;
    }

    @Override
    public float getGiftCardRefund() {
        return gift_card_refund;
    }

    @Override
    public void setGiftCardRefund(float fGiftCardRefund) {
        gift_card_refund = fGiftCardRefund;
    }

    @Override
    public boolean checkRequestUpdateInvoice() {
        return check_request_update_invoice;
    }

    @Override
    public void setCheckRequestUpdateInvoice(boolean bRequestUpdateInvoice) {
        check_request_update_invoice = bRequestUpdateInvoice;
    }

    @Override
    public boolean IsCreateAtView() {
        return isCreatAtView;
    }

    @Override
    public void setIsCreateAtView(boolean bIsCreateAtView) {
        isCreatAtView = bIsCreateAtView;
    }
}
