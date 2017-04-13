package com.magestore.app.lib.model.plugins;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 4/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface StoreCredit extends Model {
    float getBalance();
    float getAmount();
    void setAmount(float fAmount);
}
