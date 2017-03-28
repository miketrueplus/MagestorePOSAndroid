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
    String getCurrentPassword();
    void setCurrentPassword(String strCurrentPassword);
    String getNewPassword();
    void setNewPassword(String strNewPassword);
    String getConfirmPassword();
    void setConfirmPassword(String strConfirmPassword);
    boolean getResponeType();
    void setResponeType(boolean bResponeType);
    String getErrorMessage();
    void setErrorMessage(String strErrorMessage);
    Location getStaffLocation();
    void setStaffLocation(Location staffLocation);
}
