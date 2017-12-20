package com.magestore.app.pos.service.config;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigOption;
import com.magestore.app.lib.model.config.ConfigPriceFormat;
import com.magestore.app.lib.model.config.ConfigPrint;
import com.magestore.app.lib.model.config.ConfigQuantityFormat;
import com.magestore.app.lib.model.config.ConfigTaxClass;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.ChangeCurrency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.model.staff.StaffPermisson;
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
        ConfigOption configOption = configDataAccess.retrieveColorSwatch();
        boolean checkLicenseKey = configDataAccess.checkLicenseKey();

        // đặt config format tiền
        ConfigUtil.setGoogleKey(configDataAccess.googleAPIKey());
        ConfigUtil.setTaxCartDisplay(getTaxCartDisplayPrice());
        ConfigUtil.setApplyAfterDiscount(getApplyAfterDiscount());
        ConfigUtil.setTaxSalesDisplayPrice(getTaxSaleDisplayPrice());
        ConfigUtil.setTaxSalesDisplayShipping(getTaxSaleDisplayShipping());
        ConfigUtil.setTaxSalesDisplaySubtotal(getTaxSaleDisplaySubtotal());
        ConfigUtil.setCalculateApplyTaxOnOriginal(getCalculateApplyTaxOnOriginal());
        ConfigUtil.setCurrencyFormat(getPriceFormat());
        ConfigUtil.setCurrencyNoSymbolFormat(getPriceNosymbolFormat());
        ConfigUtil.setFloatFormat(getFloatFormat());
        ConfigUtil.setIntegerFormat(getIntegerFormat());
        ConfigUtil.setQuantityFormat(getQuantityFormat());
        ConfigUtil.setConfigPrint(getConfigPrint());
        ConfigUtil.setStaff(getStaff());
        ConfigUtil.setCustomerGuest(getGuestCheckout());
        ConfigUtil.setBaseCurrencyCode(getBaseCurrencyCode());
        ConfigUtil.setShowDeliveryTime(getConfigDeliveryTime());
        if (ConfigUtil.getPlatForm().equals(ConfigUtil.PLATFORM_MAGENTO_2)) {
            ConfigUtil.setEnableGiftCard(getConfigGiftCard());
            ConfigUtil.setEnableStoreCredit(getConfigStoreCredit());
            ConfigUtil.setEnableRewardPoint(getConfigRewardPoint());
        }
        ConfigUtil.setListCountry(getCountry());
        ConfigUtil.setListCustomerGroup(getCustomerGroup());
        ConfigUtil.setListOrderStatus(getListOrderStatus());
        ConfigUtil.setEnableSession(getConfigSession());
        ConfigUtil.setEnableDeleteOrder(getConfigDeleteOrder());
        if (!StringUtil.isNullOrEmpty(getCurrentCurrencyCode())) {
            List<Currency> listCurrency = getCurrencies();
            boolean checkCurrentCurrency = false;
            if (listCurrency != null && listCurrency.size() > 0) {
                for (Currency currency : listCurrency) {
                    if (currency.getCode().equals(getCurrentCurrencyCode())) {
                        checkCurrentCurrency = true;
                        ConfigUtil.setCurrentCurrency(currency);
                    }
                }
                if (!checkCurrentCurrency) {
                    ConfigUtil.setCurrentCurrency(getDefaultCurrency());
                }
            } else {
                ConfigUtil.setCurrentCurrency(getDefaultCurrency());
            }
        } else {
            ConfigUtil.setCurrentCurrency(getDefaultCurrency());
        }

        ConfigUtil.setConfigTaxClass(configTaxClass);
        ConfigUtil.setConfigPriceFormat(configDataAccess.getPriceFormat());
        if (configOption != null) {
            ConfigUtil.setColorSwatch(configOption.getItems());
        }
        ConfigUtil.setCheckActiveKey(checkLicenseKey);

        // permisson
        getConfigStaffPermisson(getStaffPermisson());
        // get config tax
        getConfigTax();
        //  get product attribute
        ConfigUtil.setProductAttribute(getProductAttribute());

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

    @Override
    public DecimalFormat currencyFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo currency format
        String pattern = "###,###.";
        for (int i = 0; i < priceFormat.getPrecision(); i++) {
            pattern += "#";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator('.');
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

    @Override
    public DecimalFormat currencyNosymbolFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo currency format
        String pattern = "###,###.";
        for (int i = 0; i < priceFormat.getPrecision(); i++) {
            pattern += "#";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
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

    @Override
    public DecimalFormat floatFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo float format
        String pattern = "###,###.";
        for (int i = 0; i < priceFormat.getPrecision(); i++) {
            pattern += "#";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
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

    @Override
    public DecimalFormat getQuantityFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // khởi tạo config data access
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();

        // lấy config
        ConfigQuantityFormat quantityFormat = configDataAccess.getQuantityFormat();
        return quantityFormat(quantityFormat);
    }

    @Override
    public DecimalFormat integetFormat(ConfigPriceFormat priceFormat) {
        // khởi tạo interger format
        String pattern = "###,###";
        Locale locale = new Locale("vi", "VN");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator(priceFormat.getGroupSymbol().charAt(0));
        DecimalFormat format = new DecimalFormat(pattern, symbols);
        format.setGroupingSize(priceFormat.getGroupLength());
        return format;
    }

    private DecimalFormat quantityFormat(ConfigQuantityFormat quantityFormat) {
        // khởi tạo interger format
        // khởi tạo float format
        String pattern = "###,###.";
        for (int i = 0; i < quantityFormat.getPrecision(); i++) {
            pattern += "#";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(quantityFormat.getDecimalSymbol().charAt(0));
        symbols.setGroupingSeparator(quantityFormat.getGroupSymbol().charAt(0));
        DecimalFormat format = new DecimalFormat(pattern, symbols);
        format.setGroupingSize(quantityFormat.getGroupLength());
        format.setMaximumFractionDigits(quantityFormat.getPrecision());
        format.setMinimumFractionDigits(0);
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
    public List<String> getStaffPermisson() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getStaffPermisson();
    }

    @Override
    public List<StaffPermisson> retrieveStaff() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.retrieveStaff();
    }

    @Override
    public void getConfigStaffPermisson(List<String> listPermisson) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        configDataAccess.getConfigStaffPermisson(listPermisson);
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
    public List<String> getProductAttribute() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getProductAttribute();
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
        ConfigUtil.setConfigPriceFormat(configPriceFormat);
        Currency currency = changeCurrency.getCurrency();
        configPriceFormat.setCurrencySymbol(currency.getCurrencySymbol());

        ConfigUtil.setCurrencyFormat(currencyFormat(configPriceFormat));
        ConfigUtil.setCurrencyNoSymbolFormat(currencyNosymbolFormat(configPriceFormat));
        ConfigUtil.setFloatFormat(floatFormat(configPriceFormat));
        ConfigUtil.setIntegerFormat(integetFormat(configPriceFormat));
        ConfigUtil.setCurrentCurrency(currency);

        return changeCurrency;
    }

    @Override
    public String getBaseCurrencyCode() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getBaseCurrencyCode();
    }

    @Override
    public String getCurrentCurrencyCode() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCurrentCurrencyCode();
    }

    @Override
    public float getConfigMaximumDiscount() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigMaximumDiscount();
    }

    @Override
    public boolean getTaxCartDisplayPrice() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getTaxCartDisplayPrice();
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

    @Override
    public boolean getConfigSession() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigSession();
    }

    @Override
    public boolean getConfigDeleteOrder() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigDeleteOrder();
    }

    @Override
    public boolean getApplyAfterDiscount() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getApplyAfterDiscount();
    }

    @Override
    public boolean getTaxSaleDisplayPrice() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getTaxSaleDisplayPrice();
    }

    @Override
    public boolean getTaxSaleDisplayShipping() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getTaxSaleDisplayShipping();
    }

    @Override
    public boolean getTaxSaleDisplaySubtotal() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getTaxSaleDisplaySubtotal();
    }

    @Override
    public boolean getCalculateApplyTaxOnOriginal() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCalculateApplyTaxOnOriginal();
    }

    @Override
    public void getConfigTax() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        configDataAccess.getConfigTax();
    }

    @Override
    public List<String> getConfigSetting() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigSetting();
    }

    @Override
    public List<Setting> getListSetting(Map<String, String> listTitle) {
        List<Setting> settingList = new ArrayList<>();
        List<String> listConfig = null;
        try {
            listConfig = getConfigSetting();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (String key : listConfig) {
            if (key.equals("0")) {
                Setting accountSetting = new PosSetting();
                accountSetting.setName(listTitle.get(key));
                accountSetting.setType(0);
                settingList.add(accountSetting);
            } else if (key.equals("1")) {
                Setting checkoutSetting = new PosSetting();
                checkoutSetting.setName(listTitle.get(key));
                checkoutSetting.setType(1);
                settingList.add(checkoutSetting);
            } else if (key.equals("2")) {
                Setting printSetting = new PosSetting();
                printSetting.setName(listTitle.get(key));
                printSetting.setType(2);
                settingList.add(printSetting);
            } else if (key.equals("3")) {
                Setting currencySetting = new PosSetting();
                currencySetting.setName(listTitle.get(key));
                currencySetting.setType(3);
                settingList.add(currencySetting);
            } else if (key.equals("4")) {
                Setting storeSetting = new PosSetting();
                storeSetting.setName(listTitle.get(key));
                storeSetting.setType(4);
                settingList.add(storeSetting);
            }
        }

        return settingList;
    }

    public Map<String, String> getListOrderStatus() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getConfigStatusOrder();
    }
}
