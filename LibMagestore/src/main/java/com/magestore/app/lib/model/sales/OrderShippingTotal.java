package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShippingTotal extends Model {
    float getBaseShippingAmount();
    float getBaseShippingDiscountAmount();
    float getBaseShippingDiscountTaxCompensationAmnt();
    float getBaseShippingInclTax();
    float getBaseShippingInvoiced();
    float getBaseShippingTaxAmount();
    float getShippingAmount();
    float getShippingDiscountAmount();
    float getShippingDiscountTaxCompensationAmount();
    float getShippingInclTax();
    float getShippingInvoiced();
    float getShippingTaxAmount();
}
