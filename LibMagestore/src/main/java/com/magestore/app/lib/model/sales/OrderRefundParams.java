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

    void setAdjustmentNegative(float adjustmentNegative);

    void setAdjustmentPositive(float adjustmentPositive);

    void setBaseCurrencyCode(String baseCurrencyCode);

    void setShippingAmount(float shippingAmount);

    void setStoreCurrencyCode(String strStoreCurrencyCode);

    void setEmailSent(String strEmailSent);

    String getEmailSent();

    List<OrderItemParams> getItems();

    void setItems(List<OrderItemParams> items);

    List<OrderCommentParams> getComments();

    void setComments(List<OrderCommentParams> comments);

    void setIncrementId(String srtIncrementId);

    void setInvoiceId(String strInvoiceId);
}
