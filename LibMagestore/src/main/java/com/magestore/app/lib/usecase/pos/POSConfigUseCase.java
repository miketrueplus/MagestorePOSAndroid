package com.magestore.app.lib.usecase.pos;

import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.entity.DefaultConfig;
import com.magestore.app.lib.gateway.ConfigGateway;
import com.magestore.app.lib.gateway.GatewayFactory;
import com.magestore.app.lib.gateway.pos.POSGatewayFactory;
import com.magestore.app.lib.usecase.ConfigUseCase;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * Trả lại config chung của hệ thống
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSConfigUseCase extends AbstractUseCase implements ConfigUseCase {
    /**
     * Trả lại config chung từ server
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Config retrieveConfig() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo customer gateway factory
        GatewayFactory factory = GatewayFactory.getFactory(POSGatewayFactory.class);
        ConfigGateway configGateway = factory.generateConfigGateway();

        // Lấy list config đầu tiên
        ConfigUtil.mConfig = configGateway.getConfig();
        return ConfigUtil.mConfig;
    }

    /**
     * Trả lại config đã được lưu trên hệ thống
     * @return
     */
    @Override
    public Config getConfig() {
        if (ConfigUtil.mConfig == null) ConfigUtil.mConfig = new DefaultConfig();
        return ConfigUtil.mConfig;
    }
}
