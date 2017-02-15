package com.magestore.app.lib.model.checkout;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 2/14/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface QuoteItems extends Model {
    String getItemId();
    void setItemId(String strItemId);
    void setId(String strId);
    int getQty();
    void setQty(int intQty);
    int getQtyToShip();
    void setQtyToShip(int intQtyToShip);
    int getUserDiscount();
    void setUserDiscount(int intUserDiscount);
    QuoteItemExtension getExtensionData();
    void setExtensionData(QuoteItemExtension quoteItemExtension);
}
