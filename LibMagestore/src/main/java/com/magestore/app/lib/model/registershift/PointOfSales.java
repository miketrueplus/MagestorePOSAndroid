package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 6/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PointOfSales extends Model {
    void setPosId(String strPosId);
    String getPosName();
    void setPosName(String strPosName);
    String getLocationId();
    void setLocationId(String strLocationId);
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
}
