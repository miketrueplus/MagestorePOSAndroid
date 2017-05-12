package com.magestore.app.pos.model.sales;

import com.magestore.app.lib.model.sales.OrderRefundGiftCard;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderRefundGiftCard extends PosAbstractModel implements OrderRefundGiftCard {
    String order_id;
    float amount;
    float base_amount;

    @Override
    public void setOrderId(String strOrderId) {
        order_id = strOrderId;
    }

    @Override
    public void setAmount(float fAmount) {
        amount = fAmount;
    }

    @Override
    public void setBaseAmount(float fBaseAmount) {
        base_amount = fBaseAmount;
    }
}
