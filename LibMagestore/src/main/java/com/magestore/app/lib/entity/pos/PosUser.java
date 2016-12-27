package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.Entity;
import com.magestore.app.lib.entity.User;

/**
 * Quản lý các thông tin user
 * Created by Mike on 12/15/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosUser extends AbstractEntity implements User {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String handphone;

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPasswords() { return password; }

    @Override
    public String getFirstName() {
        return firstname;
    }

    @Override
    public String getLastName() {
        return lastname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getHandPhoneNumber() {
        return handphone;
    }

    @Override
    public String getID() {
        return username;
    }

    public void setUserName(String name) {
        username = name;
    }
    public void setPasswords(String ppass) { password = ppass; }
}