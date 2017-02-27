package com.magestore.app.pos.model.directory;

import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosCurrency extends PosAbstractModel implements Currency {
    String code;
    String currency_name;
    String currency_symbol;
    String is_default;
    float currency_rate;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String strCode) {

    }

    @Override
    public String getCurrencyName() {
        return currency_name;
    }

    @Override
    public void setCurrenyName(String strCurrencyName) {
        currency_name = strCurrencyName;
    }

    @Override
    public String getCurrencySymbol() {
        return currency_symbol;
    }

    @Override
    public void setCurrencySymbol(String strCurrencySymbol) {
        currency_symbol = strCurrencySymbol;
    }

    @Override
    public String getIsDefault() {
        return is_default;
    }

    @Override
    public void setIsDefault(String strIsDefault) {
        is_default = strIsDefault;
    }

    @Override
    public float getCurrencyRate() {
        return currency_rate;
    }

    @Override
    public void setCurrencyRate(float fCurrencyRate) {
        currency_rate = fCurrencyRate;
    }
}
