package com.magestore.app.lib.service.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.PlaceOrderIntegrationExtension;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.lib.model.checkout.PlaceOrderExtensionParam;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationOrderData;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationParam;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.model.checkout.payment.CreditCard;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.service.ListService;

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

    Checkout saveShipping(Checkout checkout, String quoteId, String shippingCode) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout savePayment(String quoteId, String paymentCode) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Model placeOrder(String quoteId, Checkout checkout, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout);

    boolean addOrderToListCheckout(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean removeOrderToListCheckout(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    String sendEmail(String email , String increment_id) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean approvedAuthorizenet(Authorizenet authorizenet, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean invoicesPaymentAuthozire(String orderID) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean cancelPaymentAuthozire(String orderID) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    String approvedPaymentPayPal(String payment_id) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    String approvedAuthorizeIntergration(String quote_id, String token, float amount) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    String approvedPaymentStripe(String token, float amount) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    String getAccessTokenPaypalHere() throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout updateTotal(Checkout checkout);

    Checkout create();

    CheckoutPayment createPaymentMethod();

    CheckoutShipping createShipping();

    Quote createQuote();

    QuoteItems createQuoteItems();

    QuoteCustomer createQuoteCustomer();

    QuoteCustomerAddress createCustomerAddress();

    List<QuoteItemExtension> createQuoteItemExtension();

    PlaceOrderParams createPlaceOrderParams();

    PaymentMethodDataParam createPaymentMethodParam();

    PlaceOrderExtensionParam createExtensionParam();

    SaveQuoteParam createSaveQuoteParam();

    QuoteAddCouponParam createQuoteAddCouponParam();

    PlaceOrderIntegrationParam createPlaceOrderIntegrationParam();

    PlaceOrderIntegrationOrderData createPlaceOrderIntegrationOrderData();

    PlaceOrderIntegrationExtension createPlaceOrderIntegrationExtension();

    CreditCard createCreditCard();

    StoreCredit createStoreCredit();

    List<CustomerAddress> checkListAddress(Customer customer, Customer guest_customer);

    boolean checkIsVirtual(List<CartItem> cartItems);

    boolean checkCustomerID(Customer customer, Customer guest_customer);
}
