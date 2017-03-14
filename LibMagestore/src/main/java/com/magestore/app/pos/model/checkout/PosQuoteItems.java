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

    class OptionsValue {
        String code;
        String value;
    }

    List<OptionsValue> options;
    List<OptionsValue> super_attribute;
    List<OptionsValue> bundle_option;
    List<OptionsValue> bundle_option_qty;

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
                    OptionsValue quoteCustomeOption = new OptionsValue();
                    quoteCustomeOption.code = customOption.getID();
                    quoteCustomeOption.value = optionCustomValue.getID();
                if (customOption.isConfigOption()) {
                    // với config option option
                    if (super_attribute == null) super_attribute = new ArrayList<OptionsValue>();
                    super_attribute.add(quoteCustomeOption);
                }
                else if (customOption.isCustomOption()) {
                    // với custom option option
                    if (options == null) options = new ArrayList<OptionsValue>();
                    options.add(quoteCustomeOption);
                }
                else if (customOption.isBundleOption()) {
                    // với bundle
                    if (bundle_option == null) bundle_option = new ArrayList<OptionsValue>();
                    bundle_option.add(quoteCustomeOption);

                    // thêm quantity
                    if (bundle_option_qty == null) bundle_option_qty = new ArrayList<OptionsValue>();
                    OptionsValue bundleQuantity = new OptionsValue();
                    bundleQuantity.code = optionCustomValue.getID();
                    bundleQuantity.value = "1";
                    bundle_option_qty.add(bundleQuantity);
                }
            }
        }
    }
}
