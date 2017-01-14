package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigPriceFormat extends Model {
    String getPattern();
    int getPrecision();
    int getRequirePrecision();
    char getDecimalSymbol();
    char getGroupSymbol();
    int getGroupLength();
    int getIntegerRequired();
}
