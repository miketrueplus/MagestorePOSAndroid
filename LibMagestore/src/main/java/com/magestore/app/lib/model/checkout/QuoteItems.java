package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;

import java.util.List;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteItems extends Model {
    String getAmount();

    void setAmount(String amount);

    String getCustomPrice();

    void setCustomPrice(String custom_price);

    String getItemId();
    void setItemId(String strItemId);
    void setId(String strId);

    float getQty();
    void setQty(float intQty);
    void setQty(String intQty);

    float getQtyToShip();
    void setQtyToShip(float intQtyToShip);
    void setQtyToShip(String intQtyToShip);

    int getUserDiscount();
    void setUserDiscount(int intUserDiscount);
    List<QuoteItemExtension> getExtensionData();
    void setExtensionData(List<QuoteItemExtension> quoteItemExtension);

    CartItem getCartItem();

    void convertProductOption(CartItem cartItem);
    void setCustomSale(String strCustomSale);
    String getCustomSale();

    List<PosCartItem.OptionsValue> getOptions();
    List<PosCartItem.OptionsValue> getSuperAttribute();
    List<PosCartItem.OptionsValue> getBundleOption();
    List<PosCartItem.OptionsValue> getBundleOptionQty();
}
