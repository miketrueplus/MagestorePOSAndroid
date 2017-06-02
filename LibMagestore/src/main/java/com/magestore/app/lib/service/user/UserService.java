package com.magestore.app.lib.service.user;

import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/20/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface UserService extends Service {
    boolean doLogin(String domain, String proxyUser, String proxyPass, String username, String password) throws InstantiationException, IllegalAccessException, IOException, ParseException;
    void doLogout();
    boolean isLogin();
    boolean retrievePos() throws InstantiationException, IllegalAccessException, IOException, ParseException;
    List<PointOfSales> getListPos() throws InstantiationException, IllegalAccessException, IOException, ParseException;
    boolean retrieveStore() throws InstantiationException, IllegalAccessException, IOException, ParseException;
    List<Store> getListStore() throws InstantiationException, IllegalAccessException, IOException, ParseException;
}
