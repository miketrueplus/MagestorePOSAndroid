package com.magestore.app.pos.api.m1.config;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.config.ActiveKey;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOption;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigPrint;
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
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.pos.api.m1.POSAPIM1;
import com.magestore.app.pos.api.m1.POSAbstractDataAccessM1;
import com.magestore.app.pos.api.m1.POSDataAccessSessionM1;
import com.magestore.app.pos.model.magento.config.PosActiveKey;
import com.magestore.app.pos.model.magento.config.PosConfig;
import com.magestore.app.pos.model.magento.config.PosConfigCountry;
import com.magestore.app.pos.model.magento.config.PosConfigDefault;
import com.magestore.app.pos.model.magento.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.magento.config.PosConfigPrint;
import com.magestore.app.pos.model.magento.config.PosConfigQuantityFormat;
import com.magestore.app.pos.model.magento.config.PosConfigRegion;
import com.magestore.app.pos.model.magento.customer.PosCustomer;
import com.magestore.app.pos.model.magento.customer.PosCustomerAddress;
import com.magestore.app.pos.model.magento.directory.PosCurrency;
import com.magestore.app.pos.model.magento.directory.PosRegion;
import com.magestore.app.pos.model.magento.setting.PosChangeCurrency;
import com.magestore.app.pos.model.magento.staff.PosLocation;
import com.magestore.app.pos.model.magento.staff.PosStaff;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosListStaffPermisson;
import com.magestore.app.pos.parse.gson2pos.Gson2PosPriceFormatParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.EncryptUntil;
import com.magestore.app.util.StringUtil;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

import javax.crypto.Cipher;

