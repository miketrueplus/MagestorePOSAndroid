package com.magestore.app.pos.model.plugins;

import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Johan on 4/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosRewardPoint extends PosAbstractModel implements RewardPoint {
    String quote_id;
    int used_point;
    String rule_id = "rate";

    @Gson2PosExclude
    int balance;

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public void setAmount(int intAmount) {
        used_point = intAmount;
    }
}
