package com.magestore.app.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.gateway.CustomerGateway;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.UserGateway;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.gateway.pos.POSGatewaySession;
import com.magestore.app.lib.usecase.ConfigUseCase;
import com.magestore.app.lib.usecase.UseCaseFactory;
import com.magestore.app.lib.usecase.UserUseCase;
import com.magestore.app.lib.usecase.pos.POSUserUseCase;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class MageStoreUseCaseTest {
    @Test
    public void test_config_usecase_isCorrect() throws Exception {
        // Đăng nhập
        UserUseCase userUseCase = UseCaseFactory.generateUserUseCase(null, null);
        userUseCase.doLogin("demo-magento2.magestore.com", "demo", "demo123");

        ConfigUseCase configUseCase = UseCaseFactory.generateConfigUseCase(null, null);
        Config config = configUseCase.retrieveConfig();
        String strValue = (String) config.getValue("webpos/offline/customer_time");
        return;
    }
}
