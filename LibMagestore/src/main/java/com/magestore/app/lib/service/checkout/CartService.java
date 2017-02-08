package com.magestore.app.lib.service.checkout;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.service.ChildListService;

import java.io.IOException;
import java.text.ParseException;

/**
 * Giao diện các nghiệp vụ của đơn hàng
 * Created by Mike on 12/26/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface CartService extends ChildListService<Checkout, CartItem> {
    float calculateSubTotal(Checkout checkout);

    float calculateTaxTotal(Checkout checkout);

    float calculateDiscountTotal(Checkout checkout);

    float calculateLastTotal(Checkout checkout);

    CartItem findItem(Checkout checkout, Product product);

    boolean insert(Checkout checkout, Product product, int quantity, float price) throws InstantiationException, IllegalAccessException, ParseException, IOException;

    boolean insert(Checkout checkout, Product product, int quantity) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean delete(Checkout checkout, int position) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean delete(Checkout checkout, Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean delete(Checkout checkout, Product product, int subQuantity) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
