package com.magestore.app.lib.model.staff;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 7/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface StaffPermisson extends Model {
    String getDisplayName();
    List<String> getPermisson();
    String getRole();
    String getPin();
}
