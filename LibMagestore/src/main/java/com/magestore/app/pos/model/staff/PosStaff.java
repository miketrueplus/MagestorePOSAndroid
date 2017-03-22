package com.magestore.app.pos.model.staff;

import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.pos.model.PosAbstractModel;

/**
 * Created by Johan on 3/22/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosStaff extends PosAbstractModel implements Staff {
    String staff_id;
    String staff_name;

    @Override
    public String getID() {
        return staff_id;
    }

    @Override
    public void setStaffId(String strStaffId) {
        staff_id = strStaffId;
    }

    @Override
    public String getStaffName() {
        return staff_name;
    }

    @Override
    public void setStaffName(String strStaffName) {
        staff_name = strStaffName;
    }
}
