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
    int precision = 2;
    int requiredPrecision = 2;
    char decimalSymbol = '$';
    char groupSymbol = '.';
    int groupLength = 3;
    int integerRequired = 1;

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public int getRequirePrecision() {
        return requiredPrecision;
    }

    @Override
    public char getDecimalSymbol() {
        return decimalSymbol;
    }

    @Override
    public char getGroupSymbol() {
        return groupSymbol;
    }

    @Override
    public int getGroupLength() {
        return groupLength;
    }

    @Override
    public int getIntegerRequired() {
        return integerRequired;
    }
}
