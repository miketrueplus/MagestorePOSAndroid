package com.magestore.app.lib.service.sales;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.Shipping;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface CheckoutService extends ListService<Checkout> {
    boolean insert(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    Checkout create();

    PaymentMethod createPaymentMethod();

    Shipping createShipping();
}
