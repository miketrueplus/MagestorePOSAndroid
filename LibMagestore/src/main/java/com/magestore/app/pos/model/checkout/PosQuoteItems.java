package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosQuoteItems extends PosAbstractModel implements QuoteItems {
    String item_id;
    String qty;
    String qty_to_ship;
    int use_discount = 1;
    String amount;
    String custom_price;
    String is_custom_sale;

    List<PosQuoteItemExtension> extension_data;

    List<PosCartItem.OptionsValue> options;
    List<PosCartItem.OptionsValue> super_attribute;
    List<PosCartItem.OptionsValue> bundle_option;
    List<PosCartItem.OptionsValue> bundle_option_qty;

    @Override
    public String getAmount() {
        return amount;
    }

    @Override
    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String getCustomPrice() {
        return custom_price;
    }

    @Override
    public void setCustomPrice(String custom_price) {
        this.custom_price = custom_price;
    }

    @Override
    public String getItemId() {
        return item_id;
    }

    @Override
    public void setItemId(String strItemId) {
        item_id = strItemId;
    }

    @Override
    public void setId(String strId) {
        id = strId;
    }

    @Override
    public float getQty() {
        return Float.parseFloat(qty);
    }

    @Override
    public void setQty(float intQty) {
        qty = Float.toString(intQty);
    }

    @Override
    public void setQty(String intQty) {
        qty = intQty;
    }

    @Override
    public float getQtyToShip() {
        return Float.parseFloat(qty_to_ship);
    }

    @Override
    public void setQtyToShip(float intQtyToShip) {
        qty_to_ship = Float.toString(intQtyToShip);
    }

    @Override
    public void setQtyToShip(String intQtyToShip) {
        qty_to_ship = intQtyToShip;
    }

    @Override
    public int getUserDiscount() {
        return use_discount;
    }

    @Override
    public void setUserDiscount(int intUserDiscount) {
        use_discount = intUserDiscount;
    }

    @Override
    public List<QuoteItemExtension> getExtensionData() {
        return (List<QuoteItemExtension>) (List<?>) extension_data;
    }

    @Override
    public void setExtensionData(List<QuoteItemExtension> quoteItemExtension) {
        extension_data = (List<PosQuoteItemExtension>) (List<?>) quoteItemExtension;
    }

    @Override
    public CartItem getCartItem() {
        return null;
    }

    @Override
    public void convertProductOption(CartItem cartItem) {
        bundle_option = cartItem.getBundleOption();
        options = cartItem.getOptions();
        super_attribute = cartItem.getSuperAttribute();
        bundle_option_qty = cartItem.getBundleOptionQuantity();
    }

    @Override
    public void setCustomSale(String strCustomSale) {
        is_custom_sale = strCustomSale;
    }
}
