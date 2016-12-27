package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.OrderItem;
import com.magestore.app.lib.entity.Product;

/**
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class PosOrderItem extends AbstractEntity implements OrderItem {
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
}
