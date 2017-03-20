package com.magestore.app.pos.model.checkout.cart;

import com.magestore.app.lib.model.checkout.cart.CartDeleteItemParam;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 3/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosCartDeleteItemParam extends PosAbstractModel implements CartDeleteItemParam {
    String quote_id;
    String item_id;

    @Override
    public void setQuoteId(String strOrderId) {
        quote_id = strOrderId;
    }

    @Override
    public void setItemId(String strItemId) {
        item_id = strItemId;
    }
}
