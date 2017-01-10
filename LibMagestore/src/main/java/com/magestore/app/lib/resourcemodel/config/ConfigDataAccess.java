package com.magestore.app.lib.resourcemodel.config;

import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;

import java.io.IOException;

/**
 * Trả lại các cấu hình config
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigDataAccess extends DataAccess {
    Config getConfig() throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
    String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, java.text.ParseException;
}
