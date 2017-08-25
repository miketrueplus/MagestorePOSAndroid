package com.magestore.app.pos.model.magento.plugins;

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
    String currency_id;
    @Gson2PosExclude
    String customer_id;
    @Gson2PosExclude
    String store_id;
    @Gson2PosExclude
    String till_id;

    @Gson2PosExclude
    int balance;
    @Gson2PosExclude
    int max_points;

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public String getQuoteId() {
        return quote_id;
    }

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public void setAmount(int intAmount) {
        used_point = intAmount;
    }

    @Override
    public int getMaxPoints() {
        return max_points;
    }

    @Override
    public void setMaxPoins(int iMaxpoints) {
        max_points = iMaxpoints;
    }

    @Override
    public int getAmount() {
        return used_point;
    }

    @Override
    public String getCurrencyId() {
        return currency_id;
    }

    @Override
    public void setCurrencyId(String strCurrencyId) {
        currency_id = strCurrencyId;
    }

    @Override
    public String getCustomerId() {
        return customer_id;
    }

    @Override
    public void setCustomerId(String strCustomerId) {
        customer_id = strCustomerId;
    }

    @Override
    public String getStoreId() {
        return store_id;
    }

    @Override
    public void setStoreId(String strStoreId) {
        store_id = strStoreId;
    }

    @Override
    public String getTillId() {
        return till_id;
    }

    @Override
    public void setTillId(String strTillId) {
        till_id = strTillId;
    }
}
