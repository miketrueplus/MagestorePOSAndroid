package com.magestore.app.pos.model.staff;

import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.pos.model.PosAbstractModel;
import com.magestore.app.pos.parse.gson2pos.Gson2PosExclude;

/**
 * Created by Johan on 3/22/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosStaff extends PosAbstractModel implements Staff {
    String username;
    String old_password;
    String password;
    @Gson2PosExclude
    String staff_id;
    @Gson2PosExclude
    String confirm_password;
    @Gson2PosExclude
    boolean respone_type;
    @Gson2PosExclude
    String error_message;

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
        return username;
    }

    @Override
    public void setStaffName(String strStaffName) {
        username = strStaffName;
    }

    @Override
    public String getCurrentPassword() {
        return old_password;
    }

    @Override
    public void setCurrentPassword(String strCurrentPassword) {
        old_password = strCurrentPassword;
    }

    @Override
    public String getNewPassword() {
        return password;
    }

    @Override
    public void setNewPassword(String strNewPassword) {
        password = strNewPassword;
    }

    @Override
    public String getConfirmPassword() {
        return confirm_password;
    }

    @Override
    public void setConfirmPassword(String strConfirmPassword) {
        confirm_password = strConfirmPassword;
    }

    @Override
    public boolean getResponeType() {
        return respone_type;
    }

    @Override
    public void setResponeType(boolean bResponeType) {
        respone_type = bResponeType;
    }

    @Override
    public String getErrorMessage() {
        return error_message;
    }

    @Override
    public void setErrorMessage(String strErrorMessage) {
        error_message = strErrorMessage;
    }
}
