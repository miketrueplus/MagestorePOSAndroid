package com.magestore.app.lib.resourcemodel.config;

import com.magestore.app.lib.connection.ConnectionException;
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
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.model.staff.StaffPermisson;
import com.magestore.app.lib.parse.ParseException;
import com.magestore.app.lib.resourcemodel.DataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Trả lại các cấu hình config
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigDataAccess extends DataAccess {
    Config retrieveConfig() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean checkLicenseKey() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    List<ConfigTaxClass> retrieveConfigTaxClass() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    ConfigOption retrieveColorSwatch() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    String getConfig(String configPath) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, String> getCustomerGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Staff getStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    void setStaff(Staff staff) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Staff changeInformationStaff(Staff staff) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, ConfigCountry> getCountryGroup() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Customer getGuestCheckout() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    List<Currency> getCurrencies() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Currency getDefaultCurrency() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    ConfigPriceFormat getPriceFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    ConfigQuantityFormat getQuantityFormat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    ConfigPriceFormat getBasePriceFomat() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    List<String> getStaffPermisson() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    List<StaffPermisson> retrieveStaff() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, String> getConfigCCTypes() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    List<String> getConfigMonths() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, String> getConfigCCYears() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    ConfigPrint getConfigPrint() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    ChangeCurrency changeCurrency(String code) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    String getBaseCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    String getCurrentCurrencyCode() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    float getConfigMaximumDiscount() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean getConfigDeliveryTime() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean getConfigStoreCredit() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean getConfigRewardPoint() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean getConfigGiftCard() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean getConfigSession() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    boolean getConfigDeleteOrder() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    void getConfigStaffPermisson(List<String> listPermisson) throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
    Map<String, String> getConfigStatusOrder() throws DataAccessException, ConnectionException, ParseException, IOException, ParseException;
}
