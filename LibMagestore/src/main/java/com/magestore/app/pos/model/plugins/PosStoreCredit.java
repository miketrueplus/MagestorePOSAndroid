package com.magestore.app.pos.model.plugins;

import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 4/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosStoreCredit extends PosAbstractModel implements StoreCredit {
    float balance;

    @Override
    public float getBalance() {
        return balance;
    }
}
