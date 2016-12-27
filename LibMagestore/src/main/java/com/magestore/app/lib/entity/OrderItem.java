package com.magestore.app.lib.entity;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface OrderItem extends Entity  {
    void setQuantity(int param_quantity);
    void setProduct(Product param_product);
    void setPrice(float param_price);
    void setOriginalPrice(float param_price);

    Product getProduct();
    int getQuantity();
    float getPrice();
    float getOriginalPrice();
}
