package com.magestore.app.lib;

import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.user.UserService;

import org.junit.Test;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class MageStoreServiceTest {
    @Test
    public void test_config_usecase_isCorrect() throws Exception {
        // Đăng nhập
        ServiceFactory factory = ServiceFactory.getFactory(null);
        UserService userService = factory.generateUserService();
        userService.doLogin("demo-magento2.magestore.com", "demo", "demo123");

        ConfigService configService = factory.generateConfigService();
        Config config = configService.retrieveConfig();
        String strValue = (String) config.getValue("webpos/offline/customer_time");
        return;
    }
}
