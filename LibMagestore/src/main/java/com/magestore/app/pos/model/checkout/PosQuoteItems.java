package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;

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
    int qty;
    int qty_to_ship;
    int use_discount;
    PosQuoteItemExtension extension_data;

    class CustomOptions {
        int code;
        int value;
    }

    List<CustomOptions> options;
    HashMap<String, String> super_attribute;

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
    public int getQty() {
        return qty;
    }

    @Override
    public void setQty(int intQty) {
        qty = intQty;
    }

    @Override
    public int getQtyToShip() {
        return qty_to_ship;
    }

    @Override
    public void setQtyToShip(int intQtyToShip) {
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
    public QuoteItemExtension getExtensionData() {
        return extension_data;
    }

    @Override
    public void setExtensionData(QuoteItemExtension quoteItemExtension) {
        extension_data = (PosQuoteItemExtension) quoteItemExtension;
    }

    @Override
    public CartItem getCartItem() {
        return null;
    }

    @Override
    public void convertProductOption(CartItem cartItem) {
        if (cartItem.getChooseProductOptions() == null) return;
        if (!cartItem.getProduct().haveProductOption()) return;

        for (ProductOptionCustom customOption : cartItem.getChooseProductOptions().keySet()) {
            PosCartItem.ChooseProductOption chooseProductOptions = cartItem.getChooseProductOptions().get(customOption);
            if (chooseProductOptions.productOptionCustomValueList == null) continue;
            for (ProductOptionCustomValue optionCustomValue : chooseProductOptions.productOptionCustomValueList) {
                // với config option
                if (customOption.isConfigOption()) {
                    if (super_attribute == null) super_attribute = new HashMap<>();
                    super_attribute.put(customOption.getID(), optionCustomValue.getID());
                } else {
                    // với custom option option
                    CustomOptions quoteCustomeOption = new CustomOptions();
                    quoteCustomeOption.code = Integer.parseInt(customOption.getID());
                    quoteCustomeOption.value = Integer.parseInt(optionCustomValue.getID());
                    if (options == null) options = new ArrayList<CustomOptions>();
                    options.add(quoteCustomeOption);
                }
            }
        }
    }
}
