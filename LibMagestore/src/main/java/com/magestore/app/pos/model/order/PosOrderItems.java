package com.magestore.app.pos.model.order;

import com.magestore.app.lib.model.order.OrderItems;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderItems extends PosAbstractModel implements OrderItems {
    float amount_refunded;
    float base_amount_refunded;
    float base_discount_invoiced;
    float base_discount_tax_compensation_amount;
    float base_discount_tax_compensation_invoiced;
    float base_discount_tax_compensation_refunded;
    float base_price;
    float base_row_invoiced;
    float base_row_total;
    float base_tax_invoiced;
    String created_at;
    float discount_invoiced;
    float free_shipping;
    float discount_tax_compensation_amount;
    float discount_tax_compensation_canceled;
    float discount_tax_compensation_invoiced;
    float discount_tax_compensation_refunded;
    int is_qty_decimal;
    String item_id;
    String name;
    int no_discount;
    String order_id;
    float original_price;
    float price;
    String product_id;
    String product_type;
    int qty_canceled;
    int qty_invoiced;
    int qty_ordered;
    int qty_refunded;
    int qty_shipped;
    int row_invoiced;
    int row_total;
    String sku;
    String store_id;
    float tax_invoiced;
    String updated_at;
    float weight;

    @Override
    public String getID() {
        return item_id;
    }

    @Override
    public float getAmountRefunded() {
        return amount_refunded;
    }

    @Override
    public float getBasePrice() {
        return base_price;
    }

    @Override
    public float getBaseDiscountInvoiced() {
        return base_discount_invoiced;
    }

    @Override
    public float getBaseAmountRefunded() {
        return base_amount_refunded;
    }

    @Override
    public float getBaseRowInvoiced() {
        return base_row_invoiced;
    }

    @Override
    public float getBaseRowTotal() {
        return base_row_total;
    }

    @Override
    public float getBaseTaxInvoiced() {
        return base_tax_invoiced;
    }

    @Override
    public float getDiscountInvoiced() {
        return discount_invoiced;
    }

    @Override
    public float getFreeShipping() {
        return free_shipping;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getNoDiscount() {
        return no_discount;
    }

    @Override
    public String getOrderId() {
        return order_id;
    }

    @Override
    public float getOriginalPrice() {
        return original_price;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public String getProductId() {
        return product_id;
    }

    @Override
    public String getproductType() {
        return product_type;
    }

    @Override
    public int getQtyCanceled() {
        return qty_canceled;
    }

    @Override
    public int getQtyInvoiced() {
        return qty_invoiced;
    }

    @Override
    public int getQtyOrdered() {
        return qty_ordered;
    }

    @Override
    public int getQtyRefunded() {
        return qty_refunded;
    }

    @Override
    public int getQtyShipped() {
        return qty_shipped;
    }

    @Override
    public int getRowInvoiced() {
        return row_invoiced;
    }

    @Override
    public int getRowTotal() {
        return row_total;
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public float getTaxInvoiced() {
        return tax_invoiced;
    }

    @Override
    public String getUpdatedAt() {
        return updated_at;
    }

    @Override
    public float getWeight() {
        return weight;
    }
}
