package com.magestore.app.pos.model.checkout.cart;

import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class PosItems extends PosAbstractModel implements Items {
    Product product;
    int qty;
    String child_id;
    String bundle_option;
    int bundle_option_qty;
    float price;
    float base_price;
    float unit_price;
    float base_unit_price;
    float original_price;
    float base_original_price;
    String item_id;

    // Order history
    String name;
    String sku;
    float row_total = 0;
    float row_total_incl_tax = 0;
    float tax_amount = 0;
    float discount_amount = 0;
    int qty_ordered;
    int qty_canceled;
    int qty_invoiced;
    int qty_refunded;
    int qty_shipped;

    // Params Order Shipment
    String orderItemId;

    // Params Order Refund
    String return_to_stock;

    @Override
    public String getID() {
        return item_id;
    }

    @Override
    public void setId(String strId) {
        item_id = strId;
    }

    @Override
    public void setQuantity(int param_quantity) {
        qty = param_quantity;
    }

    @Override
    public void setProduct(Product param_product) {
        product = param_product;
    }

    @Override
    public void setPrice(float param_price) {
        price = param_price;
    }

    @Override
    public void setOriginalPrice(float param_price) {
        original_price = param_price;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public int getQuantity() {
        return qty;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public float getOriginalPrice() {
        return original_price;
    }

    // Order history
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSku() {
        return sku;
    }

    @Override
    public float getSubtotal() {
        return row_total;
    }

    @Override
    public float getRowTotal() {
        return row_total_incl_tax;
    }

    @Override
    public float getTaxAmount() {
        return tax_amount;
    }

    @Override
    public float getDiscountAmount() {
        return discount_amount;
    }

    @Override
    public int getQtyOrdered() {
        return qty_ordered;
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
    public int getQtyRefunded() {
        return qty_refunded;
    }

    @Override
    public int getQtyShipped() {
        return qty_shipped;
    }

    @Override
    public int QtyShip() {
        return qty_ordered - qty_shipped;
    }

    @Override
    public int QtyRefund() {
        return qty_ordered - qty_refunded;
    }

    @Override
    public void setOrderItemId(String strOrderItemId) {
        orderItemId = strOrderItemId;
    }

    @Override
    public String getOrderItemId() {
        return orderItemId;
    }

    @Override
    public void setReturnToStock(String strReturnToStock) {
        return_to_stock = strReturnToStock;
    }

    @Override
    public String getReturnToStock() {
        return return_to_stock;
    }
}
