package com.magestore.app.pos.api.odoo.config;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;
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
import com.magestore.app.lib.model.config.ConfigQuantityFormat;
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
import com.magestore.app.pos.api.odoo.POSAPIOdoo;
import com.magestore.app.pos.api.odoo.POSAbstractDataAccessOdoo;
import com.magestore.app.pos.api.odoo.POSDataAccessSessionOdoo;
import com.magestore.app.pos.model.config.PosConfig;
import com.magestore.app.pos.model.config.PosConfigPriceFormat;
import com.magestore.app.pos.model.config.PosConfigQuantityFormat;
import com.magestore.app.pos.model.customer.PosCustomer;
import com.magestore.app.pos.model.customer.PosCustomerAddress;
import com.magestore.app.pos.model.directory.PosCurrency;
import com.magestore.app.pos.model.directory.PosRegion;
import com.magestore.app.pos.model.staff.PosLocation;
import com.magestore.app.pos.model.staff.PosStaff;
import com.magestore.app.pos.parse.gson2pos.Gson2PosConfigParseImplement;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 8/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSConfigDataAccessOdoo extends POSAbstractDataAccessOdoo implements ConfigDataAccess {
    private static Config mConfig;
    private static Staff mStaff;
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
            String json = StringUtil.truncateJson(rp.readResult2String());
            Gson2PosConfigParseImplement implement = new Gson2PosConfigParseImplement();
            Gson gson = implement.createGson();
            mConfig = gson.fromJson(json, PosConfig.class);
            return mConfig;
        } catch (ConnectionException ex) {
//            statement.getCacheConnection().deleteCache();
//            throw ex;
        } catch (IOException ex) {
//            statement.getCacheConnection().deleteCache();
//            throw ex;
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
        return null;
    }

    @Override
    public boolean checkLicenseKey() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return false;
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
        return null;
    }

    @Override
    public Staff getStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        mStaff = getStaffFake();
        return mStaff;
    }

    @Override
    public void setStaff(Staff staff) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        mStaff = staff;
    }

    @Override
    public Staff changeInformationStaff(Staff staff) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public Map<String, ConfigCountry> getCountryGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
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
        return getDefaultCurrencyFake();
    }

    @Override
    public ConfigPriceFormat getPriceFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getPriceFormatFake();
    }

    @Override
    public ConfigQuantityFormat getQuantityFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getQuantityFormatFake();
    }

    @Override
    public ConfigPriceFormat getBasePriceFomat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return getPriceFormatFake();
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
        return null;
    }

    @Override
    public String getCurrentCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        return null;
    }

    @Override
    public float getConfigMaximumDiscount() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        // default maxmimum discount = 100%
        return 100;
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
    public void getConfigStaffPermisson(List<String> listPermisson) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException {
        if (listPermisson.size() > 0) {
            ConfigUtil.setManageOrderByLocation(false);
            ConfigUtil.setManagerShiftAdjustment(true);
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
            if (checkStaffPermiss(listPermisson, ALL_PERMISSON)) {
                ConfigUtil.setCreateOrder(true);
                ConfigUtil.setManagerAllOrder(true);
                ConfigUtil.setDiscountPerCart(true);
                ConfigUtil.setApplyCoupon(true);
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
//                ConfigUtil.setManagerShiftAdjustment(checkStaffPermiss(listPermisson, MANAGE_SHIFT_ADJUSTMENT));
            }
        }
    }

    @Override
    public Map<String, String> getConfigStatusOrder() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException{
        Map<String, String> listStatus = new LinkedHashTreeMap<>();
        listStatus.put("canceled", "cancel");
        listStatus.put("pending", "paid");
        listStatus.put("complete", "invoiced");
        listStatus.put("closed", "done");
        return listStatus;
    }

    private Staff getStaffFake(){
        Staff staff = new PosStaff();
        staff.setStaffId("4");
        staff.setStaffName("jessie");
        Location location = new PosLocation();
        location.setLocationId("1");
        location.setLocationName("Default Location");
        location.setLocationAddress("Default Location Address");
        staff.setStaffLocation(location);
        return staff;
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

    // TODO giả data config
    private ConfigPriceFormat getPriceFormatFake() {
        String currencySymbol = "$";
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
        String pattern = "$%s";
        int precision = 2;
        int requiredPrecision = 2;
        String decimalSymbol = ".";
        String groupSymbol = ",";
        int groupLength = 3;
        int integerRequired = 1;

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

    private ConfigQuantityFormat getQuantityFormatFake() {
        String currencySymbol = "$";
        String pattern = "$%s";
        int precision = 2;
        int requiredPrecision = 2;
        String decimalSymbol = ".";
        String groupSymbol = ",";
        int groupLength = 3;
        int integerRequired = 1;

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

    private Currency getDefaultCurrencyFake() {
        Currency currency = new PosCurrency();
        String code = "USD";
        String currency_name = "USD";
        String currency_symbol = "$";
        String is_default = "1";
        String currency_rate = "1";
        currency.setCode(code);
        currency.setCurrenyName(currency_name);
        currency.setCurrencySymbol(currency_symbol);
        currency.setIsDefault(is_default);
        try {
            currency.setCurrencyRate(Double.parseDouble(currency_rate));
        } catch (Exception e) {
        }
        return currency;
    }

    private Customer getCustomerGuestFake() {
        String customer_id = "47";
        String email = "guest@example.com";
        String first_name = "Guest";
        String last_name = "POS";
        String full_name = first_name + " " + last_name;
        String street = "Street";
        String country_id = "US";
        String city = "Guest City";
        String region_id = "12";
        String zip_code = "90034";
        String telephone = "12345678";

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

    private List<String> getPermissonFake(){
        List<String> listPermisson = new ArrayList<>();
        listPermisson.add(ALL_PERMISSON);
        return listPermisson;
    }

    private Map<String, String> getConfigYearFake(){
        Map<String, String> listCCYears = new LinkedTreeMap<>();
        listCCYears.put("2017", "2017");
        listCCYears.put("2018", "2018");
        listCCYears.put("2019", "2019");
        return listCCYears;
    }

    private List<String> getConfigMonthFake(){
        List<String> listMonth = new ArrayList<>();
        listMonth.add("01 - January");
        listMonth.add("02 - February");
        return listMonth;
    }

    private Map<String, String> getCCTypeFake(){
        Map<String, String> listCC = new LinkedTreeMap<>();
        listCC.put("VI", "Visa");
        return listCC;
    }
}
