package com.magestore.app.pos.service.config;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.checkout.ShippingMethod;
import com.magestore.app.lib.model.config.Config;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.resourcemodel.config.ConfigDataAccess;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.checkout.PosPaymentMethod;
import com.magestore.app.pos.model.checkout.PosShippingMethod;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
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
        return configDataAccess.retrieveConfig();
    }

    @Override
    public Map<String, String> getCustomerGroup() throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Nếu chưa khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        ConfigDataAccess configDataAccess = factory.generateConfigDataAccess();
        return configDataAccess.getCustomerGroup();
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
