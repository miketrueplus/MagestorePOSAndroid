package com.magestore.app.pos.api.m2.config;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOption;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigPrint;
import com.magestore.app.lib.model.config.ConfigProductOption;
import com.magestore.app.lib.model.config.ConfigQuantityFormat;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.model.setting.ChangeCurrency;
import com.magestore.app.lib.model.staff.Location;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.model.staff.StaffPermisson;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.pos.api.m2.POSAPI;
import com.magestore.app.pos.api.m2.POSAbstractDataAccess;
import com.magestore.app.pos.api.m2.POSDataAccessSession;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.pos.model.config.PosConfigCountry;
import com.magestore.app.pos.model.config.PosConfigDefault;
import com.magestore.app.pos.model.config.PosConfigOption;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.config.PosConfigPrint;
import com.magestore.app.pos.model.config.PosConfigQuantityFormat;
import com.magestore.app.pos.model.config.PosConfigRegion;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.model.setting.PosChangeCurrency;
import com.magestore.app.pos.model.staff.PosLocation;
import com.magestore.app.pos.model.staff.PosStaff;
import com.magestore.app.pos.parse.gson2pos.Gson2PosColorSwatchParse;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListStaffPermisson;
import com.magestore.app.pos.parse.gson2pos.Gson2PosPriceFormatParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListTaxClass;
import com.magestore.app.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

