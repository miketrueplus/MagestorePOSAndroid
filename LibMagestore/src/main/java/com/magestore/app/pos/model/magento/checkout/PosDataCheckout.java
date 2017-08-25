package com.magestore.app.pos.model.magento.checkout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.DataCheckout;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 8/8/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosDataCheckout extends PosAbstractModel implements DataCheckout {
    String status;
    PosCheckout data;

    @Override
    public Checkout getCheckout() {
        return data;
    }
}
