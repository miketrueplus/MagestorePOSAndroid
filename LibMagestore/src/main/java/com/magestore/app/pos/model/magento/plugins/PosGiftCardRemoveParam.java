package com.magestore.app.pos.model.magento.plugins;

import com.magestore.app.lib.model.plugins.GiftCardRemoveParam;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Johan on 4/12/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosGiftCardRemoveParam extends PosAbstractModel implements GiftCardRemoveParam {
    String quote_id;
    String code;
    @Gson2PosExclude
    String currency_id;
    @Gson2PosExclude
    String customer_id;
    @Gson2PosExclude
    String store_id;
    @Gson2PosExclude
    String till_id;

    @Override
    public String getQuoteId() {
        return quote_id;
    }

    @Override
    public void setQuoteId(String strQuoteId) {
        quote_id = strQuoteId;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String strCode) {
        code = strCode;
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
