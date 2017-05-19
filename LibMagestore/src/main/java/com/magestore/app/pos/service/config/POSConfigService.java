package com.magestore.app.pos.service.config;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigPrint;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.ChangeCurrency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.checkout.PosPaymentMethod;
import com.magestore.app.pos.model.checkout.PosShippingMethod;
import com.magestore.app.pos.model.setting.PosSetting;
import com.magestore.app.pos.model.staff.PosStaff;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
        Config config = configDataAccess.retrieveConfig();
        List<ConfigTaxClass> configTaxClass = configDataAccess.retrieveConfigTaxClass();

        // đặt config format tiền
        ConfigUtil.setCurrencyFormat(getPriceFormat());
        ConfigUtil.setCurrencyNoSymbolFormat(getPriceNosymbolFormat());
        ConfigUtil.setFloatFormat(getFloatFormat());
        ConfigUtil.setIntegerFormat(getIntegerFormat());
        ConfigUtil.setConfigPrint(getConfigPrint());
        ConfigUtil.setStaff(getStaff());
        ConfigUtil.setCustomerGuest(getGuestCheckout());
        ConfigUtil.setBaseCurrencyCode(getBaseCurrencyCode());
        ConfigUtil.setShowDeliveryTime(getConfigDeliveryTime());
        ConfigUtil.setEnableGiftCard(getConfigGiftCard());
        ConfigUtil.setEnableStoreCredit(getConfigStoreCredit());
        ConfigUtil.setEnableRewardPoint(getConfigRewardPoint());
        ConfigUtil.setCurrentCurrency(getDefaultCurrency());
        ConfigUtil.setConfigTaxClass(configTaxClass);
        // return config
        return config;
    }

    @Override
    public DecimalFormat getPriceFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // khởi tạo config data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();

        // lấy config
        ConfigPriceFormat priceFormat = configDataAccess.getPriceFormat();

        return currencyFormat(priceFormat);
    }

    private DecimalFormat currencyFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo currency format
        String pattern = (priceFormat.getPattern().indexOf(StringUtil.STRING_CURRENCY) == 0) ? "¤¤ ###,##0.0" : "###,##0.0 ¤¤";

        Locale locale = new Locale("en", "UK");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(priceFormat.getDecimalSymbol().charAt(0));
        symbols.setGroupingSeparator(priceFormat.getGroupSymbol().charAt(0));

//        String symbol = priceFormat.getCurrencySymbol();
//        if (symbol.startsWith("u")) symbol = "\\" + symbol;
        symbols.setCurrencySymbol(priceFormat.getCurrencySymbol());
        symbols.setInternationalCurrencySymbol(priceFormat.getCurrencySymbol());
