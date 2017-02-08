package com.magestore.app.pos.service.config;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.Shipping;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.pos.model.checkout.PosPaymentMethod;
import com.magestore.app.pos.model.config.PosConfigDefault;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Trả lại config chung của hệ thống
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSConfigService extends AbstractService implements ConfigService {
    private static final String FILE_CONFIG_PATH = "/MagestorePOS/Config/config.json";

    /**
     * Trả lại config chung từ server
     *
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Config retrieveConfig() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.retrieveConfig();
    }

    @Override
    public Map<String, String> getCustomerGroup() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCustomerGroup();
    }

    @Override
    public Map<String, ConfigCountry> getCountry() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCountryGroup();
    }

    public static String PAYMENT_METHOD_CC_DIRECT_POST = "CC_DIRECT";
    public static String PAYMENT_METHOD_CASH_IN = "CASH_IN";
    public static String PAYMENT_METHOD_CC_CARD = "CC_CARDN";
    public static String PAYMENT_METHOD_CASH_COD = "CASH_ON_DELIVERY";
    public static String PAYMENT_METHOD_CUSTOM_PAYMENT1 = "CUSTOM_PAYMENT1";
    public static String PAYMENT_METHOD_CUSTOM_PAYMENT2 = "CUSTOM_PAYMENT2";
    @Override
    public Map<String, String> getPaymentMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        Map<String, String> paymentMethodList = new HashMap<String, String>();
        paymentMethodList.put(PAYMENT_METHOD_CC_DIRECT_POST, "Credit Card Direct Post (Authorize.net)");
        paymentMethodList.put(PAYMENT_METHOD_CASH_IN, "Direct cash in");
        paymentMethodList.put(PAYMENT_METHOD_CC_CARD, "Credit direct");
        paymentMethodList.put(PAYMENT_METHOD_CASH_COD, "Cash on delivery");
        paymentMethodList.put(PAYMENT_METHOD_CUSTOM_PAYMENT1, "Custom payment 1");
        paymentMethodList.put(PAYMENT_METHOD_CUSTOM_PAYMENT2, "Custom payment 2");
        return paymentMethodList;
    }

    public static String SHIPPING_METHOD_FLAT_RATE = "";
    public static String PAYMENT_METHOD_FREE_SHIPPING = "";
    public static String PAYMENT_METHOD_STORE_PICKUP = "";
    @Override
    public Map<String, String> getShippingMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        Map<String, String> shippingMethodList = new HashMap<String, String>();
        shippingMethodList.put(SHIPPING_METHOD_FLAT_RATE, "Flat Rate - Fixed");
        shippingMethodList.put(PAYMENT_METHOD_FREE_SHIPPING, "Free Shipping - Free");
        shippingMethodList.put(PAYMENT_METHOD_STORE_PICKUP, "POS Shipping - Store Pickup");
        return shippingMethodList;
    }
}
