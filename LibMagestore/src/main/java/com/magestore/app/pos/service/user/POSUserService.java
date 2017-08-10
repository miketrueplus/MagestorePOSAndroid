package com.magestore.app.pos.service.user;

import com.magestore.app.lib.BuildConfig;
import com.magestore.app.lib.model.registershift.PointOfSales;
import com.magestore.app.lib.model.store.Store;
import com.magestore.app.lib.model.user.User;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.user.UserDataAccess;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;
import com.magestore.app.pos.model.registershift.PosPointOfSales;
import com.magestore.app.pos.model.user.PosUser;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Thực hiện các use case liên quan đến user account như đăng nhập
 * Created by Mike on 12/12/2016.
 */

public class POSUserService extends AbstractService implements UserService {
    static final String strfalse = "false\n";
    public static POSDataAccessSession session;
    public static POSDataAccessSessionM1 sessionM1;

    /**
     * Dựng base url từ domain do user nhập
     *
     * @param strDomain
     * @return
     */
    public String buildPOSBaseURL(String strDomain) {
        StringBuilder stringBuilder = new StringBuilder();
        String strFinalDomain = strDomain;
        if (strFinalDomain == null || strFinalDomain.trim().equals(""))
            strFinalDomain = BuildConfig.DEFAULT_REST_BASE_URL;

        int lastIndexOfApp = strFinalDomain.lastIndexOf("/");

        if (!strFinalDomain.startsWith("http://") && !strFinalDomain.startsWith("https://")) {
            if (BuildConfig.DEFAULT_REST_BASE_URL.startsWith("https://"))
                stringBuilder.append("https://");
            else
                stringBuilder.append("http://");
            stringBuilder.append(strFinalDomain);
            if (lastIndexOfApp < 0)
                stringBuilder.append("/").append(BuildConfig.DEFAULT_REST_BASE_PAGE);
            if (lastIndexOfApp == strFinalDomain.length() - 1)
                stringBuilder.append(BuildConfig.DEFAULT_REST_BASE_PAGE);
        } else {
            stringBuilder.append(strFinalDomain);
            if (lastIndexOfApp == strFinalDomain.indexOf("://") + 2)
                stringBuilder.append("/").append(BuildConfig.DEFAULT_REST_BASE_PAGE);
            if (lastIndexOfApp == strFinalDomain.length() - 1)
                stringBuilder.append(BuildConfig.DEFAULT_REST_BASE_PAGE);
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean checkPlatform(String domain, String username, String password) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        String strBaseURL = buildPOSBaseURL(domain);
        String str = userGateway.checkPlatform(strBaseURL, username, password);
        if (!StringUtil.isNullOrEmpty(str)) {
            if (str.equals("Magento")) {
                ConfigUtil.setPlatForm(ConfigUtil.PLATFORM_MAGENTO_1);
                return true;
            }
            if (str.equals("Magento2")) {
                ConfigUtil.setPlatForm(ConfigUtil.PLATFORM_MAGENTO_2);
                return true;
            }
        }
        return false;
    }

    /**
     * Thực hiện đăng nhập, check đúng sai
     *
     * @param domain
     * @param username
     * @param password
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @Override
    public boolean doLogin(String domain, String proxyUser, String proxyPass, String username, String password) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Gọi user gateway
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        String strBaseURL = buildPOSBaseURL(domain);

        // Thực hiện đăng nhập
        User user = new PosUser();
        user.setUserName(username);
        user.setPasswords(password);
        String str = userGateway.login(strBaseURL, proxyUser, proxyPass, user);
        boolean success = (str != null && (!strfalse.equals(str)));

        // nếu không đúng format session key thì thông báo lỗi
        if (success && str.length() > 100) {
            throw new IOException(str);
        }
        // Lưu lại session ID
        if (success) {
            if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2)) {
                session.REST_SESSION_ID = str.trim().replace("\"", "");
                session.REST_BASE_URL = strBaseURL;
                session.REST_USER_NAME = proxyUser;
                session.REST_PASSWORD = proxyPass;
            } else {
                sessionM1.REST_SESSION_ID = str.trim().replace("\"", "");
                sessionM1.REST_BASE_URL = strBaseURL;
                sessionM1.REST_USER_NAME = proxyUser;
                sessionM1.REST_PASSWORD = proxyPass;
            }

            // Lấy config hệ thống và lưu lại
            ServiceFactory serviceFactory = ServiceFactory.getFactory(getContext());
            ConfigService configService = serviceFactory.generateConfigService();
            configService.retrieveConfig();
        }

        // Nếu username về đúng, login thành công
        return success;
    }

    /**
     * Logout khỏi user
     */
    public void doLogout() {
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2)) {
            session = null;
        } else {
            sessionM1 = null;
        }
    }

    /**
     * Kiểm tra xem user đã login chưa
     *
     * @return
     */
    public boolean isLogin() {
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2)) {
            return session != null;
        } else {
            return sessionM1 != null;
        }
    }

    @Override
    public boolean retrievePos() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        List<PointOfSales> listPos = userGateway.retrievePos();
        if (listPos != null && listPos.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<PointOfSales> getListPos() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        List<PointOfSales> listPos = userGateway.retrievePos();
        return listPos;
    }

    @Override
    public boolean retrieveStore() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Gọi user gateway
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        List<Store> store = userGateway.retrieveStore();

        if (store != null && store.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Store> getListStore() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        return userGateway.getListStore();
    }

    @Override
    public void resetListPos() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        userGateway.resetListPos();
    }

    @Override
    public boolean requestAssignPos(String pos_id) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        UserDataAccess userGateway = factory.generateUserDataAccess();
        return userGateway.requestAssignPos(pos_id);
    }

    @Override
    public PointOfSales createPointOfSales() {
        return new PosPointOfSales();
    }
}