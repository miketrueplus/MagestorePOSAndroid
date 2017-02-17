package com.magestore.app.pos.model.checkout;

import com.google.gson.annotations.Expose;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutTotals;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCheckout extends PosAbstractModel implements Checkout {
    String customer_id;
    List<PosCartItem> items;
    List<PosCheckoutPayment> payment;
    List<PosCheckoutShipping> shipping;
    Customer customer;
    List<PosCheckoutToTals> totals;
    PosQuote quote_init;

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
    ArrayList<Model> Sintegration;

    @Gson2PosExclude
    String create_ship;
    @Gson2PosExclude
    String create_invoice;

    @Expose(serialize = false, deserialize = false)
    float sub_total = 0;
    @Expose(serialize = false, deserialize = false)
    float shipping_total = 0;
    @Expose(serialize = false, deserialize = false)
    float tax_total = 0;
    @Expose(serialize = false, deserialize = false)
    float discount_total = 0;
    @Expose(serialize = false, deserialize = false)
    float grand_total = 0;

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
        return (List<CartItem>) (List<?>) items;
    }

    @Override
    public void setCartItem(List<CartItem> cartItem) {
        this.items = (List<PosCartItem>) (List<?>) cartItem;
    }

    public List<CheckoutShipping> getCheckoutShipping() {
        return (List<CheckoutShipping>) (List<?>) shipping;
    }

    public void setCheckoutShipping(List<CheckoutShipping> shiping) {
        this.shipping = (List<PosCheckoutShipping>) (List<?>) shiping;
    }

    @Override
    public List<CheckoutPayment> getCheckoutPayment() {
        return (List<CheckoutPayment>) (List<?>) payment;
    }

    @Override
    public void setCheckoutPayment(List<CheckoutPayment> payment) {
        this.payment = (List<PosCheckoutPayment>) (List<?>) payment;
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
    public float getShippingTotal() {
        return shipping_total;
    }

    @Override
    public void setShippingTotal(float shipping) {
        shipping_total = shipping;
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
    public float getGrandTotal() {
        return grand_total;
    }

    @Override
    public void setGrandTotal(float total) {
        grand_total = total;
    }

    @Override
    public Customer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public List<CheckoutTotals> getTotals() {
        return (List<CheckoutTotals>) (List<?>) totals;
    }

    @Override
    public void setTotals(List<CheckoutTotals> checkoutTotals) {
        totals = (List<PosCheckoutToTals>) (List<?>) checkoutTotals;
    }

    @Override
    public Quote getQuote() {
        return quote_init;
    }

    @Override
    public void setQuote(Quote quote) {
        quote_init = (PosQuote) quote;
    }

    @Override
    public String getCreateShip() {
        return create_ship;
    }

    @Override
    public void setCreateShip(String strCreateShip) {
        create_ship = strCreateShip;
    }

    @Override
    public String getCreateInvoice() {
        return create_invoice;
    }

    @Override
    public void setCreateInvoice(String strCreateInvoice) {
        create_invoice = strCreateInvoice;
    }
}
