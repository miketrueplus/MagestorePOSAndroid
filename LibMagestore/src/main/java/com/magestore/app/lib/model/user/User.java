package com.magestore.app.lib.model.user;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 12/12/2016.
 */

public interface User extends Model {
    String getUserName();
    String getPasswords();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getHandPhoneNumber();
}