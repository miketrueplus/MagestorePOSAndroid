package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 6/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface PointOfSales extends Model {
    String getPosName();
    String getLocationId();
    String getStoreId();
    String getStaffId();
    String getStatus();
}
