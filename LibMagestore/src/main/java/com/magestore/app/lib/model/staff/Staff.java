package com.magestore.app.lib.model.staff;

import com.magestore.app.lib.model.Model;

/**
 * Created by Johan on 3/22/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface Staff extends Model {
    void setStaffId(String strStaffId);
    String getStaffName();
    void setStaffName(String strStaffName);
}
