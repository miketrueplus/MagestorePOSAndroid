package com.magestore.app.pos.api.m2.config;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.model.config.PosConfigCountry;
import com.magestore.app.pos.model.config.PosConfigDefault;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.config.PosConfigRegion;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSConfigDataAccess extends POSAbstractDataAccess implements ConfigDataAccess {
    // Cache config đầu tiên
    private static Config mConfig;

    /**
     * Trả lại 1 danh sách các config
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public Config retrieveConfig() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Class oldImplement = getClassParseImplement();
        setParseImplement(Gson2PosConfigParseImplement.class);

        Connection connection = null;
        Statement statement = null;
        ParamBuilder paramBuilder = null;
//        Thread thread = null;
        ResultReading rp = null;

        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CONFIG_GET_LISTING);
            statement.setEnableCache("POSConfigDataAccess.getConfig");
//            statement.getCacheConnection().deleteCache();
//            statement.getCacheConnection().setForceOutOfDate(true);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            // thực hiện truy vấn
            rp = statement.execute();
            rp.setParseImplement(Gson2PosConfigParseImplement.class);
            rp.setParseModel(PosConfig.class);
            mConfig = (Config) rp.doParse();
            return mConfig;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
//            if (thread != null)
//                thread.start();
            // đóng param builder
//            if (paramBuilder != null) paramBuilder.clear();
//            paramBuilder = null;

            // đóng statement
//            if (statement != null)statement.close();
//            statement = null;

            // đóng connection
//            if (connection != null) connection.close();
//            connection = null;
        }
    }

    /**
     * Trả lại 1 config dưới dạng 1 string
     *
     * @param configPath
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws java.text.ParseException
     */
    @Override
    public String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        // trả lại giá trị
        return mConfig.getValue(configPath).toString();
    }

    /**
     * Lấy customer group trong config
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Map<String, String> getCustomerGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        // Chuyển đối customer
        ArrayList<LinkedTreeMap> customerGroupList = (ArrayList) mConfig.getValue("customerGroup");
        LinkedTreeMap<String, String> returnCustomerGroup = new LinkedTreeMap<String, String>();
        for (LinkedTreeMap customerGroup : customerGroupList) {
            Double id = (Double) customerGroup.get("id");
            returnCustomerGroup.put(String.format("%.0f", id), customerGroup.get("code").toString());
        }
        return returnCustomerGroup;
    }

    /**
     * Lấy country trong config
     *
     * @return
     * @throws DataAccessException
     * @throws ConnectionException
     * @throws ParseException
     * @throws IOException
     * @throws ParseException
     */
    @Override
    public Map<String, ConfigCountry> getCountryGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        ArrayList<LinkedTreeMap> countryList = (ArrayList) mConfig.getValue("country");
        Map<String, ConfigCountry> listConfigCountry = new HashMap<String, ConfigCountry>();
        for (LinkedTreeMap country : countryList) {
            ConfigCountry configCountry = new PosConfigCountry();
            String country_id = country.get("country_id").toString();
            String country_name = country.get("country_name").toString();
            configCountry.setCountryID(country_id);
            configCountry.setCountryName(country_name);
            ArrayList<LinkedTreeMap> regionList = (ArrayList) country.get("regions");
            if (regionList != null) {
                List<PosConfigRegion> listConfigRegion = new ArrayList<PosConfigRegion>();
                for (LinkedTreeMap region : regionList) {
                    ConfigRegion configRegion = new PosConfigRegion();
                    String code = region.get("code").toString();
                    String name = region.get("name").toString();
                    String id = region.get("id").toString();
                    configRegion.setID(id);
                    configRegion.setCode(code);
                    configRegion.setName(name);
                    listConfigRegion.add((PosConfigRegion) configRegion);
                }
                configCountry.setRegions(listConfigRegion);
            }
            listConfigCountry.put(country_id, configCountry);
        }
        // TODO: chưa sort country, region
        return listConfigCountry;
    }

    @Override
    public Customer getGuestCheckout() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        String customer_id = (String) mConfig.getValue("webpos/guest_checkout/customer_id");
        String email = (String) mConfig.getValue("webpos/guest_checkout/email");
        String first_name = (String) mConfig.getValue("webpos/guest_checkout/first_name");
        String last_name = (String) mConfig.getValue("webpos/guest_checkout/last_name");
        String full_name = first_name + " " + last_name;
        String street = (String) mConfig.getValue("webpos/guest_checkout/street");
        String country_id = (String) mConfig.getValue("webpos/guest_checkout/country_id");
        String city = (String) mConfig.getValue("webpos/guest_checkout/city");
        String region_id = (String) mConfig.getValue("webpos/guest_checkout/region_id");
        String zip_code = (String) mConfig.getValue("webpos/guest_checkout/zip");
        String telephone = (String) mConfig.getValue("webpos/guest_checkout/telephone");

        Customer guest = new PosCustomer();
        guest.setID(customer_id);
        guest.setEmail(email);
        guest.setFirstName(first_name);
        guest.setLastName(last_name);
        guest.setName(full_name);
        guest.setTelephone(telephone);
        List<CustomerAddress> listAddress = new ArrayList<CustomerAddress>();
        CustomerAddress customerAddress = new PosCustomerAddress();
        customerAddress.setCustomer(customer_id);
        customerAddress.setFirstName(first_name);
        customerAddress.setLastName(last_name);
        customerAddress.setTelephone(telephone);
        customerAddress.setCity(city);
        customerAddress.setPostCode(zip_code);
        customerAddress.setCountry(country_id);
        customerAddress.setStreet1(street);
        customerAddress.setRegionID(region_id);
        Region region = new PosRegion();
        try {
            region.setRegionID(Integer.parseInt(region_id));
        } catch (Exception e) {
            region.setRegionID(0);
        }
        customerAddress.setRegion(region);
        listAddress.add(customerAddress);
        guest.setAddressList(listAddress);

        return guest;
    }

    @Override
    public List<Currency> getCurrencies() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        ArrayList<LinkedTreeMap> currencyList = (ArrayList) mConfig.getValue("currencies");
        List<Currency> listCurrency = new ArrayList<>();

        for (LinkedTreeMap item : currencyList) {
            Currency currency = new PosCurrency();
            String code = item.get("code").toString();
            String currency_name = item.get("currency_name").toString();
            String currency_symbol = item.get("currency_symbol").toString();
            String is_default = item.get("is_default").toString();
            String currency_rate = item.get("currency_rate").toString();
            currency.setCode(code);
            currency.setCurrenyName(currency_name);
            currency.setCurrencySymbol(currency_symbol);
            currency.setIsDefault(is_default);
            try {
                currency.setCurrencyRate(Double.parseDouble(currency_rate));
            } catch (Exception e) {
            }
            listCurrency.add(currency);
        }

        return listCurrency;
    }

    @Override
    public Currency getDefaultCurrency() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        List<Currency> listCurrency = getCurrencies();
        Currency dCurrentcy = new PosCurrency();
        if (listCurrency != null && listCurrency.size() > 0) {
            boolean checkCurrency = false;
            for (Currency currency : listCurrency) {
                if (currency.getIsDefault().equals("1")) {
                    checkCurrency = true;
                    dCurrentcy = currency;
                }
            }
            if (!checkCurrency) {
                dCurrentcy = listCurrency.get(0);
            }
        }
        return dCurrentcy;
    }

    @Override
    public ConfigPriceFormat getPriceFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        LinkedTreeMap priceFormat = (LinkedTreeMap) mConfig.getValue("priceFormat");

        return getPriceFormat(priceFormat);
    }

    @Override
    public ConfigPriceFormat getBasePriceFomat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        LinkedTreeMap priceFormat = (LinkedTreeMap) mConfig.getValue("basePriceFormat");

        return getPriceFormat(priceFormat);
    }

    @Override
    public Map<String, String> getConfigCCTypes() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        Map<String, Object> cc_types = (Map) mConfig.getValue("cc_types");

        Map<String, String> listCCTypes = new HashMap<>();

        for (String key : cc_types.keySet()) {
            String value = cc_types.get(key).toString();
            listCCTypes.put(key, value);
        }

        return listCCTypes;
    }

    @Override
    public List<String> getConfigMonths() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        List<String> listCCMonths = (List) mConfig.getValue("cc_months");
        return listCCMonths;
    }

    @Override
    public Map<String, String> getConfigCCYears() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        Map<String, Object> cc_years = (Map) mConfig.getValue("cc_years");

        Map<String, String> listCCYears = new HashMap<>();

        for (String key : cc_years.keySet()) {
            String value = cc_years.get(key).toString();
            listCCYears.put(key, value);
        }

        return listCCYears;
    }

    private ConfigPriceFormat getPriceFormat(LinkedTreeMap priceFormat) {
        String pattern = priceFormat.get("pattern").toString();
        int precision = (int) priceFormat.get("precision");
        int requiredPrecision = (int) priceFormat.get("requiredPrecision");
        String decimalSymbol = priceFormat.get("decimalSymbol").toString();
        String groupSymbol = priceFormat.get("groupSymbol").toString();
        int groupLength = (int) priceFormat.get("groupLength");
        int integerRequired = (int) priceFormat.get("integerRequired");

        ConfigPriceFormat configPriceFormat = new PosConfigPriceFormat();
        configPriceFormat.setPattern(pattern);
        configPriceFormat.setPrecision(precision);
        configPriceFormat.setRequirePrecision(requiredPrecision);
        configPriceFormat.setDecimalSymbol(decimalSymbol);
        configPriceFormat.setGroupSymbol(groupSymbol);
        configPriceFormat.setGroupLength(groupLength);
        configPriceFormat.setIntegerRequied(integerRequired);

        return configPriceFormat;
    }
}
