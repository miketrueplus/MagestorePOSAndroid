package com.magestore.app.lib.model.order;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderItems extends Model {
    float getAmountRefunded();
    float getBasePrice();
    float getBaseDiscountInvoiced();
    float getBaseAmountRefunded();
    float getBaseRowInvoiced();
    float getBaseRowTotal();
    float getBaseTaxInvoiced();
    float getDiscountInvoiced();
    float getFreeShipping();
    String getName();
    int getNoDiscount();
    String getOrderId();
    float getOriginalPrice();
    float getPrice();
    String getProductId();
    String getproductType();
    int getQtyCanceled();
    int getQtyInvoiced();
    int getQtyOrdered();
    int getQtyRefunded();
    int getQtyShipped();
    int getRowInvoiced();
    int getRowTotal();
    String getSku();
    String getStoreId();
    float getTaxInvoiced();
    String getUpdatedAt();
    float getWeight();
}
