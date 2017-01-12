package com.magestore.app.lib.model.order;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Interface của Order payment address
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderPayment extends Model {
    List<String> getAdditionalInformation();
    float getAmountOrdered();
    float getAmountPaid();
    float getBaseAmountOrdered();
    float getBaseAmountPaid();
    float getBaseShippingAmount();
    float getBaseShippingCaptured();
    String getMethod();
    float getShippingAmount();
    float getShippingCaptured();
}