import javax.crypto.Cipher;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class POSConfigDataAccess extends POSAbstractDataAccess implements ConfigDataAccess {
    // Cache config đầu tiên
    private static Config mConfig;
    private static Staff mStaff;
    private static Customer guest;
    private static CustomerAddress customerAddress;
    private static Currency currentCurrency;
    private static List<ConfigTaxClass> listConfigTax;
    private String publicKey = "-----BEGIN PUBLIC KEY-----MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJ8EDi+a0lilUChsDba33FrcHLZZZIMxT7XhyEP3J3llQXNJkflG+5GzBvFTd+B1pvpc45WOktNReyPDZ/OMNukCAwEAAQ==-----END PUBLIC KEY-----";
    private String extensionName = "retailer-pos";

    private class ConfigEntity {
        Staff staff;
        String currency;
    }

    private class POSConfigPriceDataAcess {
        ConfigPriceFormat priceFormat;
    }

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
//            statement.setEnableCache("POSConfigDataAccess.getConfig");
//            statement.getCacheConnection().setReloadCacheLater(true);
//            statement.getCacheConnection().setForceOutOfDate(true);

//            statement.getCacheConnection().deleteCache();

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
//            statement.getCacheConnection().deleteCache();
            throw ex;
        } catch (IOException ex) {
//            statement.getCacheConnection().deleteCache();
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

    @Override
    public boolean checkLicenseKey() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        String licensekey = (String) mConfig.getValue("webpos/general/active_key");
        if (licensekey.length() < 68) return false;

        CRC32 crc = new CRC32();
        String strExtensionName = licensekey.substring(0, 10) + extensionName;
        crc.update(strExtensionName.getBytes());
        int strDataCrc32 = (int) crc.getValue();
        int crc32Pos = (strDataCrc32 & 0x7FFFFFFF % 49) + 10;
        String crc32String = licensekey.substring(crc32Pos, (crc32Pos + 10));
        String key = licensekey.substring(0, crc32Pos) + licensekey.substring((crc32Pos + 13), licensekey.length());
        while ((key.length() % 4) != 0) {
            key += "=";
        }
        String licenseString = decryptRSAToString(key, publicKey);
        if (StringUtil.isNullOrEmpty(licenseString)) return false;

        String strlicenseString = licenseString.substring(0, 3);
        String strlicensekey = licensekey.substring((crc32Pos + 10), (crc32Pos + 10 + 3));
        if (!strlicenseString.equals(strlicensekey)) return false;

        String type = licenseString.substring(0, 1);
        String strexpiredTime = licenseString.substring(1, 3);
        int expiredTime = Integer.parseInt(String.valueOf(strexpiredTime), 16);
        long extensionHash = -1;
        try {
            extensionHash = Long.parseLong(licenseString.substring(3, 13));
        } catch (Exception e) {
        }
        CRC32 crcExtensionName = new CRC32();
        crcExtensionName.update(extensionName.getBytes());
        long crc32ExtensionName = crcExtensionName.getValue();
        if (extensionHash != crc32ExtensionName)
            return false;

        return false;
    }

    private String decryptRSAToString(String encryptedBase64, String privateKey) {
        String rStart = privateKey.replace("-----BEGIN PUBLIC KEY-----", "");
        String rEnd = rStart.replace("-----END PUBLIC KEY-----", "");
        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");

            PublicKey publicKey = keyFac.generatePublic(new X509EncodedKeySpec(Base64.decode(rEnd.toString(), Base64.DEFAULT)));

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }

    @Override
    public List<ConfigTaxClass> retrieveConfigTaxClass() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CONFIG_TAX_CLASS);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(200)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListTaxClass.class);
            Gson2PosListTaxClass listTaxClass = (Gson2PosListTaxClass) rp.doParse();
            listConfigTax = (List<ConfigTaxClass>) (List<?>) (listTaxClass.items);
            return listConfigTax;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public ConfigOption retrieveColorSwatch() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        setParseImplement(Gson2PosColorSwatchParse.class);
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_CONFIG_COLOR_SWATCH);

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(200)
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            rp = statement.execute();
            rp.setParseImplement(Gson2PosColorSwatchParse.class);
            rp.setParseModel(PosConfigOption.class);
            return (ConfigOption) rp.doParse();
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
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
        List<LinkedTreeMap> customerGroupList = (ArrayList) mConfig.getValue("customerGroup");
        LinkedTreeMap<String, String> returnCustomerGroup = new LinkedTreeMap<String, String>();
        for (LinkedTreeMap customerGroup : customerGroupList) {
            Double id = (Double) customerGroup.get("id");
            returnCustomerGroup.put(String.format("%.0f", id), customerGroup.get("code").toString());
        }
        return returnCustomerGroup;
    }

    @Override
    public Staff getStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        String staff_id = (String) mConfig.getValue("staffId");
        String staff_name = (String) mConfig.getValue("staffName");
        String location_id = (String) mConfig.getValue("locationId");
        String location_name = (String) mConfig.getValue("location_name");
        String location_address = (String) mConfig.getValue("location_address");

        Staff staff = new PosStaff();
        staff.setStaffId(staff_id);
        staff.setStaffName(staff_name);
        Location location = new PosLocation();
        location.setLocationId(location_id);
        location.setLocationName(location_name);
        location.setLocationAddress(location_address);
        staff.setStaffLocation(location);
        mStaff = staff;
        return mStaff;
    }

    @Override
    public void setStaff(Staff staff) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        mStaff = staff;
    }

    @Override
    public Staff changeInformationStaff(Staff staff) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_SETTING_ACCOUNT);

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            ConfigEntity configEntity = new ConfigEntity();
            configEntity.staff = staff;

            rp = statement.execute(configEntity);

            String reponse = StringUtil.truncateJson(rp.readResult2String());

            JSONObject jsonObject = new JSONObject(reponse);
            String error = jsonObject.getString("error");
            String message = jsonObject.getString("message");

            if (error.equals("0")) {
                staff.setResponeType(true);
            } else {
                staff.setResponeType(false);
            }
            staff.setErrorMessage(message);
            return staff;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
        return null;
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

        List<LinkedTreeMap> countryList = (ArrayList) mConfig.getValue("country");
        Map<String, ConfigCountry> listConfigCountry = new LinkedTreeMap<>();
        Collections.sort(countryList, new Comparator<LinkedTreeMap>() {
            @Override
            public int compare(LinkedTreeMap linkedTreeMap, LinkedTreeMap linkedTreeMap1) {
                String name = linkedTreeMap.get("country_name").toString();
                String name1 = linkedTreeMap1.get("country_name").toString();
                return name.compareToIgnoreCase(name1);
            }
        });
        for (LinkedTreeMap country : countryList) {
            ConfigCountry configCountry = new PosConfigCountry();
            String country_id = country.get("country_id").toString();
            String country_name = country.get("country_name").toString();
            configCountry.setCountryID(country_id);
            configCountry.setCountryName(country_name);
            List<LinkedTreeMap> regionList = (ArrayList) country.get("regions");
            if (regionList != null) {
                Map<String, ConfigRegion> listConfigRegion = new LinkedTreeMap<>();
                Collections.sort(regionList, new Comparator<LinkedTreeMap>() {
                    @Override
                    public int compare(LinkedTreeMap linkedTreeMap, LinkedTreeMap linkedTreeMap1) {
                        String name = linkedTreeMap.get("name").toString();
                        String name1 = linkedTreeMap1.get("name").toString();
                        return name.compareToIgnoreCase(name1);
                    }
                });
                for (LinkedTreeMap region : regionList) {
                    ConfigRegion configRegion = new PosConfigRegion();
                    String code = region.get("code").toString();
                    String name = region.get("name").toString();
                    String id = region.get("id").toString();
                    configRegion.setID(id);
                    configRegion.setCode(code);
                    configRegion.setName(name);
                    listConfigRegion.put(id, configRegion);
                }
                configCountry.setRegions(listConfigRegion);
            }
            listConfigCountry.put(country_id, configCountry);
        }
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

        if (guest == null) {
            guest = new PosCustomer();
        }
        guest.setID(customer_id);
        guest.setEmail(email);
        guest.setFirstName(first_name);
        guest.setLastName(last_name);
        guest.setName(full_name);
        guest.setTelephone(telephone);
        List<CustomerAddress> listAddress = new ArrayList<CustomerAddress>();
        if (customerAddress == null) {
            customerAddress = new PosCustomerAddress();
        }
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

        List<LinkedTreeMap> currencyList = (ArrayList) mConfig.getValue("currencies");
        List<Currency> listCurrency = new ArrayList<>();

        for (LinkedTreeMap item : currencyList) {
            Currency currency = new PosCurrency();
            String code = item.get("code").toString();
            String currency_name = item.get("currency_name").toString();
            String currency_symbol = "";
            if (item.get("currency_symbol") != null) {
                currency_symbol = item.get("currency_symbol").toString();
            }
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

//    public Map<String, String> getPriceFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {

//        mConfig
//
//        // nếu chưa load config, cần khởi tạo chế độ default
//        if (mConfig == null) mConfig = new PosConfigDefault();
//
//        // trả lại giá trị
//        return mConfig.getValue("priceFormat").toString();
//    }

    @Override
    public Currency getDefaultCurrency() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (currentCurrency == null) {
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
                currentCurrency = dCurrentcy;
            }
        }

        return currentCurrency;
    }

    @Override
    public ConfigPriceFormat getPriceFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        LinkedTreeMap priceFormat = (LinkedTreeMap) mConfig.getValue("priceFormat");

        return getPriceFormat(priceFormat);
    }

    @Override
    public ConfigQuantityFormat getQuantityFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        LinkedTreeMap priceFormat = (LinkedTreeMap) mConfig.getValue("priceFormat");

        return getQuantityFormat(priceFormat);
    }

    @Override
    public ConfigPriceFormat getBasePriceFomat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        LinkedTreeMap priceFormat = (LinkedTreeMap) mConfig.getValue("basePriceFormat");

        return getPriceFormat(priceFormat);
    }

    @Override
    public List<String> getStaffPermisson() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        List<String> listPermisson = new ArrayList<>();
        if (mConfig.getValue("staffResourceAccess") != null) {
            listPermisson = (List) mConfig.getValue("staffResourceAccess");
        }

        return listPermisson;
    }

    @Override
    public List<StaffPermisson> retrieveStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_STAFF_GET_LISTING);

            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(200)
                    .setSortOrderASC("display_name")
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);
            rp = statement.execute();
            rp.setParseImplement(getClassParseImplement());
            rp.setParseModel(Gson2PosListStaffPermisson.class);
            Gson2PosListStaffPermisson listStaff = (Gson2PosListStaffPermisson) rp.doParse();
            List<StaffPermisson> list = (List<StaffPermisson>) (List<?>) (listStaff.items);
            return list;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public Map<String, String> getConfigCCTypes() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        Map<String, Object> cc_types = (Map) mConfig.getValue("cc_types");

        Map<String, String> listCCTypes = new LinkedTreeMap<>();

        for (String key : cc_types.keySet()) {
            if (!key.equals("")) {
                String value = cc_types.get(key).toString();
                listCCTypes.put(key, value);
            }
        }

        return listCCTypes;
    }

    @Override
    public List<String> getConfigMonths() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        List<String> listCCMonths = (List) mConfig.getValue("cc_months");

        for (String month : listCCMonths) {
            if (month.equals("Month") || month.equals("month")) {
                listCCMonths.remove(month);
                break;
            }
        }

        for (String month : listCCMonths) {
            String sub = month.substring(0, 2);
            listCCMonths.set(listCCMonths.indexOf(month), sub);
        }
        return listCCMonths;
    }

    @Override
    public Map<String, String> getConfigCCYears() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        Map<String, Object> cc_years = (Map) mConfig.getValue("cc_years");

        Map<String, String> listCCYears = new LinkedTreeMap<>();

        for (String key : cc_years.keySet()) {
            if (!key.equals("0")) {
                double value = (double) cc_years.get(key);
                int intValue = (int) value;
                listCCYears.put(key, String.valueOf(intValue));
            }
        }

        return listCCYears;
    }

    @Override
    public ConfigPrint getConfigPrint() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        String auto_print = (String) mConfig.getValue("webpos/receipt/general/auto_print");
        String font_type = (String) mConfig.getValue("webpos/receipt/content/font_type");
        String header_text = (String) mConfig.getValue("webpos/receipt/content/header_text");
        String footer_text = (String) mConfig.getValue("webpos/receipt/content/footer_text");
        String show_receipt_logo = (String) mConfig.getValue("webpos/receipt/optional/show_receipt_logo");
        String path_logo = (String) mConfig.getValue("webpos/general/webpos_logo_url");
        String show_cashier_name = (String) mConfig.getValue("webpos/receipt/optional/show_cashier_name");
        String show_comment = (String) mConfig.getValue("webpos/receipt/optional/show_comment");

        ConfigPrint configPrint = new PosConfigPrint();
        configPrint.setAutoPrint(auto_print);
        configPrint.setFontType(font_type);
        configPrint.setHeaderText(header_text);
        configPrint.setFooterText(footer_text);
        configPrint.setShowReceiptLogo(show_receipt_logo);
        configPrint.setPathLogo(path_logo);
        configPrint.setShowCashierName(show_cashier_name);
        configPrint.setShowComment(show_comment);

        return configPrint;
    }

    @Override
    public ChangeCurrency changeCurrency(String code) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Connection connection = null;
        Statement statement = null;
        ResultReading rp = null;
        ParamBuilder paramBuilder = null;
        try {
            // Khởi tạo connection và khởi tạo truy vấn
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSession.REST_BASE_URL, POSDataAccessSession.REST_USER_NAME, POSDataAccessSession.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPI.REST_SETTING_CHANGE_CURRENCY);

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSession.REST_SESSION_ID);

            ConfigEntity configEntity = new ConfigEntity();
            configEntity.currency = code;

            rp = statement.execute(configEntity);

            String reponse = StringUtil.truncateJson(rp.readResult2String());

            Gson2PosPriceFormatParseImplement implement = new Gson2PosPriceFormatParseImplement();
            Gson gson = implement.createGson();
            PosChangeCurrency priceFormat = gson.fromJson(reponse, PosChangeCurrency.class);
            List<Currency> listCurrency = getCurrencies();
            currentCurrency = priceFormat.getCurrency();
            if (listCurrency != null && listCurrency.size() > 0) {
                for (Currency currency : listCurrency) {
                    if (currency.getCode().equals(code)) {
                        currentCurrency = currency;
                        priceFormat.setCurrency(currency);
                    }
                }
            }
            return priceFormat;
        } catch (ConnectionException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
            // đóng result reading
            if (rp != null) rp.close();
            rp = null;

            if (paramBuilder != null) paramBuilder.clear();
            paramBuilder = null;

            // đóng statement
            if (statement != null) statement.close();
            statement = null;

            // đóng connection
            if (connection != null) connection.close();
            connection = null;
        }
    }

    @Override
    public String getBaseCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        String baseCurrencyCode = "";
        if (mConfig.getValue("baseCurrencyCode") != null) {
            baseCurrencyCode = (String) mConfig.getValue("baseCurrencyCode");
        }
        return baseCurrencyCode;
    }

    @Override
    public float getConfigMaximumDiscount() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        if (mConfig.getValue("maximum_discount_percent") instanceof String) {
            String maximum_discount = (String) mConfig.getValue("maximum_discount_percent");
            float cmaximum = 0;
            try {
                cmaximum = Float.parseFloat(maximum_discount);
            } catch (Exception e) {
                cmaximum = 0;
            }
            return cmaximum;
        } else if (mConfig.getValue("maximum_discount_percent") instanceof Double) {
            double maximum_discount = (double) mConfig.getValue("maximum_discount_percent");
            return (float) maximum_discount;
        } else {
            return 100;
        }
    }

    @Override
    public boolean getConfigDeliveryTime() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();

        String enable_delivery_time = (String) mConfig.getValue("webpos/general/enable_delivery_date");
        boolean isShowDelivery;
        if (!StringUtil.isNullOrEmpty(enable_delivery_time)) {
            if (enable_delivery_time.equals("1")) {
                isShowDelivery = true;
                return isShowDelivery;
            }
        }

        return false;
    }

    @Override
    public boolean getConfigStoreCredit() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        if (mConfig.getValue("plugins_config") != null) {
            if (mConfig.getValue("plugins_config") instanceof LinkedTreeMap) {
                LinkedTreeMap plugins_config = (LinkedTreeMap) mConfig.getValue("plugins_config");
                LinkedTreeMap os_store_credit = (LinkedTreeMap) plugins_config.get("os_store_credit");
                if (os_store_credit != null) {
                    String enable_store_credit = (String) os_store_credit.get("customercredit/general/enable");
                    boolean isShowStoreCredit;
                    if (!StringUtil.isNullOrEmpty(enable_store_credit)) {
                        if (enable_store_credit.equals("1")) {
                            isShowStoreCredit = true;
                            return isShowStoreCredit;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean getConfigRewardPoint() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        if (mConfig.getValue("plugins_config") != null) {
            if (mConfig.getValue("plugins_config") instanceof LinkedTreeMap) {
                LinkedTreeMap plugins_config = (LinkedTreeMap) mConfig.getValue("plugins_config");
                LinkedTreeMap os_reward_points = (LinkedTreeMap) plugins_config.get("os_reward_points");
                if (os_reward_points != null) {
                    String enable_store_credit = (String) os_reward_points.get("rewardpoints/general/enable");
                    boolean isShowStoreCredit;
                    if (!StringUtil.isNullOrEmpty(enable_store_credit)) {
                        if (enable_store_credit.equals("1")) {
                            isShowStoreCredit = true;
                            return isShowStoreCredit;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean getConfigGiftCard() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        if (mConfig.getValue("plugins_config") != null) {
            if (mConfig.getValue("plugins_config") instanceof LinkedTreeMap) {
                LinkedTreeMap plugins_config = (LinkedTreeMap) mConfig.getValue("plugins_config");
                LinkedTreeMap os_gift_card = (LinkedTreeMap) plugins_config.get("os_gift_card");
                if (os_gift_card != null) {
                    String enable_gift_card = (String) os_gift_card.get("giftvoucher/general/active");
                    boolean isShowGiftCard;
                    if (!StringUtil.isNullOrEmpty(enable_gift_card)) {
                        if (enable_gift_card.equals("1")) {
                            isShowGiftCard = true;
                            return isShowGiftCard;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean getConfigSession() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        String enable_session = (String) mConfig.getValue("webpos/general/enable_session");
        boolean isSession;
        if (!StringUtil.isNullOrEmpty(enable_session)) {
            if (enable_session.equals("1")) {
                isSession = true;
                return isSession;
            }
        }

        return false;
    }

    private ConfigPriceFormat getPriceFormat(LinkedTreeMap priceFormat) {
        String currencySymbol = (String) mConfig.getValue("currentCurrencySymbol");
        String pattern = priceFormat.get("pattern").toString();
        int precision = ((Double) priceFormat.get("precision")).intValue();
        int requiredPrecision = ((Double) priceFormat.get("requiredPrecision")).intValue();
        String decimalSymbol = priceFormat.get("decimalSymbol").toString();
        String groupSymbol = priceFormat.get("groupSymbol").toString();
        int groupLength = ((Double) priceFormat.get("groupLength")).intValue();
        int integerRequired = ((Double) priceFormat.get("integerRequired")).intValue();

        ConfigPriceFormat configPriceFormat = new PosConfigPriceFormat();
        configPriceFormat.setPattern(pattern);
        configPriceFormat.setPrecision(precision);
        configPriceFormat.setRequirePrecision(requiredPrecision);
        configPriceFormat.setDecimalSymbol(decimalSymbol);
        configPriceFormat.setGroupSymbol(groupSymbol);
        configPriceFormat.setGroupLength(groupLength);
        configPriceFormat.setIntegerRequied(integerRequired);
        configPriceFormat.setCurrencySymbol(currencySymbol);

        return configPriceFormat;
    }

    private ConfigQuantityFormat getQuantityFormat(LinkedTreeMap quantityFormat) {
        String currencySymbol = (String) mConfig.getValue("currentCurrencySymbol");
        String pattern = quantityFormat.get("pattern").toString();
        int precision = ((Double) quantityFormat.get("precision")).intValue();
        int requiredPrecision = ((Double) quantityFormat.get("requiredPrecision")).intValue();
        String decimalSymbol = quantityFormat.get("decimalSymbol").toString();
        String groupSymbol = quantityFormat.get("groupSymbol").toString();
        int groupLength = ((Double) quantityFormat.get("groupLength")).intValue();
        int integerRequired = 0;

        ConfigQuantityFormat configQuantityFormat = new PosConfigQuantityFormat();
        configQuantityFormat.setPattern(pattern);
        configQuantityFormat.setPrecision(precision);
        configQuantityFormat.setRequirePrecision(requiredPrecision);
        configQuantityFormat.setDecimalSymbol(decimalSymbol);
        configQuantityFormat.setGroupSymbol(groupSymbol);
        configQuantityFormat.setGroupLength(groupLength);
        configQuantityFormat.setIntegerRequied(integerRequired);
//        configQuantityFormat.setCurrencySymbol(currencySymbol);

        return configQuantityFormat;
    }
}
