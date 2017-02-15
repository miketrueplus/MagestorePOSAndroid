package com.magestore.app.pos.service.checkout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.checkout.PosCheckoutShipping;
import com.magestore.app.pos.model.checkout.PosQuote;
import com.magestore.app.pos.model.checkout.PosQuoteCustomer;
import com.magestore.app.pos.model.checkout.PosQuoteCustomerAddress;
import com.magestore.app.pos.model.checkout.PosQuoteItemExtension;
import com.magestore.app.pos.model.checkout.PosQuoteItems;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCheckoutService extends AbstractService implements CheckoutService {
    @Override
    public boolean insert(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.insert(checkouts);
    }

    @Override
    public Checkout savePayment(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        QuoteCustomer quoteCustomer = createQuoteCustomer();
        Quote quote = createQuote();
        quote.setQuoteId("");
        quote.setCustomerId(checkout.getCustomerID());
        // TODO: Giả data với (store_id = 1 và current_id = USD, till_id = 1) sau fix lại theo config
        quote.setCurrencyId("USD");
        quote.setStoreId("1");
        quote.setTillId("1");
        quote.setItems(addItemToQuote(checkout));

        quoteCustomer.setCustomerId(checkout.getCustomerID());
        addCustomerAddressToQuote(checkout, quoteCustomer);
        quote.setCustomer(quoteCustomer);
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        return checkoutDataAccess.saveCart(quote);
    }

    @Override
    public boolean delete(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 1;
    }

    @Override
    public Checkout create() {
        Checkout checkout = new PosCheckout();
        List<CartItem> cartItemList = new ArrayList<CartItem>();
        checkout.setCartItem(cartItemList);
        return checkout;
    }

    @Override
    public Checkout retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Checkout> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        List<Checkout> checkoutList = new ArrayList<Checkout>();
        checkoutList.add(create());
        return checkoutList;
    }

    @Override
    public List<Checkout> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return retrieve(1, 1);
    }

    @Override
    public List<Checkout> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return retrieve(1, 1);
    }

    @Override
    public boolean update(Checkout oldModel, Checkout newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public CheckoutPayment createPaymentMethod() {
        return new PosCheckoutPayment();
    }

    @Override
    public CheckoutShipping createShipping() {
        return new PosCheckoutShipping();
    }

    @Override
    public Quote createQuote() {
        return new PosQuote();
    }

    @Override
    public QuoteItems createQuoteItems() {
        return new PosQuoteItems();
    }

    @Override
    public QuoteCustomer createQuoteCustomer() {
        return new PosQuoteCustomer();
    }

    @Override
    public QuoteCustomerAddress createCustomerAddress() {
        return new PosQuoteCustomerAddress();
    }

    @Override
    public QuoteItemExtension createQuoteItemExtension() {
        return new PosQuoteItemExtension();
    }

    private List<QuoteItems> addItemToQuote(Checkout checkout) {
        List<QuoteItems> listQuoteItem = new ArrayList<QuoteItems>();
        for (CartItem item : checkout.getCartItem()) {
            QuoteItems quoteItems = createQuoteItems();
            quoteItems.setQty(item.getQuantity());
            quoteItems.setId(item.getID());
            quoteItems.setItemId(item.getItemId());
            QuoteItemExtension quoteItemExtension = createQuoteItemExtension();
            quoteItems.setExtensionData(quoteItemExtension);
            listQuoteItem.add(quoteItems);
        }
        return listQuoteItem;
    }

    private void addCustomerAddressToQuote(Checkout checkout, QuoteCustomer quoteCustomer) {
        List<CustomerAddress> listAddress = checkout.getCustomer().getAddress();
        if (listAddress != null && listAddress.size() > 0) {
            if (listAddress.size() > 2) {
                CustomerAddress shippingAddress = listAddress.get(0);
                QuoteCustomerAddress customerShippingAddress = createCustomerAddress();
                customerShippingAddress.setCountryId(shippingAddress.getCountry());
                customerShippingAddress.setRegionId(shippingAddress.getRegion().getRegionID());
                customerShippingAddress.setPostcode(shippingAddress.getPostCode());
                customerShippingAddress.setStreet(shippingAddress.getStreet());
                customerShippingAddress.setTelephone(shippingAddress.getTelephone());
                customerShippingAddress.setCity(shippingAddress.getCity());
                customerShippingAddress.setFirstname(shippingAddress.getFirstName());
                customerShippingAddress.setLastname(shippingAddress.getLastName());
                customerShippingAddress.setEmail(checkout.getCustomer().getEmail());
                quoteCustomer.setShippingAddress(customerShippingAddress);
                CustomerAddress billingAddress = listAddress.get(1);
                QuoteCustomerAddress customerBillingAddress = createCustomerAddress();
                customerBillingAddress.setCountryId(billingAddress.getCountry());
                customerBillingAddress.setRegionId(billingAddress.getRegion().getRegionID());
                customerBillingAddress.setPostcode(billingAddress.getPostCode());
                customerBillingAddress.setStreet(billingAddress.getStreet());
                customerBillingAddress.setTelephone(billingAddress.getTelephone());
                customerBillingAddress.setCity(billingAddress.getCity());
                customerBillingAddress.setFirstname(billingAddress.getFirstName());
                customerBillingAddress.setLastname(billingAddress.getLastName());
                customerBillingAddress.setEmail(checkout.getCustomer().getEmail());
                quoteCustomer.setBillingAddress(customerBillingAddress);
            } else {
                CustomerAddress address = listAddress.get(0);
                QuoteCustomerAddress customerAddress = createCustomerAddress();
                customerAddress.setCountryId(address.getCountry());
                customerAddress.setRegionId(address.getRegion().getRegionID());
                customerAddress.setPostcode(address.getPostCode());
                customerAddress.setStreet(address.getStreet());
                customerAddress.setTelephone(address.getTelephone());
                customerAddress.setCity(address.getCity());
                customerAddress.setFirstname(address.getFirstName());
                customerAddress.setLastname(address.getLastName());
                customerAddress.setEmail(checkout.getCustomer().getEmail());
                quoteCustomer.setShippingAddress(customerAddress);
                quoteCustomer.setBillingAddress(customerAddress);
            }
        }
    }
}