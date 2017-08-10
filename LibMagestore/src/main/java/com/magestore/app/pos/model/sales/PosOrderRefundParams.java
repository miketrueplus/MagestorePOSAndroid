package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 1/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderRefundParams extends PosAbstractModel implements OrderRefundParams {
    String orderId;
    float adjustmentNegative;
    float adjustmentPositive;
    String baseCurrencyCode;
    List<OrderCommentParams> comments;
    String emailSent;
    List<OrderItemParams> items;
    float shippingAmount;
    String storeCurrencyCode;
    String increment_id;
    String invoice_id;


    @Override
    public void setOrderId(String strOrderId) {
        orderId = strOrderId;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setAdjustmentNegative(float adjustmentNegative) {
        this.adjustmentNegative = adjustmentNegative;
    }

    @Override
    public void setAdjustmentPositive(float adjustmentPositive) {
        this.adjustmentPositive = adjustmentPositive;
    }

    @Override
    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    @Override
    public void setShippingAmount(float shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    @Override
    public void setStoreCurrencyCode(String strStoreCurrencyCode) {
        this.storeCurrencyCode = strStoreCurrencyCode;
    }

    @Override
    public void setEmailSent(String strEmailSent) {
        emailSent = strEmailSent;
    }

    @Override
    public String getEmailSent() {
        return emailSent;
    }

    @Override
    public List<OrderItemParams> getItems() {
        return items;
    }

    @Override
    public void setItems(List<OrderItemParams> items) {
        this.items = items;
    }

    @Override
    public List<OrderCommentParams> getComments() {
        return comments;
    }

    @Override
    public void setComments(List<OrderCommentParams> comments) {
        this.comments = comments;
    }

    @Override
    public void setIncrementId(String srtIncrementId) {
        increment_id = srtIncrementId;;
    }

    @Override
    public void setInvoiceId(String strInvoiceId) {
        invoice_id = strInvoiceId;
    }
}
