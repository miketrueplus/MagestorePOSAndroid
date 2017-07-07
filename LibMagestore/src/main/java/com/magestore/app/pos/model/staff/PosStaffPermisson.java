package com.magestore.app.pos.model.staff;

import com.magestore.app.lib.model.staff.StaffPermisson;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Created by Johan on 7/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosStaffPermisson extends PosAbstractModel implements StaffPermisson {
    String staff_id;
    String display_name;
    List<String> permission;
    String role;
    String pin;

    @Override
    public String getID() {
        return staff_id;
    }

    @Override
    public String getDisplayName() {
        return display_name;
    }

    @Override
    public List<String> getPermisson() {
        return permission;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getPin() {
        return pin;
    }
}
