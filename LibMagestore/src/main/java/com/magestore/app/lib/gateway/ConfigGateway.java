package com.magestore.app.lib.gateway;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.entity.Config;
import com.magestore.app.lib.entity.Customer;
import com.magestore.app.lib.parse.ParseException;

import java.io.IOException;
import java.util.List;

/**
 * Trả lại các cấu hình config
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigGateway extends Gateway {
    Config getConfig() throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException;
    String getConfig(String configPath) throws GatewayException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
