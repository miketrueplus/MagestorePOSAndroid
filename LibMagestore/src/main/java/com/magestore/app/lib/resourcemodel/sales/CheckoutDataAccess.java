package com.magestore.app.lib.resourcemodel.sales;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.ListDataAccess;

import java.io.IOException;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */
public interface CheckoutDataAccess extends DataAccess {
    boolean insert(Checkout... models) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}