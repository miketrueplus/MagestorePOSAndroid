package com.magestore.app.pos.api.odoo.sales;

import com.magestore.app.lib.model.catalog.ProductTaxDetailOdoo;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.resourcemodel.sales.CartDataAccess;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 9/21/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSCartDataAccessOdoo extends POSAbstractDataAccessOdoo implements CartDataAccess {
    @Override
    public boolean delete(Checkout checkout, CartItem cartItem) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return false;
    }

    @Override
    public void changeTax(Checkout checkout) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        float total_tax = 0;
        float grand_total = 0;
        List<CartItem> listCartItem = checkout.getCartItem();
        for (CartItem cartItem : listCartItem) {
            float price_item;
            if (cartItem.isCustomPrice()) {
                price_item = cartItem.getCustomPrice();
            } else {
                price_item = cartItem.getProduct().getFinalPrice();
            }
            cartItem.setRowTotal(price_item);
            float tax = 0;
            List<ProductTaxDetailOdoo> listTaxDetail = cartItem.getProduct().getTaxDetail();
            if (listTaxDetail != null && listTaxDetail.size() > 0) {
                for (ProductTaxDetailOdoo taxDetail : listTaxDetail) {
                    tax += taxDetail.getAmount();
                }
            }
            total_tax += (price_item * tax) / 100;
        }
        checkout.setTaxTotal(total_tax);
    }
}
