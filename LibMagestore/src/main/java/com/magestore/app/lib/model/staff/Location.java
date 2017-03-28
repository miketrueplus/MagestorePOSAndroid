package com.magestore.app.lib.model.staff;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 3/28/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Location extends Model {
    void setLocationId(String strLocationId);
    String getLocationName();
    void setLocationName(String strLocationName);
    String getLocationAddress();
    void setLocationAddress(String strLocationAddress);
}
