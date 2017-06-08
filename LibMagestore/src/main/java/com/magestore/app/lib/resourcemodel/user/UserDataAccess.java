package com.magestore.app.lib.resourcemodel.user;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface UserDataAccess extends DataAccess {
    /**
     * Thực hiện login với username và password
     * @return Trả lại user id đăng nhập
     * @throws DataAccessException
     */
    String login(String domain, String proxyUser, String proxyPassword, User user) throws ParseException, ConnectionException, DataAccessException, IOException;

    List<Store> retrieveStore() throws ParseException, ConnectionException, DataAccessException, IOException;

    List<Store> getListStore() throws ParseException, ConnectionException, DataAccessException, IOException;

    List<PointOfSales> retrievePos() throws ParseException, ConnectionException, DataAccessException, IOException;

    void resetListPos() throws ParseException, ConnectionException, DataAccessException, IOException;
}