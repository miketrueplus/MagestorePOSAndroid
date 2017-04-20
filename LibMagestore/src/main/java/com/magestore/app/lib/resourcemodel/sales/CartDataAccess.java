package com.magestore.app.lib.resourcemodel.sales;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.resourcemodel.DataAccess;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface CartDataAccess extends DataAccess {
    boolean delete(Checkout checkout, CartItem cartItem) throws ParseException, InstantiationException, IllegalAccessException, IOException;
}
