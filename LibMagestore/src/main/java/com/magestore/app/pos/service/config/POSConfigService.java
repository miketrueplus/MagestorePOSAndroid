package com.magestore.app.pos.service.config;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.directory.Currency;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
        Config config = configDataAccess.retrieveConfig();

        // đặt config format tiền
        ConfigUtil.setCurrencyFormat(getPriceFormat());
        ConfigUtil.setCurrencyNoSymbolFormat(getPriceNosymbolFormat());
        ConfigUtil.setFloatFormat(getFloatFormat());
        ConfigUtil.setIntegerFormat(getIntegerFormat());

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

        // khởi tạo currency format
        String pattern = (priceFormat.getPattern().indexOf(StringUtil.STRING_CURRENCY) == 0) ? "¤¤ ###,##0.0" : "###,##0.0 ¤¤";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(priceFormat.getDecimalSymbol().charAt(0));
        symbols.setGroupingSeparator(priceFormat.getGroupSymbol().charAt(0));
        symbols.setCurrencySymbol(priceFormat.getCurrencySymbol());
        symbols.setInternationalCurrencySymbol(priceFormat.getCurrencySymbol());
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

        // khởi tạo currency format
        String pattern = "###,###.#";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
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

        // khởi tạo float format
        String pattern = "###,###.#";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
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

        // khởi tạo interger format
        String pattern = "###,###";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
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
    public float getConfigMaximumDiscount() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigMaximumDiscount();
    }

    public static String SETTING_ACCOUNT = "My Account";
    public static String SETTING_CURRENCY = "Currency";
    public static String SETTING_STORE = "Store";

    @Override
    public List<Setting> getListSetting() {
        List<Setting> settingList = new ArrayList<>();

        Setting accountSetting = new PosSetting();
        accountSetting.setName(SETTING_ACCOUNT);
        accountSetting.setType(0);
        settingList.add(accountSetting);

        Setting currencySetting = new PosSetting();
        currencySetting.setName(SETTING_CURRENCY);
        currencySetting.setType(1);
        settingList.add(currencySetting);

        Setting storeSetting = new PosSetting();
        storeSetting.setName(SETTING_STORE);
        storeSetting.setType(2);
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