/**
 * Created by Johan on 8/3/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSConfigDataAccessM1 extends POSAbstractDataAccessM1 implements ConfigDataAccess {
    private static Config mConfig;
    private static Staff mStaff;
    private static Customer guest;
    private static CustomerAddress customerAddress;
    private static Currency currentCurrency;

    // all permission
    private static String ALL_PERMISSON = "Magestore_Webpos::all";
    // create order
    private static String CREATE_ORDER = "Magestore_Webpos::create_orders";
    // manage order
    private static String MANAGE_ALL_ORDER = "Magestore_Webpos::manage_all_order";
    private static String MANAGE_ORDER_ME = "Magestore_Webpos::manage_order_me";
    private static String MANAGE_ORDER_OTHER_STAFF = "Magestore_Webpos::manage_order_other_staff";
    private static String CAN_USE_REFUND = "Magestore_Webpos::can_use_refund";
    // manage discount
    private static String MANAGE_ALL_DISCOUNT = "Magestore_Webpos::all_discount";
    private static String APPLY_DISCOUNT_PER_CART = "Magestore_Webpos::apply_discount_per_cart";
    private static String APPLY_COUPON = "Magestore_Webpos::apply_coupon";
    private static String APPLY_DISCOUNT_PER_ITEM = "Magestore_Webpos::apply_discount_per_item";
    private static String APPLY_CUSTOM_PRICE = "Magestore_Webpos::apply_custom_price";
    // Session
    private static String MANAGE_SHIFT_ADJUSTMENT = "Magestore_Webpos::manage_shift_adjustment";

    private class ConfigEntity {
        Staff staff;
        String currency;
    }

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_CONFIG_GET_LISTING);
//            statement.setEnableCache("POSConfigDataAccess.getConfig");
//            statement.getCacheConnection().setReloadCacheLater(true);
//            statement.getCacheConnection().setForceOutOfDate(true);

//            statement.getCacheConnection().deleteCache();

            // Xây dựng tham số
            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

            // thực hiện truy vấn
            rp = statement.execute();
            String json = StringUtil.truncateJson(rp.readResult2String());
            Gson2PosConfigParseImplement implement = new Gson2PosConfigParseImplement();
            Gson gson = implement.createGson();
            mConfig = gson.fromJson(json, PosConfig.class);
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
        ActiveKey activeKey = new PosActiveKey();
        if (mConfig.getValue("webpos/general/active_key") == null) return false;

        String baseUrl = getHostUrl(POSDataAccessSessionM1.REST_BASE_URL);
        String extensionName = POSDataAccessSessionM1.REST_EXTENSION_NAME;
        String licensekey = (String) mConfig.getValue("webpos/general/active_key");
        if (licensekey.length() < 68) return false;
        CRC32 crc = new CRC32();
        String strExtensionName = licensekey.substring(0, 10) + extensionName;
        crc.update(strExtensionName.getBytes());
        int strDataCrc32 = (int) crc.getValue();
        int crc32Pos = (strDataCrc32 & 0x7FFFFFFF % 51) + 10;
        int md5Length = 32;
        String md5String = licensekey.substring(crc32Pos, (crc32Pos + md5Length));
        int md5StringLength = md5String.length();
        String key = licensekey.substring(0, crc32Pos) + licensekey.substring((crc32Pos + md5StringLength + 3), licensekey.length());
        try {
            while ((key.length() % 4) != 0) {
                key += "=";
            }
            String licenseString = decryptRSAToString(key, POSDataAccessSessionM1.REST_PUBLIC_KEY);
            if (StringUtil.isNullOrEmpty(licenseString)) return false;

            String strlicenseString = licenseString.substring(0, 3);
            String strlicensekey = licensekey.substring((crc32Pos + md5StringLength), (crc32Pos + md5StringLength + 3));
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
            if (extensionHash != crc32ExtensionName) return false;

            String licenseDomain = licenseString.substring(17, licenseString.length()).replaceAll(" ", "");
            String checkCRc32 = licensekey.substring(0, crc32Pos) + licensekey.substring((crc32Pos + md5StringLength), (crc32Pos + md5StringLength) + (licensekey.length() - crc32Pos - md5StringLength)) + extensionName + licenseDomain;
//            CRC32 crcCheck = new CRC32();
//            crcCheck.update(checkCRc32.getBytes());
//            long lcrc32String = -1;
//            try {
//                lcrc32String = Long.parseLong(crc32String);
//            } catch (Exception e) {
//            }
            String md5Check = EncryptUntil.HashMD5(checkCRc32);
            if (!md5Check.equals(md5String))
                return false;

            String strDate = licenseString.substring(11, 15);
            int resultDate = Integer.parseInt(String.valueOf(strDate), 16);
            String DATE_FORMAT = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String createdDate = sdf.format(new Date(resultDate * 24 * 3600 * 1000L));
            if (!checkSameDomain(baseUrl, licenseDomain)) return false;

            activeKey.setType(type);
            activeKey.setExpiredTime(expiredTime);
            activeKey.setCreatedDate(createdDate);
            activeKey.setLicenseDomain(licenseDomain);
            ConfigUtil.setActiveKey(activeKey);
            ConfigUtil.setIsDevLicense(type.equals("D") ? true : false);
            return true;
        } catch (Exception e) {
            String licenseDomain = "";
            if (baseUrl.contains("https://")) {
                baseUrl = baseUrl.replace("https://", "");
            } else if (baseUrl.contains("http://")) {
                baseUrl = baseUrl.replace("http://", "");
            }
            if (baseUrl.length() > 36) {
                licenseDomain = baseUrl.substring(0, 36);
            } else {
                licenseDomain = baseUrl;
            }
            String checkCRc32 = licensekey.substring(0, crc32Pos) + licensekey.substring((crc32Pos + md5StringLength), (crc32Pos + md5StringLength) + (licensekey.length() - crc32Pos - md5StringLength)) + extensionName + licenseDomain;
//            CRC32 crcCheck = new CRC32();
//            crcCheck.update(checkCRc32.getBytes());
//            long lcrc32String = -1;
//            try {
//                lcrc32String = Long.parseLong(crc32String);
//            } catch (Exception ex) {
//            }
            String md5Check = EncryptUntil.HashMD5(checkCRc32);
            if (!md5Check.equals(md5String))
                return false;
            String type = licensekey.substring(crc32Pos + md5StringLength, crc32Pos + md5StringLength + 1);
            String strexpiredTime = licensekey.substring(crc32Pos + md5StringLength + 1, crc32Pos + md5StringLength + 1 + 2);
            int expiredTime = Integer.parseInt(String.valueOf(strexpiredTime), 16);
            if (!checkSameDomain(baseUrl, licenseDomain))
                return false;
            activeKey.setType(type);
            activeKey.setExpiredTime(expiredTime);
            activeKey.setLicenseDomain(licenseDomain);
            ConfigUtil.setActiveKey(activeKey);
            ConfigUtil.setIsDevLicense(type.equals("D") ? true : false);
            return true;
        }
    }

    private String getHostUrl(String url) {
        if (StringUtil.isNullOrEmpty(url)) {
            return "";
        }
        try {
            URL host = new URL(url);
            return host.getHost();
        } catch (Exception e) {
            return url;
        }
    }

    // so sánh domain
    private boolean checkSameDomain(String domain, String licenseDomain) {
        if (domain.contains("www.")) {
            domain = domain.replace("www.", "");
        }
        if (licenseDomain.contains("www.")) {
            licenseDomain = licenseDomain.replace("www.", "");
        }

        if (domain.contains("https://")) {
            domain = domain.replace("https://", "");
        } else if (domain.contains("http://")) {
            domain = domain.replace("http://", "");
        }

        if (domain.length() > 36) {
            domain = domain.substring(0, 36);
        }

        String checkdomain = domain.substring(domain.length() - 1, domain.length());
        if (checkdomain.equals("/")) {
            domain = domain.substring(0, (domain.length() - 1));
        }

        if (licenseDomain.contains("https://")) {
            licenseDomain = licenseDomain.replace("https://", "");
        } else if (licenseDomain.contains("http://")) {
            licenseDomain = licenseDomain.replace("http://", "");
        }

        String checklicenseDomain = licenseDomain.substring(licenseDomain.length() - 1, licenseDomain.length());
        if (checklicenseDomain.equals("/")) {
            licenseDomain = licenseDomain.substring(0, (licenseDomain.length() - 1));
        }

        if (domain.equals(licenseDomain)) return true;

        return false;
    }

    // giải mã với public key
    private String decryptRSAToString(String encryptedBase64, String privateKey) {
        String decryptedString = "";
        try {
            String rStart = privateKey.replace("-----BEGIN PUBLIC KEY-----", "");
            String rEnd = rStart.replace("-----END PUBLIC KEY-----", "");
            rEnd = rEnd.replaceAll("\r", "");
            rEnd = rEnd.replaceAll("\n", "");
            rEnd = rEnd.replaceAll("\t", "");
            rEnd = rEnd.replaceAll(" ", "");
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
        return ConfigUtil.getConfigTaxClass();
    }

    @Override
    public ConfigOption retrieveColorSwatch() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();

        // trả lại giá trị
        return mConfig.getValue(configPath).toString();
    }

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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_SETTING_ACCOUNT);

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
            Map<String, ConfigRegion> mapRegion = getRegion(country_id);
            if (mapRegion != null) {
                configCountry.setRegions(mapRegion);
            }
            listConfigCountry.put(country_id, configCountry);
        }
        return listConfigCountry;
    }

    private Map<String, Map<String, ConfigRegion>> getRegionGroup() {
        // nếu chưa load config, cần khởi tạo chế độ default
        if (mConfig == null) mConfig = new PosConfigDefault();
        Map<String, LinkedTreeMap> regionGroup = (Map) mConfig.getValue("regionJson");
        Map<String, Map<String, ConfigRegion>> mapCountry = new HashMap<>();

        for (String key : regionGroup.keySet()) {
            if (!key.equals("config")) {
                Map<String, ConfigRegion> listConfigRegion = new LinkedTreeMap<>();
                Map<String, LinkedTreeMap> region = (Map<String, LinkedTreeMap>) regionGroup.get(key);
                for (String id : region.keySet()) {
                    ConfigRegion configRegion = new PosConfigRegion();
                    LinkedTreeMap item = (LinkedTreeMap) region.get(id);
                    String code = (String) item.get("code");
                    String name = (String) item.get("name");
                    configRegion.setID(id);
                    configRegion.setCode(code);
                    configRegion.setName(name);
                    listConfigRegion.put(id, configRegion);
                }
                mapCountry.put(key, listConfigRegion);
            }
        }
        return mapCountry;
    }

    private static Map<String, Map<String, ConfigRegion>> mapRegionGroup;

    private Map<String, ConfigRegion> getRegion(String country_id) {
        if (mapRegionGroup == null) {
            mapRegionGroup = getRegionGroup();
        }
        return mapRegionGroup.get(country_id);
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
                String symbol = item.get("currency_symbol").toString();
                if (symbol.length() > 0) {
                    String sSymbol = symbol.substring(0, 1);
                    if (sSymbol.equals("u")) {
                        currency_symbol = StringEscapeUtils.unescapeJava("\\" + symbol);
                    } else if (sSymbol.equals("\\")) {
                        currency_symbol = StringEscapeUtils.unescapeJava(symbol);
                    } else if (symbol.contains("\\u")) {
                        currency_symbol = StringEscapeUtils.unescapeJava(symbol);
                    } else {
                        currency_symbol = StringEscapeUtils.unescapeJava(symbol);
                    }
                }
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_STAFF_GET_LISTING);

            paramBuilder = statement.getParamBuilder()
                    .setPage(1)
                    .setPageSize(100)
                    .setSortOrderASC("display_name")
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionM1.REST_BASE_URL, POSDataAccessSessionM1.REST_USER_NAME, POSDataAccessSessionM1.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIM1.REST_SETTING_CHANGE_CURRENCY);

            paramBuilder = statement.getParamBuilder()
                    .setSessionID(POSDataAccessSessionM1.REST_SESSION_ID);

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
    public String getCurrentCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        String currentCurrencyCode = "";
        if (mConfig.getValue("baseCurrencyCode") != null) {
            currentCurrencyCode = (String) mConfig.getValue("currentCurrencyCode");
        }
        return currentCurrencyCode;
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
        if (mConfig.getValue("webpos/general/enable_session") == null) {
            return false;
        }
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

    @Override
    public boolean getConfigDeleteOrder() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (mConfig == null) mConfig = new PosConfigDefault();
        if (mConfig.getValue("webpos/general/confirm_delete_order") == null) {
            return false;
        }
        String enable_delete_order = (String) mConfig.getValue("webpos/general/confirm_delete_order");
        boolean isDeleteOrder;
        if (!StringUtil.isNullOrEmpty(enable_delete_order)) {
            if (enable_delete_order.equals("1")) {
                isDeleteOrder = true;
                return isDeleteOrder;
            }
        }

        return false;
    }

    @Override
    public void getConfigStaffPermisson(List<String> listPermisson) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (listPermisson.size() > 0) {
            ConfigUtil.setManageOrderByLocation(false);
            if (checkStaffPermiss(listPermisson, ALL_PERMISSON)) {
                ConfigUtil.setCreateOrder(true);
                ConfigUtil.setManagerShiftAdjustment(true);
                ConfigUtil.setManagerAllOrder(true);
                ConfigUtil.setDiscountPerCart(true);
                ConfigUtil.setApplyCoupon(true);
                ConfigUtil.setDiscountPerItem(true);
                ConfigUtil.setCanUseRefund(true);
                ConfigUtil.setApplyCustomPrice(true);
            } else {
                ConfigUtil.setManagerAllOrder(checkStaffPermiss(listPermisson, MANAGE_ALL_ORDER));
                ConfigUtil.setCreateOrder(checkStaffPermiss(listPermisson, CREATE_ORDER));
                if (ConfigUtil.isManagerAllOrder()) {
                    ConfigUtil.setManageOrderByMe(true);
                    ConfigUtil.setManageOrderOtherStaff(true);
                } else {
                    ConfigUtil.setManageOrderByMe(checkStaffPermiss(listPermisson, MANAGE_ORDER_ME));
                    ConfigUtil.setManageOrderOtherStaff(checkStaffPermiss(listPermisson, MANAGE_ORDER_OTHER_STAFF));
                    ConfigUtil.setCanUseRefund(checkStaffPermiss(listPermisson, CAN_USE_REFUND));
                }
                ConfigUtil.setManageAllDiscount(checkStaffPermiss(listPermisson, MANAGE_ALL_DISCOUNT));
                if (ConfigUtil.isManageAllDiscount()) {
                    ConfigUtil.setDiscountPerCart(true);
                    ConfigUtil.setApplyCoupon(true);
                    ConfigUtil.setDiscountPerItem(true);
                    ConfigUtil.setApplyCustomPrice(true);
                } else {
                    ConfigUtil.setDiscountPerCart(checkStaffPermiss(listPermisson, APPLY_DISCOUNT_PER_CART));
                    ConfigUtil.setApplyCoupon(checkStaffPermiss(listPermisson, APPLY_COUPON));
                    ConfigUtil.setDiscountPerItem(checkStaffPermiss(listPermisson, APPLY_DISCOUNT_PER_ITEM));
                    ConfigUtil.setApplyCustomPrice(checkStaffPermiss(listPermisson, APPLY_CUSTOM_PRICE));
                }
                ConfigUtil.setManagerShiftAdjustment(checkStaffPermiss(listPermisson, MANAGE_SHIFT_ADJUSTMENT));
            }
        }
    }

    private boolean checkStaffPermiss(List<String> listPermisson, String permisson) {
        boolean checkPermisson = false;
        for (String _permisson : listPermisson) {
            if (_permisson.equals(permisson)) {
                checkPermisson = true;
                return checkPermisson;
            }
        }
        return checkPermisson;
    }

    private ConfigPriceFormat getPriceFormat(LinkedTreeMap priceFormat) {
        String currencySymbol = (String) mConfig.getValue("currentCurrencySymbol");
        String currency_symbol = "";
        if (currencySymbol.length() > 0) {
            String sSymbol = currencySymbol.substring(0, 1);
            if (sSymbol.equals("u")) {
                currency_symbol = StringEscapeUtils.unescapeJava("\\" + currencySymbol);
            } else if (sSymbol.equals("\\")) {
                currency_symbol = StringEscapeUtils.unescapeJava(currencySymbol);
            } else if (currencySymbol.contains("\\u")) {
                currency_symbol = StringEscapeUtils.unescapeJava(currencySymbol);
            } else {
                currency_symbol = StringEscapeUtils.unescapeJava(currencySymbol);
            }
        }
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
        configPriceFormat.setCurrencySymbol(currency_symbol);

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
