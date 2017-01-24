package com.magestore.app.lib.model.checkout.cart;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Items extends Model {
    void setQuantity(int param_quantity);
    void setProduct(Product param_product);
    void setPrice(float param_price);
    void setOriginalPrice(float param_price);

    Product getProduct();
    int getQuantity();
    float getPrice();
    float getOriginalPrice();

    // Order history
    String getName();
    String getSku();
    float getSubtotal();
    float getRowTotal();
    float getTaxAmount();
    float getDiscountAmount();
    int getQtyOrdered();
    int getQtyCanceled();
    int getQtyInvoiced();
    int getQtyRefunded();
    int getQtyShipped();
    int QtyShip();

    void setId(String strID);

    // Param Order Shipment
    void setOrderItemId(String strOrderItemId);
    String getOrderItemId();
}
