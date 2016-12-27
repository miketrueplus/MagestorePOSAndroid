package com.magestore.app.lib.usecase;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 12/20/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface UserUseCase extends UseCase {
    boolean doLogin(String domain, String username, String password) throws InstantiationException, IllegalAccessException, IOException, ParseException;
    void doLogout();
    boolean isLogin();
}
