package com.magestore.app.pos.model.checkout;

import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.ArrayList;
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
        String value;
    };
    List<CustomOptions> options;


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

        if (options == null) options = new ArrayList<CustomOptions>();
        for (ProductOptionCustom customOption :
                cartItem.getChooseProductOptions().keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            CustomOptions quoteCustomeOption = new CustomOptions();
            quoteCustomeOption.code = Integer.parseInt(customOption.getOptionID());
            for (int i = 0; i < customOption.getOptionValueList().size(); i++) {
                stringBuilder.append(i == 0 ? "" : ",").append(customOption.getOptionValueList().get(i).getOptionTypeID());
            }
            quoteCustomeOption.value = stringBuilder.toString();
            options.add(quoteCustomeOption);
        }
    }
}
