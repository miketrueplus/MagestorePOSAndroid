package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 5/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderRefundGiftCard extends Model {
    void setOrderId(String strOrderId);
    float getAmount();
    void setAmount(float fAmount);
    float getBaseAmount();
    void setBaseAmount(float fBaseAmount);
}
