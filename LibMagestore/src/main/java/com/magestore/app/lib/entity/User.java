package com.magestore.app.lib.entity;

/**
 * Created by Mike on 12/12/2016.
 */

public interface User extends Entity {
    String getUserName();
    String getPasswords();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getHandPhoneNumber();
}