package com.magestore.app.pos.model.setting;

import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.ChangeCurrency;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.directory.PosCurrency;

/**
 * Created by Johan on 4/17/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosChangeCurrency extends PosAbstractModel implements ChangeCurrency {
    PosConfigPriceFormat priceFormat;
    PosCurrency currency;

    @Override
    public ConfigPriceFormat getPriceFormat() {
        return priceFormat;
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public void setCurrency(Currency currency) {
        this.currency = (PosCurrency) currency;
    }
}
