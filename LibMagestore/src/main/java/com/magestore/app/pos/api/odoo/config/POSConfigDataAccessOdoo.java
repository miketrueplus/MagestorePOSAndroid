package com.magestore.app.pos.api.odoo.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.LinkedHashTreeMap;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ConnectionException;
import com.magestore.app.lib.connection.ConnectionFactory;
import com.magestore.app.lib.connection.ParamBuilder;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.model.config.ActiveKey;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOdoo;
import com.magestore.app.lib.model.config.ConfigOption;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigPrint;
import com.magestore.app.lib.model.config.ConfigQuantityFormat;
import com.magestore.app.lib.model.config.ConfigRegion;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.config.DataConfig;
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
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.config.PosActiveKey;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.pos.model.config.PosConfigCountry;
import com.magestore.app.pos.model.config.PosConfigOdoo;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.config.PosConfigQuantityFormat;
import com.magestore.app.pos.model.config.PosConfigRegion;
import com.magestore.app.pos.model.config.PosDataConfig;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.model.staff.PosLocation;
import com.magestore.app.pos.model.staff.PosStaff;
import com.magestore.app.pos.parse.gson2pos.Gson2PosAbstractParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseModelOdoo;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.EncryptUntil;
import com.magestore.app.util.StringUtil;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSConfigDataAccessOdoo extends POSAbstractDataAccessOdoo implements ConfigDataAccess {
    private static Config mConfig;
    private static Staff mStaff;
    private static ConfigOdoo mConfigOdoo;
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

    private class StaffEntity {
        String name;
        String old_password;
        String new_password;
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_CONFIG_GET_LISTING);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            // thực hiện truy vấn
            rp = statement.execute();
            rp.setParseImplement(new Gson2PosConfigParseModelOdoo());
            rp.setParseModel(PosDataConfig.class);
            DataConfig dataConfig = (DataConfig) rp.doParse();
            mConfigOdoo = dataConfig.getItems().get(0);
            return mConfig;
        } catch (ConnectionException ex) {
        } catch (IOException ex) {
        } finally {
        }
        return null;
    }

    @Override
    public boolean checkLicenseKey() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        ActiveKey activeKey = new PosActiveKey();
        if (mConfigOdoo.getActiveKey() == null) return false;
        String baseUrl = StringUtil.getHostUrl(POSDataAccessSessionOdoo.REST_BASE_URL);
        String extensionName = POSDataAccessSessionOdoo.REST_EXTENSION_NAME;
        String licensekey = mConfigOdoo.getActiveKey();
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
            String licenseString = StringUtil.decryptRSAToString(key, POSDataAccessSessionOdoo.REST_PUBLIC_KEY);
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
            String md5Check = EncryptUntil.HashMD5(checkCRc32);
            if (!md5Check.equals(md5String))
                return false;

            String strDate = licenseString.substring(11, 15);
            int resultDate = Integer.parseInt(String.valueOf(strDate), 16);
            String DATE_FORMAT = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            String createdDate = sdf.format(new Date(resultDate * 24 * 3600 * 1000L));
            if (!StringUtil.checkSameDomain(baseUrl, licenseDomain)) return false;

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
            String md5Check = EncryptUntil.HashMD5(checkCRc32);
            if (!md5Check.equals(md5String))
                return false;
            String type = licensekey.substring(crc32Pos + md5StringLength, crc32Pos + md5StringLength + 1);
            String strexpiredTime = licensekey.substring(crc32Pos + md5StringLength + 1, crc32Pos + md5StringLength + 1 + 2);
            int expiredTime = Integer.parseInt(String.valueOf(strexpiredTime), 16);
            if (!StringUtil.checkSameDomain(baseUrl, licenseDomain))
                return false;
            activeKey.setType(type);
            activeKey.setExpiredTime(expiredTime);
            activeKey.setLicenseDomain(licenseDomain);
            ConfigUtil.setActiveKey(activeKey);
            ConfigUtil.setIsDevLicense(type.equals("D") ? true : false);
            return true;
        }
    }

    @Override
    public List<ConfigTaxClass> retrieveConfigTaxClass() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public ConfigOption retrieveColorSwatch() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public Map<String, String> getCustomerGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getCustomerGroup();
    }

    @Override
    public Staff getStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Location location = new PosLocation();
        Staff staff = mConfigOdoo.getStaff();
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
            connection = ConnectionFactory.generateConnection(getContext(), POSDataAccessSessionOdoo.REST_BASE_URL, POSDataAccessSessionOdoo.REST_USER_NAME, POSDataAccessSessionOdoo.REST_PASSWORD);
            statement = connection.createStatement();
            statement.prepareQuery(POSAPIOdoo.REST_SETTING_ACCOUNT);
            statement.setSessionHeader(POSDataAccessSessionOdoo.REST_SESSION_ID);

            StaffEntity staffEntity = new StaffEntity();
            staffEntity.name = staff.getStaffName();
            staffEntity.old_password = staff.getCurrentPassword();
            staffEntity.new_password = staff.getNewPassword();

            rp = statement.execute(staffEntity);

            String reponse = StringUtil.truncateJson(rp.readResult2String());

            JSONObject jsonObject = new JSONObject(reponse);
            String message = jsonObject.getString("messages");

            staff.setResponeType(true);
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
        return mConfigOdoo.getCountry();
    }

    @Override
    public Customer getGuestCheckout() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getCustomerGuestFake();
    }

    @Override
    public List<Currency> getCurrencies() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public Currency getDefaultCurrency() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getDefaultCurrency();
    }

    @Override
    public ConfigPriceFormat getPriceFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getPriceFormat();
    }

    @Override
    public ConfigQuantityFormat getQuantityFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getQuantityFormat();
    }

    @Override
    public ConfigPriceFormat getBasePriceFomat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getPriceFormat();
    }

    @Override
    public List<String> getStaffPermisson() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getPermissonFake();
    }

    @Override
    public List<StaffPermisson> retrieveStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public Map<String, String> getConfigCCTypes() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getCCTypeFake();
    }

    @Override
    public List<String> getConfigMonths() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getConfigMonthFake();
    }

    @Override
    public Map<String, String> getConfigCCYears() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getConfigYearFake();
    }

    @Override
    public ConfigPrint getConfigPrint() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public ChangeCurrency changeCurrency(String code) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public String getBaseCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getDefaultCurrency().getCode();
    }

    @Override
    public String getCurrentCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return mConfigOdoo.getDefaultCurrency().getCode();
    }

    @Override
    public float getConfigMaximumDiscount() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // default maxmimum discount = 100%
        return 100;
    }

    @Override
    public String googleAPIKey() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public boolean taxCartDisplay() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getConfigDeliveryTime() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getConfigStoreCredit() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getConfigRewardPoint() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getConfigGiftCard() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getConfigSession() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return true;
    }

    @Override
    public boolean getConfigDeleteOrder() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // default open session
        return false;
    }

    @Override
    public boolean getApplyAfterDiscount() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getTaxSaleDisplayPrice() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getTaxSaleDisplayShipping() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public boolean getTaxSaleDisplaySubtotal() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
    }

    @Override
    public void getConfigStaffPermisson(List<String> listPermisson) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (listPermisson.size() > 0) {
            ConfigUtil.setChangeStaff(false);
            ConfigUtil.setManageOrderByLocation(false);
            ConfigUtil.setNeedToShip(false);
            ConfigUtil.setMarkAsShip(false);
            ConfigUtil.setCanUseRefund(false);
            ConfigUtil.setSendEmail(false);
            ConfigUtil.setShip(false);
            ConfigUtil.setCancel(false);
            ConfigUtil.setAddComment(true);
            ConfigUtil.setReOder(true);
            ConfigUtil.setPartialInvoice(false);
            ConfigUtil.setShiftOpenNote(false);
            ConfigUtil.setEnableOpenFloatAmount(false);
            ConfigUtil.setShiftCloseNote(false);
            ConfigUtil.setEnableCloseAmount(false);
            ConfigUtil.setCancelCloseSession(false);
            ConfigUtil.setPrintSession(false);
            ConfigUtil.setShippingAddress(false);
            ConfigUtil.setAddAddress(false);
            ConfigUtil.setLastName(false);
            ConfigUtil.setCompany(false);
            ConfigUtil.setSubscribe(false);
            ConfigUtil.setEditState(false);
            ConfigUtil.setSameAddress(false);
            ConfigUtil.setRequiedFirstName(true);
            ConfigUtil.setRequiedLastName(false);
            ConfigUtil.setRequiedEmail(false);
            ConfigUtil.setRequiedPhone(false);
            ConfigUtil.setRequiedStreet1(false);
            ConfigUtil.setRequiedCity(false);
            ConfigUtil.setRequiedZipCode(false);
            ConfigUtil.setAddAddressDefault(false);
            ConfigUtil.setApplyCoupon(false);
            ConfigUtil.setCustomSales(false);
            ConfigUtil.setShowAvailableQty(false);
            if (checkStaffPermiss(listPermisson, ALL_PERMISSON)) {
                ConfigUtil.setCreateOrder(true);
                ConfigUtil.setManagerAllOrder(true);
//                ConfigUtil.setDiscountPerCart(true);
//                ConfigUtil.setApplyCoupon(true);
                ConfigUtil.setDiscountPerItem(true);
//                ConfigUtil.setCanUseRefund(true);
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
                ConfigUtil.setManageAllDiscount(false);
                if (ConfigUtil.isManageAllDiscount()) {
                    ConfigUtil.setDiscountPerCart(true);
                    ConfigUtil.setApplyCoupon(true);
                    ConfigUtil.setDiscountPerItem(true);
                    ConfigUtil.setApplyCustomPrice(true);
                } else {
//                    ConfigUtil.setDiscountPerCart(checkStaffPermiss(listPermisson, APPLY_DISCOUNT_PER_CART));
//                    ConfigUtil.setApplyCoupon(checkStaffPermiss(listPermisson, APPLY_COUPON));
                    ConfigUtil.setDiscountPerItem(checkStaffPermiss(listPermisson, APPLY_DISCOUNT_PER_ITEM));
                    ConfigUtil.setApplyCustomPrice(checkStaffPermiss(listPermisson, APPLY_CUSTOM_PRICE));
                }
//                ConfigUtil.setManagerShiftAdjustment(checkStaffPermiss(listPermisson, MANAGE_SHIFT_ADJUSTMENT));
            }
        }
    }

    @Override
    public Map<String, String> getConfigStatusOrder() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        Map<String, String> listStatus = new LinkedHashTreeMap<>();
        listStatus.put("canceled", "cancel");
        listStatus.put("pending", "paid");
        listStatus.put("complete", "invoiced");
        listStatus.put("closed", "done");
        return listStatus;
    }

    @Override
    public List<String> getConfigSetting() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        List<String> listSetting = new ArrayList<>();
        listSetting.add("0");
        listSetting.add("2");
        return listSetting;
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

    private Customer getCustomerGuestFake() {
        String customer_id = "";
        String email = "guest@example.com";
        String first_name = "Guest";
        String last_name = "";
        String full_name = first_name + " " + last_name;
        String street = "Street";
        String country_id = "US";
        String city = "Guest City";
        String region_id = "12";
        String zip_code = "90034";
        String telephone = "12345678";

        if (mConfigOdoo.getGuestCustomer() != null) {
            if (!StringUtil.isNullOrEmpty(mConfigOdoo.getGuestCustomer().getID())) {
                customer_id = mConfigOdoo.getGuestCustomer().getID();
            }
            if (!StringUtil.isNullOrEmpty(mConfigOdoo.getGuestCustomer().getFirstName())) {
                first_name = mConfigOdoo.getGuestCustomer().getFirstName();
            }
        }

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

    private List<String> getPermissonFake() {
        List<String> listPermisson = new ArrayList<>();
        listPermisson.add(ALL_PERMISSON);
        listPermisson.add(APPLY_DISCOUNT_PER_ITEM);
        listPermisson.add(APPLY_CUSTOM_PRICE);
        return listPermisson;
    }

    private Map<String, String> getConfigYearFake() {
        Map<String, String> listCCYears = new LinkedTreeMap<>();
        listCCYears.put("2017", "2017");
        listCCYears.put("2018", "2018");
        listCCYears.put("2019", "2019");
        return listCCYears;
    }

    private List<String> getConfigMonthFake() {
        List<String> listMonth = new ArrayList<>();
        listMonth.add("01 - January");
        listMonth.add("02 - February");
        return listMonth;
    }

    private Map<String, String> getCCTypeFake() {
        Map<String, String> listCC = new LinkedTreeMap<>();
        listCC.put("VI", "Visa");
        return listCC;
    }
}
