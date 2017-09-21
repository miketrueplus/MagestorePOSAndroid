package com.magestore.app.lib.service.checkout;

import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.ServiceException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

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

    CartItem create(Product product);

    CartItem create(Product product, float quantity, float price);

    CartItem createCustomSale();

    CartItem create(Product product, float quantity);

    CartItem create(Checkout checkout, Product product) throws InstantiationException, IllegalAccessException, ParseException, IOException;

    CartItem findItem(Checkout checkout, Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException;


    CartItem insertWithCustomSale(Checkout checkout, CartItem cartItem) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    CartItem insertWithOption(Checkout checkout, CartItem cartItem) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    CartItem findCartItem(Checkout checkout, CartItem findItem);

    boolean compareCartItem(CartItem itemOld, CartItem itemNew);

    void increase(CartItem cartItem);

    void increase(CartItem cartItem, float quantity);

    void substract(CartItem cartItem);

    void substract(CartItem cartItem, float quantity);

    CartItem insert(Checkout checkout, String productID, String productName, float quantity, float price, boolean inStock, String image) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    CartItem insert(Checkout checkout, Product product, float quantity, float price) throws InstantiationException, IllegalAccessException, ParseException, IOException;

    CartItem insert(Checkout checkout, Product product, float quantity) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    boolean delete(Checkout checkout, int position) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    CartItem delete(Checkout checkout, Product product) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    CartItem delete(Checkout checkout, Product product, float subQuantity) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    void updatePrice(CartItem cartItem);

    boolean validateStock(Checkout checkout, Product product, float quantity);

    boolean validateStock(Checkout checkout, CartItem item, float quantity);

    float calculateValidStock(Checkout checkout, CartItem item, float quantity) throws ServiceException;

    float calculateValidStock(Checkout checkout, Product product, float quantity) throws ServiceException;

    List<CartItem> reOrder(Checkout checkout, Order order) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    void changeTax(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException;
}
