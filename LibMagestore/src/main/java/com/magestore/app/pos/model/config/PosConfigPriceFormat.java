package com.magestore.app.pos.model.config;

import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class PosConfigPriceFormat extends PosAbstractModel implements ConfigPriceFormat {
    String pattern;
    int precision;
    int requiredPrecision;
    String decimalSymbol;
    String groupSymbol;
    int groupLength;
    int integerRequired;
    String currencySymbol;

    @Override
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    @Override
    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(String strPattern) {
        pattern = strPattern;
    }

    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public void setPrecision(int intPrecision) {
        precision = intPrecision;
    }

    @Override
    public int getRequirePrecision() {
        return requiredPrecision;
    }

    @Override
    public void setRequirePrecision(int intRequirePrecision) {
        requiredPrecision = intRequirePrecision;
    }

    @Override
    public String getDecimalSymbol() {
        return decimalSymbol;
    }

    @Override
    public void setDecimalSymbol(String strDecimalSymbol) {
        decimalSymbol = strDecimalSymbol;
    }

    @Override
    public String getGroupSymbol() {
        return groupSymbol;
    }

    @Override
    public void setGroupSymbol(String strGroupSymbol) {
        groupSymbol = strGroupSymbol;
    }

    @Override
    public int getGroupLength() {
        return groupLength;
    }

    @Override
    public void setGroupLength(int intGroupLength) {
        groupLength = intGroupLength;
    }

    @Override
    public int getIntegerRequired() {
        return integerRequired;
    }

    @Override
    public void setIntegerRequied(int intIntegerRequied) {
        intIntegerRequied = intIntegerRequied;
    }
}
