package com.magestore.app.lib.service.config;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.config.ConfigCustomerGroup;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.model.staff.Staff;
import com.magestore.app.lib.service.Service;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigService extends Service {
    /**
     * Kết nối server lấy tham số config
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Config retrieveConfig() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    DecimalFormat getPriceFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    DecimalFormat getPriceNosymbolFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    DecimalFormat getFloatFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    DecimalFormat getIntegerFormat() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Trả về CustomerGroup
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Map<String, String> getCustomerGroup() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Trả về Country
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Staff getStaff() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    void setStaff(Staff staff) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Staff changeInformationStaff(Staff staff) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Staff createStaff();

    Map<String, ConfigCountry> getCountry() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<PaymentMethod> getPaymentMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<ShippingMethod> getShippingMethodList() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<CheckoutPayment> getCheckoutPaymentList() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Customer getGuestCheckout() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<Currency> getCurrencies() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Currency getDefaultCurrency() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Map<String, String> getConfigCCTypes() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<String> getConfigMonths() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Map<String, String> getConfigCCYears() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    float getConfigMaximumDiscount() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    List<Setting> getListSetting();
}