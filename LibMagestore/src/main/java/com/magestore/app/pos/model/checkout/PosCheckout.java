package com.magestore.app.pos.model.checkout;

import com.google.gson.annotations.Expose;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCheckout extends PosAbstractModel implements Checkout {
    String customer_id;
    List<CartItem> items;
    CheckoutPayment payment;
    CheckoutShipping checkoutShipping;
    private class CheckoutConfig {
        String apply_promotion = "0";
        String note;
        boolean create_invoice = true;
        boolean create_shipment = false;
        int cart_base_discount_amount = 0;
        int cart_discount_amount = 0;
        String cart_discount_name;
        String currency_code;
    }
    CheckoutConfig config;
    String coupon_code;
    private class Extension {
        String key;
        String value;
    }
    Extension extension_data;
    ArrayList<Model> session_data;
    ArrayList<Model>Sintegration;

    @Expose(serialize = false, deserialize = false)
    float sub_total = 0;
    @Expose(serialize = false, deserialize = false)
    float tax_total = 0;
    @Expose(serialize = false, deserialize = false)
    float discount_total = 0;
    @Expose(serialize = false, deserialize = false)
    float last_total = 0;

    @Override
    public String getCustomerID() {
        return customer_id;
    }

    @Override
    public void setCustomerID(String strCustomerID) {
        this.customer_id = strCustomerID;
    }

    @Override
    public List<CartItem> getCartItem() {
        return this.items;
    }

    @Override
    public void setCartItem(List<CartItem> cartItem) {
        this.items = cartItem;
    }

    public CheckoutShipping getCheckoutShipping() {
        return checkoutShipping;
    }

    public void setCheckoutShipping(CheckoutShipping shiping) {
        this.checkoutShipping = shiping;
    }

    @Override
    public CheckoutPayment getPayment() {
        return payment;
    }

    @Override
    public void setPayment(CheckoutPayment payment) {
        this.payment = payment;
    }

    @Override
    public String getCouponCode() {
        return coupon_code;
    }

    @Override
    public void setCouponCode(String strCouponCode) {
        this.coupon_code = strCouponCode;
    }

    @Override
    public float getSubTotal() {
        return sub_total;
    }

    @Override
    public void setSubTotal(float total) {
        sub_total = total;
    }

    @Override
    public float getTaxTotal() {
        return tax_total;
    }

    @Override
    public void setTaxTotal(float total) {
        tax_total = total;
    }

    @Override
    public float getDiscountTotal() {
        return discount_total;
    }

    @Override
    public void setDiscountTotal(float total) {
        discount_total = total;
    }

    @Override
    public float getLastTotal() {
        return last_total;
    }

    @Override
    public void setLastTotal(float total) {
        last_total = total;
    }
}
