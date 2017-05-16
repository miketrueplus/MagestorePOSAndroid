package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.CartItem;

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
    int getQty();
    void setQty(int intQty);
    int getQtyToShip();
    void setQtyToShip(int intQtyToShip);
    int getUserDiscount();
    void setUserDiscount(int intUserDiscount);
    List<QuoteItemExtension> getExtensionData();
    void setExtensionData(List<QuoteItemExtension> quoteItemExtension);

    CartItem getCartItem();

    void convertProductOption(CartItem cartItem);
}
