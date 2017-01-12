package com.magestore.app.pos.model.order;

import com.magestore.app.lib.model.order.Order;
import com.magestore.app.lib.model.order.OrderItems;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Cấu trúc dữ liệu cho orders
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrder extends PosAbstractModel implements Order {
    String entity_id;
    float rewardpoints_earn;
    float rewardpoints_spent;
    float rewardpoints_discount;
    float rewardpoints_base_discount;
    float gift_voucher_discount;
    float base_gift_voucher_discount;
    float base_customercredit_discount;
    float customercredit_discount;
    String webpos_base_change;
    String webpos_change;
    String webpos_staff_id;
    String webpos_staff_name;
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
    float base_to_global_rate;
    float base_to_order_rate;
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
    float grand_total;
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
    float subtotal;
    float subtotal_incl_tax;
    float subtotal_invoiced;
    float tax_amount;
    float tax_invoiced;
    float total_due;
    float total_invoiced;
    float total_item_count;
    float total_paid;
    String total_qty_ordered;
    String updated_at;
    String weight;
    String total_count;
    PosOrderBillingAddress billing_address;
    PosOrderPayment payment;
    List<OrderItems> items;
    PosOrderAttributes extension_attributes;

    @Override
    public String getID() {
        return entity_id;
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
    public List<OrderItems> getItems() {
        return items;
    }

    @Override
    public PosOrderAttributes getExtensionAttributes() {
        return extension_attributes;
    }
}