//        symbols.setCurrencySymbol(StringEscapeUtils.unescapeJava(symbol));
//        symbols.setInternationalCurrencySymbol(StringEscapeUtils.unescapeJava(symbol));

        DecimalFormat currencyFormat = new DecimalFormat(pattern, symbols);
        currencyFormat.setGroupingSize(priceFormat.getGroupLength());
        currencyFormat.setMaximumFractionDigits(priceFormat.getPrecision());
        currencyFormat.setMinimumFractionDigits(priceFormat.getRequirePrecision());
        return currencyFormat;
    }

    @Override
    public DecimalFormat getPriceNosymbolFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // khởi tạo config data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();

        // lấy config
        ConfigPriceFormat priceFormat = configDataAccess.getPriceFormat();

        return currencyNosymbolFormat(priceFormat);
    }

    private DecimalFormat currencyNosymbolFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo currency format
        String pattern = "###,###.#";
        Locale locale = new Locale("en", "UK");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(priceFormat.getDecimalSymbol().charAt(0));
        symbols.setGroupingSeparator(priceFormat.getGroupSymbol().charAt(0));
        DecimalFormat currencyFormat = new DecimalFormat(pattern, symbols);
        currencyFormat.setGroupingSize(priceFormat.getGroupLength());
        currencyFormat.setMaximumFractionDigits(priceFormat.getPrecision());
        currencyFormat.setMinimumFractionDigits(priceFormat.getRequirePrecision());
        return currencyFormat;
    }

    @Override
    public DecimalFormat getFloatFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // khởi tạo config data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();

        // lấy config
        ConfigPriceFormat priceFormat = configDataAccess.getPriceFormat();

        return floatFormat(priceFormat);
    }

    private DecimalFormat floatFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo float format
        String pattern = "###,###.#";
        Locale locale = new Locale("en", "UK");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setDecimalSeparator(priceFormat.getDecimalSymbol().charAt(0));
        symbols.setGroupingSeparator(priceFormat.getGroupSymbol().charAt(0));
        DecimalFormat format = new DecimalFormat(pattern, symbols);
        format.setGroupingSize(priceFormat.getGroupLength());
        format.setMaximumFractionDigits(priceFormat.getPrecision());
        format.setMinimumFractionDigits(priceFormat.getRequirePrecision());
        return format;
    }

    @Override
    public DecimalFormat getIntegerFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // khởi tạo config data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();

        // lấy config
        ConfigPriceFormat priceFormat = configDataAccess.getPriceFormat();

        return integetFormat(priceFormat);
    }

    private DecimalFormat integetFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo interger format
        String pattern = "###,###";
        Locale locale = new Locale("en", "UK");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(priceFormat.getGroupSymbol().charAt(0));
        DecimalFormat format = new DecimalFormat(pattern, symbols);
        format.setGroupingSize(priceFormat.getGroupLength());
        return format;
    }

    @Override
    public Map<String, String> getCustomerGroup() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCustomerGroup();
    }

    @Override
    public Staff getStaff() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getStaff();
    }

    @Override
    public void setStaff(Staff staff) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        configDataAccess.setStaff(staff);
    }

    @Override
    public Staff changeInformationStaff(Staff staff) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.changeInformationStaff(staff);
    }

    @Override
    public Staff createStaff() {
        return new PosStaff();
    }

    @Override
    public Map<String, ConfigCountry> getCountry() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCountryGroup();
    }

    @Override
    public Customer getGuestCheckout() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getGuestCheckout();
    }

    @Override
    public List<Currency> getCurrencies() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCurrencies();
    }

    @Override
    public Currency getDefaultCurrency() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getDefaultCurrency();
    }

    @Override
    public Map<String, String> getConfigCCTypes() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigCCTypes();
    }

    @Override
    public List<String> getConfigMonths() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigMonths();
    }

    @Override
    public Map<String, String> getConfigCCYears() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigCCYears();
    }

    @Override
    public ConfigPrint getConfigPrint() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigPrint();
    }

    @Override
    public ChangeCurrency changeCurrency(String code) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        ChangeCurrency changeCurrency = configDataAccess.changeCurrency(code);
        ConfigPriceFormat configPriceFormat = changeCurrency.getPriceFormat();
        Currency currency = changeCurrency.getCurrency();
        configPriceFormat.setCurrencySymbol(currency.getCurrencySymbol());

        ConfigUtil.setCurrencyFormat(currencyFormat(configPriceFormat));
        ConfigUtil.setCurrencyNoSymbolFormat(currencyNosymbolFormat(configPriceFormat));
        ConfigUtil.setFloatFormat(floatFormat(configPriceFormat));
        ConfigUtil.setIntegerFormat(integetFormat(configPriceFormat));
        ConfigUtil.setCurrentCurrency(getDefaultCurrency());

        return changeCurrency;
    }

    @Override
    public String getBaseCurrencyCode() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getBaseCurrencyCode();
    }

    @Override
    public float getConfigMaximumDiscount() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigMaximumDiscount();
    }

    @Override
    public boolean getConfigDeliveryTime() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigDeliveryTime();
    }

    @Override
    public boolean getConfigStoreCredit() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigStoreCredit();
    }

    @Override
    public boolean getConfigRewardPoint() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigRewardPoint();
    }

    @Override
    public boolean getConfigGiftCard() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigGiftCard();
    }

    public static String SETTING_ACCOUNT = "My Account";
    public static String SETTING_PRINT = "Print";
    public static String SETTING_CURRENCY = "Currency";
    public static String SETTING_STORE = "Store";

    @Override
    public List<Setting> getListSetting() {
        List<Setting> settingList = new ArrayList<>();

        Setting accountSetting = new PosSetting();
        accountSetting.setName(SETTING_ACCOUNT);
        accountSetting.setType(0);
        settingList.add(accountSetting);

        Setting printSetting = new PosSetting();
        printSetting.setName(SETTING_PRINT);
        printSetting.setType(1);
        settingList.add(printSetting);

        Setting currencySetting = new PosSetting();
        currencySetting.setName(SETTING_CURRENCY);
        currencySetting.setType(2);
        settingList.add(currencySetting);

        Setting storeSetting = new PosSetting();
        storeSetting.setName(SETTING_STORE);
        storeSetting.setType(3);
        settingList.add(storeSetting);

        return settingList;
    }

    public static String PAYMENT_METHOD_CC_DIRECT_POST = "CC_DIRECT";
    public static String PAYMENT_METHOD_CASH_IN = "CASH_IN";
    public static String PAYMENT_METHOD_CC_CARD = "CC_CARDN";
    public static String PAYMENT_METHOD_CASH_COD = "CASH_ON_DELIVERY";
    public static String PAYMENT_METHOD_CUSTOM_PAYMENT1 = "CUSTOM_PAYMENT1";
    public static String PAYMENT_METHOD_CUSTOM_PAYMENT2 = "CUSTOM_PAYMENT2";

    @Override
    public List<PaymentMethod> getPaymentMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        List<PaymentMethod> paymentMethodList = new ArrayList<PaymentMethod>();

        PaymentMethod method = new PosPaymentMethod();
        method.setName("Credit Card Direct Post (Authorize.net)");
        method.setCreditCardDirect();
        paymentMethodList.add(method);

        method = new PosPaymentMethod();
        method.setName("Direct cash in");
        method.setCashIn();
        paymentMethodList.add(method);

        method = new PosPaymentMethod();
        method.setName("Credit direct");
        method.setCreditCardIn();
        paymentMethodList.add(method);

        method = new PosPaymentMethod();
        method.setName("Cash on delivery");
        method.setCashOnDelivery();
        paymentMethodList.add(method);

        method = new PosPaymentMethod();
        method.setName("Custom payment 1");
        method.setCustomerPayment();
        paymentMethodList.add(method);

        method = new PosPaymentMethod();
        method.setName("Custom payment 2");
        method.setCustomerPayment();
        paymentMethodList.add(method);

        return paymentMethodList;
    }

    public static String SHIPPING_METHOD_FLAT_RATE = "SHIPPING_METHOD_FLAT_RATE";
    public static String PAYMENT_METHOD_FREE_SHIPPING = "PAYMENT_METHOD_FREE_SHIPPING";
    public static String PAYMENT_METHOD_STORE_PICKUP = "PAYMENT_METHOD_STORE_PICKUP";

    @Override
    public List<ShippingMethod> getShippingMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        List<ShippingMethod> shippingMethodList = new ArrayList<ShippingMethod>();

        ShippingMethod method = new PosShippingMethod();
        method.setName("Flat Rate - Fixed");
        method.setFixedRate(10);
        shippingMethodList.add(method);

        method = new PosShippingMethod();
        method.setName("Free CheckoutShipping - Free");
        method.setFreeRate();
        shippingMethodList.add(method);

        method = new PosShippingMethod();
        method.setName("POS CheckoutShipping - Store Pickup");
        method.setFreeRate();
        shippingMethodList.add(method);

        return shippingMethodList;
    }

    @Override
    public List<CheckoutPayment> getCheckoutPaymentList() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        List<CheckoutPayment> checkoutPaymentList = new ArrayList<CheckoutPayment>();
        CheckoutPayment payment = new PosCheckoutPayment();
        payment.setTitle("Cash In");
        payment.setBaseAmount(10.0f);
        checkoutPaymentList.add(payment);
        return checkoutPaymentList;

    }
}
