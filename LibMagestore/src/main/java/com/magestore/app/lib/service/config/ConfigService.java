package com.magestore.app.lib.service.config;

import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.service.Service;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigService extends Service {
    /**
     * Kết nối server lấy tham số config
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Config retrieveConfig() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Lấy tham số config từ cache luôn
     * @return
     */
    Config getConfig();
}