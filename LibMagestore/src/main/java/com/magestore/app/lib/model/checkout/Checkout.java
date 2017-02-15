package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;

import java.util.List;

/**
 * Created by Mike on 1/6/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface Checkout extends Model {
    String getCustomerID();
    void setCustomerID(String strCustomerID);

    List<CartItem> getCartItem();
    void setCartItem(List<CartItem> items);

    CheckoutShipping getCheckoutShipping();
    void setCheckoutShipping(CheckoutShipping shiping);

    CheckoutPayment getPayment();
    void setPayment(CheckoutPayment payment);

    String getCouponCode();
    void setCouponCode(String strCouponCode);

    float getSubTotal();
    void setSubTotal(float total);

    float getTaxTotal();
    void setTaxTotal(float total);

    float getDiscountTotal();
    void setDiscountTotal(float total);

    float getLastTotal();
    void setLastTotal(float total);

    Customer getCustomer();
    void setCustomer(Customer customer);
}
