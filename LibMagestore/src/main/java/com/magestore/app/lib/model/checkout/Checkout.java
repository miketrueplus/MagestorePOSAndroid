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

    List<CheckoutShipping> getCheckoutShipping();
    void setCheckoutShipping(List<CheckoutShipping> shiping);

    List<CheckoutPayment> getCheckoutPayment();
    void setCheckoutPayment(List<CheckoutPayment> payment);

    String getCouponCode();
    void setCouponCode(String strCouponCode);

    float getSubTotal();
    void setSubTotal(float total);

    float getShippingTotal();
    void setShippingTotal(float shipping);

    float getTaxTotal();
    void setTaxTotal(float total);

    float getDiscountTotal();
    void setDiscountTotal(float total);

    float getGrandTotal();
    void setGrandTotal(float total);

    Customer getCustomer();
    void setCustomer(Customer customer);

    List<CheckoutTotals> getTotals();
    void setTotals(List<CheckoutTotals> checkoutTotals);

    Quote getQuote();
    void setQuote(Quote quote);

    String getCreateShip();
    void setCreateShip(String strCreateShip);

    String getCreateInvoice();
    void setCreateInvoice(String strCreateInvoice);

    float getRealAmount();
    void setRealAmount(float fRealAmount);

    float getRemainMoney();
    void setRemainMoney(float fRemainMoney);

    float getExchangeMoney();
    void setExchangeMoney(float fExchangeMoney);

    String getCreateAt();
    void setCreateAt(String strCreateAt);
}
