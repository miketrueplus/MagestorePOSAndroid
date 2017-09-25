package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 1/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderRefundParams extends Model {
    void setOrderId(String strOrderId);

    String getOrderId();

    float getAdjustmentNegative();
    void setAdjustmentNegative(float adjustmentNegative);

    float getAdjustmentPositive();
    void setAdjustmentPositive(float adjustmentPositive);

    String getBaseCurrencyCode();
    void setBaseCurrencyCode(String baseCurrencyCode);

    float getShippingAmount();
    void setShippingAmount(float shippingAmount);

    String getStoreCurrencyCode();
    void setStoreCurrencyCode(String strStoreCurrencyCode);

    void setEmailSent(String strEmailSent);

    String getEmailSent();

    List<OrderItemParams> getItems();

    void setItems(List<OrderItemParams> items);

    List<OrderCommentParams> getComments();

    void setComments(List<OrderCommentParams> comments);

    void setIncrementId(String srtIncrementId);

    void setInvoiceId(String strInvoiceId);

    void setQty(String strString);

    void setStock(String strStock);
}
