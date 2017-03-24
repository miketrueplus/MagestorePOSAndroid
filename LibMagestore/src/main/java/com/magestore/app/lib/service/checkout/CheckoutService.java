package com.magestore.app.lib.service.checkout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.pos.model.checkout.PosPlaceOrderParams;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CheckoutService extends ListService<Checkout> {
    boolean insert(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout saveCart(Checkout checkout, String quoteId) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout saveQuote(Checkout checkout, SaveQuoteParam quoteParam) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout saveShipping(String quoteId, String shippingCode) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout savePayment(String quoteId, String paymentCode) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Order placeOrder(String quoteId, Checkout checkout, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout);

    boolean addOrderToListCheckout(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean removeOrderToListCheckout(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout updateTotal(Checkout checkout);

    Checkout create();

    CheckoutPayment createPaymentMethod();

    CheckoutShipping createShipping();

    Quote createQuote();

    QuoteItems createQuoteItems();

    QuoteCustomer createQuoteCustomer();

    QuoteCustomerAddress createCustomerAddress();

    QuoteItemExtension createQuoteItemExtension();

    PlaceOrderParams createPlaceOrderParams();

    PaymentMethodDataParam createPaymentMethodParam();

    SaveQuoteParam createSaveQuoteParam();

    QuoteAddCouponParam createQuoteAddCouponParam();
}
