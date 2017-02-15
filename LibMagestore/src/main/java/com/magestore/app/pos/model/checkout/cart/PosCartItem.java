package com.magestore.app.pos.model.checkout.cart;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCartItem extends PosAbstractModel implements CartItem {
    Product product;
    int qty;
    @Gson2PosExclude
    String child_id;
    @Gson2PosExclude
    String bundle_option;
    float price;
    float base_price;
    @Gson2PosExclude
    float unit_price;

    // Order history
    String name;
    String sku;
    float row_total = 0;
    float row_total_incl_tax = 0;
    float tax_amount = 0;
    float discount_amount = 0;
    @Gson2PosExclude
    int qty_ordered;
    @Gson2PosExclude
    int qty_canceled;
    @Gson2PosExclude
    int qty_invoiced;
    @Gson2PosExclude
    int qty_refunded;
    @Gson2PosExclude
    int qty_shipped;
    @Gson2PosExclude
    String product_type;
    @Gson2PosExclude
    String parent_item_id;

    @Gson2PosExclude
    float base_original_price;
    @Gson2PosExclude
    float base_unit_price;
    @Gson2PosExclude
    int bundle_option_qty;
    @Gson2PosExclude
    String is_virtual;
    @Gson2PosExclude
    String item_id;
    @Gson2PosExclude
    float original_price;

    // Params Order Shipment
    String orderItemId;

    // Params Order Refund
    String return_to_stock;

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

    @Override
    public String getItemId() {
        return item_id;
    }

    @Override
    public void setItemId(String strItemId) {
        item_id = strItemId;
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
    public int QtyInvoice() {
        return qty_ordered - qty_invoiced;
    }

    @Override
    public String getProductType() {
        return product_type;
    }

    @Override
    public String getIsVirtual() {
        return is_virtual;
    }

    @Override
    public String getParentItemId() {
        return parent_item_id;
    }

    @Override
    public void setId(String strID) {
        id = strID;
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
