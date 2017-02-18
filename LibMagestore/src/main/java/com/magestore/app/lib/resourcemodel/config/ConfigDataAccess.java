package com.magestore.app.lib.resourcemodel.config;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import java.io.IOException;
import java.util.Map;

/**
 * Trả lại các cấu hình config
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigDataAccess extends DataAccess {
    Config retrieveConfig() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, String> getCustomerGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, ConfigCountry> getCountryGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Customer getGuestCheckout() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
}
