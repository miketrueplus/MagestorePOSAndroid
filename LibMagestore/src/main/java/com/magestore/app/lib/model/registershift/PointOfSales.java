package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.directory.Currency;

/**
 * Created by Johan on 6/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PointOfSales extends Model {
    String getPosId();
    void setPosId(String strPosId);
    String getPosName();
    void setPosName(String strPosName);
    String getLocationId();
    void setLocationId(String strLocationId);
    String getLocationName();
    void setLocationName(String strLocationName);
    String getAddress();
    void setAddress(String strAddress);
    String getStoreId();
    void setStoreId(String strStoreId);
    String getStaffId();
    String getStatus();
    boolean getCashControl();
    void setCashControl(boolean bCashControl);
    boolean getIfaceDiscount();
    void setIfaceDiscount(boolean bIfaceDiscount);
    float getDiscountPC();
    void setDiscountPC(float fDiscountPC);
    String getDiscountProductId();
    void setDiscountProductId(String strDiscountProductId);
    Currency getCurrency();
    void setCurrency(Currency mCurrency);
    ConfigPriceFormat getPriceFormat();
    void setPriceFormat(ConfigPriceFormat mPriceFormat);
    String getReceiptHeader();
    void setReceiptHeader(String strReceiptHeader);
    String getReceiptFooter();
    void setReceiptFooter(String strReceiptFooter);
}
