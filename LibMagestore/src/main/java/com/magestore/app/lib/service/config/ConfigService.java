package com.magestore.app.lib.service.config;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigCustomerGroup;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.service.Service;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

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
     * Trả về CustomerGroup
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Map<String, String> getCustomerGroup() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Trả về Country
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Map<String, ConfigCountry> getCountry() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Map<String, String> getPaymentMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Map<String, String> getShippingMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException;
}