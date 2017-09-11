package com.magestore.app.lib.resourcemodel.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;

import java.io.IOException;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public interface CheckoutDataAccess extends DataAccess {
    boolean insert(Checkout... models) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout saveCart(Checkout checkout, Quote quote) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout saveQuote(SaveQuoteParam quoteParam) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout saveShipping(Checkout checkout, String quoteId, String shippingCode) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout savePayment(String quoteId, String paymentCode) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Model placeOrder(Checkout checkout, PlaceOrderParams placeOrderParams, List<CheckoutPayment> listCheckoutPayment) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean addOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean removeOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    String sendEmail(String email, String increment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean approvedAuthorizenet(String url, String params) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean invoicesPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean cancelPaymentAuthozire(String orderID) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    String approvedPaymentPayPal(String payment_id) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    String approvedAuthorizeIntergration(String quote_id, String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    String approvedStripe(String token, float amount) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    String getAccessTokenPaypalHere() throws ParseException, InstantiationException, IllegalAccessException, IOException;
}
