package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;

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
    float rewardpoints_earn = 0;
    float rewardpoints_spent = 0;
    float rewardpoints_discount = 0;
    float rewardpoints_base_discount = 0;
    float gift_voucher_discount = 0;
    float base_gift_voucher_discount = 0;
    float base_customercredit_discount = 0;
    float customercredit_discount = 0;
    private String webpos_staff_id;
    private String webpos_staff_name;

    private List<PosCartItem> items;

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
    float base_shipping_tax_amount;
    float base_subtotal;
    float base_subtotal_incl_tax;
    float base_subtotal_invoiced;
    float base_tax_amount;
    float base_tax_invoiced;
    float base_total_due;
    float base_total_invoiced;
    float base_total_invoiced_cost;
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
    float total_invoiced;
    float total_item_count;
    float total_paid;
    int total_qty_ordered;
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

    @Override
    public String getID() {
        return entity_id;
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
    public float getBaseDiscountAmount() {
        return base_discount_amount;
    }

    @Override
    public float getBaseGrandTotal() {
        return base_grand_total;
    }

    @Override
    public float getBaseShippingAmount() {
        return base_shipping_amount;
    }

    @Override
    public float getBaseShippingInclTax() {
        return base_shipping_incl_tax;
    }

    @Override
    public float getBaseShippingTaxAmount() {
        return base_shipping_tax_amount;
    }

    @Override
    public float getBaseSubtotal() {
        return base_subtotal;
    }

    @Override
    public float getBaseSubtotalInclTax() {
        return base_subtotal_incl_tax;
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
    public float getShippingTaxAmount() {
        return shipping_tax_amount;
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
    public int getTotalQtyOrdered() {
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
    public String getIncrementId() {
        return increment_id;
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
    public float getGrandTotal() {
        return grand_total;
    }

    @Override
    public String getCreatedAt() {
        return created_at;
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
    public String getWebposStaffName() {
        return webpos_staff_name;
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
    public float getShippingAmount() {
        return shipping_amount;
    }

    @Override
    public float getDiscountAmount() {
        return discount_amount;
    }

    @Override
    public float getTotalPaid() {
        return total_paid;
    }

    @Override
    public float getOrderHistorySubtotal() {
        return subtotal;
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
}
