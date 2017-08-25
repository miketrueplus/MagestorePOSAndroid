package com.magestore.app.pos.model.magento.sales;

import com.magestore.app.lib.model.sales.OrderShippingTotal;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Quản lý order total
 * Created by Johan on 1/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderShippingTotal extends PosAbstractModel implements OrderShippingTotal {
    float base_shipping_amount;
    float base_shipping_discount_amount;
    float base_shipping_discount_tax_compensation_amnt;
    float base_shipping_incl_tax;
    float base_shipping_invoiced;
    float base_shipping_tax_amount;
    float shipping_amount;
    float shipping_discount_amount;
    float shipping_discount_tax_compensation_amount;
    float shipping_incl_tax;
    float shipping_invoiced;
    float shipping_tax_amount;

    @Override
    public float getBaseShippingAmount() {
        return base_shipping_amount;
    }

    @Override
    public float getBaseShippingDiscountAmount() {
        return base_shipping_discount_amount;
    }

    @Override
    public float getBaseShippingDiscountTaxCompensationAmnt() {
        return base_shipping_discount_tax_compensation_amnt;
    }

    @Override
    public float getBaseShippingInclTax() {
        return base_shipping_incl_tax;
    }

    @Override
    public float getBaseShippingInvoiced() {
        return base_shipping_invoiced;
    }

    @Override
    public float getBaseShippingTaxAmount() {
        return base_shipping_tax_amount;
    }

    @Override
    public float getShippingAmount() {
        return shipping_amount;
    }

    @Override
    public float getShippingDiscountAmount() {
        return shipping_discount_amount;
    }

    @Override
    public float getShippingDiscountTaxCompensationAmount() {
        return shipping_discount_tax_compensation_amount;
    }

    @Override
    public float getShippingInclTax() {
        return shipping_incl_tax;
    }

    @Override
    public float getShippingInvoiced() {
        return shipping_invoiced;
    }

    @Override
    public float getShippingTaxAmount() {
        return shipping_tax_amount;
    }
}
