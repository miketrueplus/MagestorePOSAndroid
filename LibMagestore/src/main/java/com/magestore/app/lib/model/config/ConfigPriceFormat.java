package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 1/14/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigPriceFormat extends Model {
    String getPattern();
    void setPattern(String strPattern);
    int getPrecision();
    void setPrecision(int intPrecision);
    int getRequirePrecision();
    void setRequirePrecision(int intRequirePrecision);
    String getDecimalSymbol();
    void setDecimalSymbol(String strDecimalSymbol);
    String getGroupSymbol();
    void setGroupSymbol(String strGroupSymbol);
    int getGroupLength();
    void setGroupLength(int intGroupLength);
    int getIntegerRequired();
    void setIntegerRequied(int intIntegerRequied);
}
