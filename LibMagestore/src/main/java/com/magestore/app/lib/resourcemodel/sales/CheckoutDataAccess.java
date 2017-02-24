package com.magestore.app.lib.resourcemodel.sales;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;

import java.io.IOException;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public interface CheckoutDataAccess extends DataAccess {
    boolean insert(Checkout... models) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout saveCart(Quote quote) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout saveShipping(String quoteId, String shippingCode) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Checkout savePayment(String quoteId, String paymentCode) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    Order placeOrder(PlaceOrderParams placeOrderParams) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean addOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException;

    boolean removeOrderToListCheckout(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}
