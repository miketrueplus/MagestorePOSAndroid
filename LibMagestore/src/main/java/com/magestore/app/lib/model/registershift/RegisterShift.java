package com.magestore.app.lib.model.registershift;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface RegisterShift extends Model {
    float getTotalSales();
    String getOpenedAt();
    String getClosedAt();
    String getUpdateAt();
    String getOpenAndClose();
}
