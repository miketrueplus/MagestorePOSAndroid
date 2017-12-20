package com.magestore.app.pos.service.checkout;

import android.text.TextUtils;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.PlaceOrderIntegrationExtension;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutTotals;
import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.lib.model.checkout.PlaceOrderExtensionParam;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationOrderData;
import com.magestore.app.lib.model.checkout.PlaceOrderIntegrationParam;
import com.magestore.app.lib.model.checkout.PlaceOrderParams;
import com.magestore.app.lib.model.checkout.Quote;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.QuoteCustomer;
import com.magestore.app.lib.model.checkout.QuoteCustomerAddress;
import com.magestore.app.lib.model.checkout.QuoteItemExtension;
import com.magestore.app.lib.model.checkout.QuoteItems;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.model.checkout.payment.CreditCard;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.plugins.GiftCard;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.checkout.PosCheckoutShipping;
import com.magestore.app.pos.model.checkout.PosPaymentMethodDataParam;
import com.magestore.app.pos.model.checkout.PosPlaceOrderExtensionParam;
import com.magestore.app.pos.model.checkout.PosPlaceOrderIntegrationExtension;
import com.magestore.app.pos.model.checkout.PosPlaceOrderIntegrationOrderData;
import com.magestore.app.pos.model.checkout.PosPlaceOrderIntegrationParam;
import com.magestore.app.pos.model.checkout.PosPlaceOrderParams;
import com.magestore.app.pos.model.checkout.PosQuote;
import com.magestore.app.pos.model.checkout.PosQuoteAddCouponParam;
import com.magestore.app.pos.model.checkout.PosQuoteCustomer;
import com.magestore.app.pos.model.checkout.PosQuoteCustomerAddress;
import com.magestore.app.pos.model.checkout.PosQuoteItemExtension;
import com.magestore.app.pos.model.checkout.PosQuoteItems;
import com.magestore.app.pos.model.checkout.PosSaveQuoteParam;
import com.magestore.app.pos.model.checkout.payment.PosCreditCard;
import com.magestore.app.pos.model.plugins.PosStoreCredit;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class POSCheckoutService extends AbstractService implements CheckoutService {
    @Override
    public boolean insert(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.insert(checkouts);
    }

    @Override
    public Checkout saveCart(Checkout checkout, String quoteId) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        QuoteCustomer quoteCustomer = createQuoteCustomer();
        Quote quote = createQuote();
        if (!TextUtils.isEmpty(quoteId)) {
            quote.setQuoteId(quoteId);
        } else {
            quote.setQuoteId("");
        }
        if (checkout.getCustomer() != null) {
            if (StringUtil.isNullOrEmpty(checkout.getCustomerID())) {
                if (!checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
                    quote.setCustomerId(checkout.getCustomerID());
                } else {
                    quote.setCustomerId("");
                }
            } else {
                quote.setCustomerId(checkout.getCustomerID());
            }
        } else {
            quote.setCustomerId("");
        }
        quote.setCurrencyId(ConfigUtil.getCurrentCurrency().getCode());
        quote.setStoreId(checkout.getStoreId());
        quote.setItems(addItemToQuote(checkout));

        addCustomerAddressToQuote(checkout, quoteCustomer);
        quote.setCustomer(quoteCustomer);
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        return checkoutDataAccess.saveCart(checkout, quote);
    }

    @Override
    public Checkout saveQuote(Checkout checkout, SaveQuoteParam quoteParam) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        quoteParam.setQuoteId(checkout.getQuoteId());
        quoteParam.setStoreId(checkout.getStoreId());
        if (checkout.getCustomer() != null) {
            if (StringUtil.isNullOrEmpty(checkout.getCustomerID())) {
                if (!checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
                    quoteParam.setCustomerId(checkout.getCustomerID());
                } else {
                    quoteParam.setCustomerId("");
                }
            } else {
                quoteParam.setCustomerId(checkout.getCustomerID());
            }
        } else {
            quoteParam.setCustomerId("");
        }
        quoteParam.setCurrencyId(ConfigUtil.getCurrentCurrency().getCode());

        return checkoutDataAccess.saveQuote(checkout, quoteParam);
    }

    @Override
    public Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        quoteAddCouponParam.setQuoteId(checkout.getQuoteId());

        if (checkout.getCustomer() != null) {
            if (StringUtil.isNullOrEmpty(checkout.getCustomerID())) {
                if (!checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
                    quoteAddCouponParam.setCustomerId(checkout.getCustomerID());
                } else {
                    quoteAddCouponParam.setCustomerId("");
                }
            } else {
                quoteAddCouponParam.setCustomerId(checkout.getCustomerID());
            }
        } else {
            quoteAddCouponParam.setCustomerId("");
        }

        return checkoutDataAccess.addCouponToQuote(checkout, quoteAddCouponParam);
    }

    @Override
    public Checkout saveShipping(Checkout checkout, String quoteId, String shippingCode) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        // TODO: với trường hợp shipping ko có default thì truyền lên webpos_shipping_storepickup
        if (shippingCode.equals("")) {
            shippingCode = "webpos_shipping_storepickup";
        }
        return checkoutDataAccess.saveShipping(checkout, quoteId, shippingCode);
    }

    @Override
    public Checkout savePayment(String quoteId, String paymentCode) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.savePayment(quoteId, paymentCode);
    }

    private static String KEY_EXTENSION_GRAND_TOTAL = "grand_total";
    private static String KEY_EXTENSION_BASE_GRAND_TOTAL = "base_grand_total";
    private static String KEY_EXTENSION_TAX_AMOUNT = "tax_amount";
    private static String KEY_EXTENSION_BASE_TAX_AMOUNT = "base_tax_amount";
    private static String KEY_EXTENSION_SHIPPING_AMOUNT = "shipping_amount";
    private static String KEY_EXTENSION_BASE_SHIPPING_AMOUNT = "base_shipping_amount";
    private static String KEY_EXTENSION_WEBPOS_STAFF_ID = "webpos_staff_id";
    private static String KEY_EXTENSION_WEBPOS_STAFF_NAME = "webpos_staff_name";
    private static String KEY_EXTENSION_CREATE_AT = "created_at";
    private static String KEY_EXTENSION_CUSTOMER_FULLNAME = "customer_fullname";
    private static String KEY_EXTENSION_WEBPOS_CHANGE = "webpos_change";
    private static String KEY_EXTENSION_WEBPOS_BASE_CHANGE = "webpos_base_change";
    private static String KEY_EXTENSION_WEBPOS_SHIFT_ID = "webpos_shift_id";
    private static String KEY_EXTENSION_LOCATION_ID = "location_id";
    private static String STORE_CREDIT_PAYMENT_CODE = "storecredit";
    private static String STORE_CREDIT_MODULE = "customer_credit";
    private static String STORE_CREDIT_EVENT = "webpos_use_customer_credit_after";
    private static String STORE_CREDIT_BASE_DISCOUNT = "base_customercredit_discount";
    private static String STORE_CREDIT_DISCOUNT = "customercredit_discount";
    private static String GIFT_CARD_MODULE = "os_gift_card";
    private static String GIFT_CARD_EVENT = "webpos_create_order_with_giftcard_after";
    private static String GIFT_CARD_BASE_DISCOUNT = "base_gift_voucher_discount";
    private static String GIFT_CARD_DISCOUNT = "gift_voucher_discount";
    private static String REWARD_POINT_MODULE = "os_reward_points";
    private static String REWARD_POINT_EVENT = "webpos_create_order_with_points_after";
    private static String REWARD_POINT_SPENT = "rewardpoints_spent";
    private static String REWARD_POINT_BASE_DISCOUNT = "rewardpoints_base_discount";
    private static String REWARD_POINT_DISCOUNT = "rewardpoints_discount";
    private static String REWARD_POINT_BASE_AMOUNT = "rewardpoints_amount";
    private static String REWARD_POINT_AMOUNT = "rewardpoints_base_amount";
    private static final String PAYMENT_STRIPE_CODE = "stripe_integration";
    private static final String PAYMENT_AUTHORIZE = "authorizenet_integration";

    @Override
    public Model placeOrder(String quoteId, Checkout checkout, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        PlaceOrderParams placeOrderParams = createPlaceOrderParams();
        if (checkout.getCustomer() != null) {
            if (StringUtil.isNullOrEmpty(checkout.getCustomerID())) {
                if (!checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
                    placeOrderParams.setCustomerId(checkout.getCustomerID());
                } else {
                    placeOrderParams.setCustomerId(checkout.getCustomerID());
                }
            } else {
                placeOrderParams.setCustomerId(checkout.getCustomerID());
            }
        } else {
            placeOrderParams.setCustomerId("");
        }
        placeOrderParams.setQuoteId(quoteId);
        PosPlaceOrderParams.PlaceOrderActionParam placeOrderActionParam = placeOrderParams.createPlaceOrderActionParam();
        placeOrderParams.setCreateInvoice(checkout.getCreateInvoice());
        placeOrderParams.setCreateShipment(checkout.getCreateShip());
        placeOrderParams.setActions(placeOrderActionParam);
        PosPlaceOrderParams.PlaceOrderQuoteDataParam placeOrderQuoteDataParam = placeOrderParams.createPlaceOrderQuoteDataParam();
        placeOrderParams.setCustomerNote(checkout.getNote());
        if (!StringUtil.isNullOrEmpty(checkout.getDeliveryDate())) {
            placeOrderParams.setDeliveryTime(checkout.getDeliveryDate());
        }
        placeOrderParams.setQuoteData(placeOrderQuoteDataParam);

        PosPlaceOrderParams.PlaceOrderPaymentParam placeOrderPaymentParam = placeOrderParams.createPlaceOrderPaymentParam();
        if (listCheckoutPayment.size() > 1) {
            // TODO: trường hợp 2 payment trở lên thì truyền tham số "multipaymentforpos"
            placeOrderParams.setMethod("multipaymentforpos");
        } else {
            if (listCheckoutPayment != null && listCheckoutPayment.size() == 1) {
                String paymentCode = listCheckoutPayment.get(0).getCode();
                String paymentType = listCheckoutPayment.get(0).getType();
                if (paymentType.equals("2") || paymentCode.equals(PAYMENT_STRIPE_CODE) || paymentCode.equals(PAYMENT_AUTHORIZE)) {
                    placeOrderParams.setMethod("multipaymentforpos");
                } else {
                    placeOrderParams.setMethod(paymentCode);
                }
            } else {
                placeOrderParams.setMethod("multipaymentforpos");
            }
        }

        List<PlaceOrderIntegrationParam> listIntegration = new ArrayList<>();
        List<PaymentMethodDataParam> listPaymentMethodParam = placeOrderParams.createPaymentMethodData();

        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            for (CheckoutPayment checkoutPayment : listCheckoutPayment) {
                if (checkoutPayment.getAdditionalData() == null) {
                    PosCheckoutPayment.AdditionalData paymentAdditionParam = checkoutPayment.createAdditionalData();
                    checkoutPayment.setAdditionalData(paymentAdditionParam);
                }
                PaymentMethodDataParam paymentMethodDataParam = createPaymentMethodParam();
                PosPaymentMethodDataParam.PaymentMethodAdditionalParam additionalParam = paymentMethodDataParam.createAddition();
                paymentMethodDataParam.setPaymentMethodAdditionalParam(additionalParam);
                paymentMethodDataParam.setShiftId(ConfigUtil.getShiftId());
                paymentMethodDataParam.setReferenceNumber(checkoutPayment.getReferenceNumber());
                paymentMethodDataParam.setAmount(checkoutPayment.getAmount());
                paymentMethodDataParam.setBaseAmount(ConfigUtil.convertToBasePrice(checkoutPayment.getBaseAmount()));
                paymentMethodDataParam.setBaseRealAmount(ConfigUtil.convertToBasePrice(checkoutPayment.getRealAmount()));
                paymentMethodDataParam.setRealAmount(checkoutPayment.getBaseRealAmount());
                paymentMethodDataParam.setCode(checkoutPayment.getCode());
                paymentMethodDataParam.setIsPayLater(checkoutPayment.isPaylater());
                paymentMethodDataParam.setTitle(checkoutPayment.getTitle());
                paymentMethodDataParam.setCCOwner(checkoutPayment.getCCOwner());
                paymentMethodDataParam.setCCType(checkoutPayment.getCCType());
                paymentMethodDataParam.setCCNumber(checkoutPayment.getCCNumber());
                if (!StringUtil.isNullOrEmpty(checkoutPayment.getCCExpMonth())) {
//                String month = checkoutPayment.getCCExpMonth().substring(0, 2);
                    paymentMethodDataParam.setCCExpMonth(checkoutPayment.getCCExpMonth());
                } else {
                    paymentMethodDataParam.setCCExpMonth(checkoutPayment.getCCExpMonth());
                }
                paymentMethodDataParam.setCCExpYear(checkoutPayment.getCCExpYear());
                paymentMethodDataParam.setCID(checkoutPayment.getCID());
                listPaymentMethodParam.add(paymentMethodDataParam);

                if (checkoutPayment.getCode().equals(STORE_CREDIT_PAYMENT_CODE)) {
                    PlaceOrderIntegrationParam storeCreditIntegration = createPlaceOrderIntegrationParam();
                    List<PlaceOrderIntegrationOrderData> listStoreCreditOrderData = new ArrayList<>();
                    List<PlaceOrderIntegrationExtension> listStoreCreditExtension = new ArrayList<>();
                    storeCreditIntegration.setModule(STORE_CREDIT_MODULE);
                    storeCreditIntegration.setEventName(STORE_CREDIT_EVENT);

                    PlaceOrderIntegrationOrderData storeCreditBaseDiscount = createPlaceOrderIntegrationOrderData();
                    storeCreditBaseDiscount.setKey(STORE_CREDIT_BASE_DISCOUNT);
                    storeCreditBaseDiscount.setValue(-ConfigUtil.convertToBasePrice(checkoutPayment.getAmount()));
                    PlaceOrderIntegrationOrderData storeCreditDiscount = createPlaceOrderIntegrationOrderData();
                    storeCreditDiscount.setKey(STORE_CREDIT_DISCOUNT);
                    storeCreditDiscount.setValue(-checkoutPayment.getAmount());
                    listStoreCreditOrderData.add(storeCreditBaseDiscount);
                    listStoreCreditOrderData.add(storeCreditDiscount);

                    PlaceOrderIntegrationExtension storeCreditExtension = createPlaceOrderIntegrationExtension();
                    storeCreditExtension.setKey(STORE_CREDIT_BASE_DISCOUNT);
                    storeCreditExtension.setValue(ConfigUtil.convertToBasePrice(checkoutPayment.getAmount()));
                    listStoreCreditExtension.add(storeCreditExtension);

                    storeCreditIntegration.setOrderData(listStoreCreditOrderData);
                    storeCreditIntegration.setExtensionData(listStoreCreditExtension);

                    listIntegration.add(storeCreditIntegration);
                }
            }
        }

        placeOrderParams.setMethodData(listPaymentMethodParam);
        placeOrderParams.setPayment(placeOrderPaymentParam);

        // Extension data
        List<PlaceOrderExtensionParam> listExtension = new ArrayList<>();

        PlaceOrderExtensionParam extensionParamGrandTotal = createExtensionParam();
        extensionParamGrandTotal.setKey(KEY_EXTENSION_GRAND_TOTAL);
        extensionParamGrandTotal.setValue(String.valueOf(checkout.getGrandTotal()));

        PlaceOrderExtensionParam extensionParamBaseGrandTotal = createExtensionParam();
        extensionParamBaseGrandTotal.setKey(KEY_EXTENSION_BASE_GRAND_TOTAL);
        extensionParamBaseGrandTotal.setValue(String.valueOf(ConfigUtil.convertToBasePrice(checkout.getGrandTotal())));

        PlaceOrderExtensionParam extensionParamTaxAmount = createExtensionParam();
        extensionParamTaxAmount.setKey(KEY_EXTENSION_TAX_AMOUNT);
        extensionParamTaxAmount.setValue(String.valueOf(checkout.getTaxTotal()));

        PlaceOrderExtensionParam extensionParamBaseTaxAmount = createExtensionParam();
        extensionParamBaseTaxAmount.setKey(KEY_EXTENSION_BASE_TAX_AMOUNT);
        extensionParamBaseTaxAmount.setValue(String.valueOf(ConfigUtil.convertToBasePrice(checkout.getTaxTotal())));

        PlaceOrderExtensionParam extensionParamShipping = createExtensionParam();
        extensionParamShipping.setKey(KEY_EXTENSION_SHIPPING_AMOUNT);
        extensionParamShipping.setValue(String.valueOf(checkout.getShippingTotal()));

        PlaceOrderExtensionParam extensionParamBaseShipping = createExtensionParam();
        extensionParamBaseTaxAmount.setKey(KEY_EXTENSION_BASE_SHIPPING_AMOUNT);
        extensionParamBaseTaxAmount.setValue(String.valueOf(ConfigUtil.convertToBasePrice(checkout.getShippingTotal())));

        PlaceOrderExtensionParam extensionParamCustomerName = createExtensionParam();
        extensionParamCustomerName.setKey(KEY_EXTENSION_CUSTOMER_FULLNAME);
        if (!checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
            extensionParamCustomerName.setValue(checkout.getCustomer().getName());
        }

//        PlaceOrderExtensionParam extensionParamCreateAt = createExtensionParam();
//        extensionParamCreateAt.setKey(KEY_EXTENSION_CREATE_AT);
//        extensionParamCreateAt.setValue(ConfigUtil.convertToGMTTime(ConfigUtil.getCurrentDateTime()));

        PlaceOrderExtensionParam extensionParamStaffID = createExtensionParam();
        extensionParamStaffID.setKey(KEY_EXTENSION_WEBPOS_STAFF_ID);
        extensionParamStaffID.setValue(ConfigUtil.getStaff().getID());

        PlaceOrderExtensionParam extensionParamStaffName = createExtensionParam();
        extensionParamStaffName.setKey(KEY_EXTENSION_WEBPOS_STAFF_NAME);
        extensionParamStaffName.setValue(ConfigUtil.getStaff().getStaffName());

        PlaceOrderExtensionParam extensionParamChange = createExtensionParam();
        extensionParamChange.setKey(KEY_EXTENSION_WEBPOS_CHANGE);
        extensionParamChange.setValue(String.valueOf(checkout.getExchangeMoney()));

        PlaceOrderExtensionParam extensionParamBaseChange = createExtensionParam();
        extensionParamBaseChange.setKey(KEY_EXTENSION_WEBPOS_BASE_CHANGE);
        extensionParamBaseChange.setValue(String.valueOf(ConfigUtil.convertToBasePrice(checkout.getExchangeMoney())));

        PlaceOrderExtensionParam extensionParamShiftId = createExtensionParam();
        extensionParamShiftId.setKey(KEY_EXTENSION_WEBPOS_SHIFT_ID);
        extensionParamShiftId.setValue(ConfigUtil.getShiftId());

        PlaceOrderExtensionParam extensionParamLocationId = createExtensionParam();
        extensionParamLocationId.setKey(KEY_EXTENSION_LOCATION_ID);
        if (ConfigUtil.getPointOfSales() != null) {
            extensionParamLocationId.setValue(ConfigUtil.getPointOfSales().getLocationId());
        } else {
            extensionParamLocationId.setValue(ConfigUtil.getLocationId());
        }

        listExtension.add(extensionParamGrandTotal);
        listExtension.add(extensionParamBaseGrandTotal);
        listExtension.add(extensionParamTaxAmount);
        listExtension.add(extensionParamBaseTaxAmount);
        listExtension.add(extensionParamShipping);
        listExtension.add(extensionParamBaseShipping);
        listExtension.add(extensionParamCustomerName);
//        listExtension.add(extensionParamCreateAt);
        listExtension.add(extensionParamStaffID);
        listExtension.add(extensionParamStaffName);
        listExtension.add(extensionParamChange);
        listExtension.add(extensionParamBaseChange);
        listExtension.add(extensionParamShiftId);
        listExtension.add(extensionParamLocationId);

        placeOrderParams.setPlaceOrderExtensionData(listExtension);

        // add plugins
        if (ConfigUtil.PLATFORM_MAGENTO_2.equals(ConfigUtil.getPlatForm())) {
            List<GiftCard> listGiftCard = checkout.getListGiftCardUse();
            if (listGiftCard != null && listGiftCard.size() > 0) {
                PlaceOrderIntegrationParam giftCardIntegration = createPlaceOrderIntegrationParam();
                List<PlaceOrderIntegrationOrderData> listgiftCardOrderData = new ArrayList<>();
                List<PlaceOrderIntegrationExtension> listgiftCardExtension = new ArrayList<>();
                giftCardIntegration.setModule(GIFT_CARD_MODULE);
                giftCardIntegration.setEventName(GIFT_CARD_EVENT);

                float total_giftcard_discount = 0;
                for (GiftCard giftCard : listGiftCard) {
                    total_giftcard_discount += giftCard.getAmount();
                    PlaceOrderIntegrationExtension giftCardExtension = createPlaceOrderIntegrationExtension();
                    giftCardExtension.setKey(giftCard.getCouponCode());
                    giftCardExtension.setValue(ConfigUtil.convertToBasePrice(giftCard.getAmount()));
                    listgiftCardExtension.add(giftCardExtension);
                }

                PlaceOrderIntegrationOrderData giftCardBaseDiscount = createPlaceOrderIntegrationOrderData();
                giftCardBaseDiscount.setKey(GIFT_CARD_BASE_DISCOUNT);
                giftCardBaseDiscount.setValue(ConfigUtil.convertToBasePrice(total_giftcard_discount));

                PlaceOrderIntegrationOrderData giftCardDiscount = createPlaceOrderIntegrationOrderData();
                giftCardDiscount.setKey(GIFT_CARD_DISCOUNT);
                giftCardDiscount.setValue(total_giftcard_discount);
                listgiftCardOrderData.add(giftCardBaseDiscount);
                listgiftCardOrderData.add(giftCardDiscount);

                giftCardIntegration.setOrderData(listgiftCardOrderData);
                giftCardIntegration.setExtensionData(listgiftCardExtension);

                listIntegration.add(giftCardIntegration);
            }

            if (checkout.getRewardPointUsePointValue() != 0) {
                PlaceOrderIntegrationParam rewardIntegration = createPlaceOrderIntegrationParam();
                List<PlaceOrderIntegrationOrderData> listrewardOrderData = new ArrayList<>();
                List<PlaceOrderIntegrationExtension> listrewardExtension = new ArrayList<>();
                rewardIntegration.setModule(REWARD_POINT_MODULE);
                rewardIntegration.setEventName(REWARD_POINT_EVENT);

                PlaceOrderIntegrationExtension rewardExtension = createPlaceOrderIntegrationExtension();
                listrewardExtension.add(rewardExtension);

                PlaceOrderIntegrationOrderData rewardSpent = createPlaceOrderIntegrationOrderData();
                rewardSpent.setKey(REWARD_POINT_SPENT);
                rewardSpent.setValue(checkout.getRewardPoint().getAmount());


                PlaceOrderIntegrationOrderData rewardBaseDiscount = createPlaceOrderIntegrationOrderData();
                rewardBaseDiscount.setKey(REWARD_POINT_BASE_DISCOUNT);
                rewardBaseDiscount.setValue(ConfigUtil.convertToBasePrice(checkout.getRewardPointUsePointValue()));

                PlaceOrderIntegrationOrderData rewardDiscount = createPlaceOrderIntegrationOrderData();
                rewardDiscount.setKey(REWARD_POINT_DISCOUNT);
                rewardDiscount.setValue(checkout.getRewardPointUsePointValue());

                PlaceOrderIntegrationOrderData rewardBaseAmount = createPlaceOrderIntegrationOrderData();
                rewardBaseAmount.setKey(REWARD_POINT_BASE_AMOUNT);
                rewardBaseAmount.setValue(ConfigUtil.convertToBasePrice(checkout.getRewardPointUsePointValue()));

                PlaceOrderIntegrationOrderData rewardAmount = createPlaceOrderIntegrationOrderData();
                rewardAmount.setKey(REWARD_POINT_AMOUNT);
                rewardAmount.setValue(checkout.getRewardPointUsePointValue());

                listrewardOrderData.add(rewardSpent);
                listrewardOrderData.add(rewardBaseDiscount);
                listrewardOrderData.add(rewardDiscount);
                listrewardOrderData.add(rewardBaseAmount);
                listrewardOrderData.add(rewardAmount);

                rewardIntegration.setOrderData(listrewardOrderData);
                listIntegration.add(rewardIntegration);
            }
        }
        placeOrderParams.setIntegration(listIntegration);

        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.placeOrder(checkout, placeOrderParams, listCheckoutPayment);
    }

    @Override
    public void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout) {
        try {
            DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
            CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
            checkoutDataAccess.updateCartItemWithServerRespone(oldCheckout, newCheckout);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addOrderToListCheckout(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.addOrderToListCheckout(checkout);
    }

    @Override
    public boolean removeOrderToListCheckout(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.removeOrderToListCheckout(checkout);
    }

    @Override
    public String sendEmail(String email, String increment_id) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.sendEmail(email, increment_id);
    }

    @Override
    public boolean approvedAuthorizenet(Authorizenet authorizenet, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        String url = authorizenet.getPaymentInformation().getUrl();
        Map<String, String> params = authorizenet.getPaymentInformation().getParams();
        CheckoutPayment payment = listCheckoutPayment.get(0);
//        String month = payment.getCCExpMonth().substring(0, 2);
        params.put("x_exp_date", (payment.getCCExpMonth() + "/" + payment.getCCExpYear()));
        params.put("x_card_code", payment.getCID());
        params.put("x_card_num", payment.getCCNumber());
        params.put("cc_owner", payment.getCCOwner());
        params.put("cc_type", payment.getCCType());
        params.put("x_relay_url", "");
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        String urlParameters = postData.toString();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.approvedAuthorizenet(url, urlParameters);
    }

    @Override
    public boolean invoicesPaymentAuthozire(String orderID) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.invoicesPaymentAuthozire(orderID);
    }

    @Override
    public boolean cancelPaymentAuthozire(String orderID) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.cancelPaymentAuthozire(orderID);
    }

    @Override
    public String approvedPaymentPayPal(String payment_id) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.approvedPaymentPayPal(payment_id);
    }

    @Override
    public String approvedAuthorizeIntergration(String quote_id, String token, float amount) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.approvedAuthorizeIntergration(quote_id, token, amount);
    }

    @Override
    public String approvedPaymentStripe(String token, float amount) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.approvedStripe(token, amount);
    }

    @Override
    public String getAccessTokenPaypalHere() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.getAccessTokenPaypalHere();
    }

    @Override
    public boolean checkCreateInvoice(Checkout checkout) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.checkCreateInvoice(checkout);
    }

    @Override
    public Checkout updateTotal(Checkout checkout) {
        if (checkout != null) {
            if (checkout.getTotals() != null && checkout.getTotals().size() > 0) {
                boolean shipping = false;
                boolean discount = false;
                boolean tax = false;
                boolean gifcard = false;
                boolean reward_earn_point = false;
                boolean reward_use_point = false;
                for (CheckoutTotals checkoutTotals : checkout.getTotals()) {
                    if (checkoutTotals.getCode().equals("subtotal")) {
                        checkout.setSubTotal(checkoutTotals.getValue());
                        checkout.setSubTitle(checkoutTotals.getTitle());
                        checkout.setSubTotalView(ConfigUtil.convertToBasePrice(checkoutTotals.getValue()));
                    } else if (checkoutTotals.getCode().equals("shipping")) {
                        shipping = true;
                        checkout.setShippingTotal(ConfigUtil.convertToBasePrice(checkoutTotals.getValue()));
                        checkout.setShippingTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("discount")) {
                        discount = true;
                        checkout.setDiscountTotal(ConfigUtil.convertToBasePrice(checkoutTotals.getValue()));
                        checkout.setDiscountTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("tax")) {
                        tax = true;
                        checkout.setTaxTotal(ConfigUtil.convertToBasePrice(checkoutTotals.getValue()));
                        checkout.setTaxTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("grand_total")) {
                        checkout.setGrandTotal(checkoutTotals.getValue());
                        checkout.setGrandTitle(checkoutTotals.getTitle());
                        checkout.setGrandTotalView(ConfigUtil.convertToBasePrice(checkoutTotals.getValue()));
                    }

                    // plugins
                    else if (checkoutTotals.getCode().equals("giftvoucher") || checkoutTotals.getCode().equals("giftvoucheraftertax") || checkoutTotals.getCode().equals("giftvoucher_after_tax") || checkoutTotals.getCode().equals("giftvoucherbeforetax") || checkoutTotals.getCode().equals("giftvoucher_before_tax") || checkoutTotals.getCode().equals("giftcard")) {
                        gifcard = true;
                        checkout.setGiftCardDiscount(checkoutTotals.getValue());
                        checkout.setGiftCardTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("rewardpoints_label")) {
                        reward_earn_point = true;
                        checkout.setRewardPointEarnPointValue((int) checkoutTotals.getValue());
                    } else if (checkoutTotals.getCode().equals("use-point") || checkoutTotals.getCode().equals("rewardpoints")) {
                        reward_use_point = true;
                        checkout.setRewardPointUsePointValue(checkoutTotals.getValue());
                        checkout.setRewardPointUsePointTitle(checkoutTotals.getTitle());
                    }
                }
                if (!shipping) {
                    checkout.setShippingTotal(0);
                }
                if (!discount) {
                    checkout.setDiscountTotal(0);
                }
                if (!tax) {
                    checkout.setTaxTotal(0);
                }
                if (!gifcard) {
                    checkout.setGiftCardDiscount(0);
                }
                if (!reward_earn_point) {
                    checkout.setRewardPointEarnPointValue(0);
                }
                if (!reward_use_point) {
                    checkout.setRewardPointUsePointValue(0);
                }
            }
        }
        return checkout;
    }

    @Override
    public boolean delete(Checkout... checkouts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 1;
    }

    @Override
    public Checkout create() {
        Checkout checkout = new PosCheckout();
        checkout.setCreateAt(ConfigUtil.getCurrentTimeNow());
        List<CartItem> cartItemList = new ArrayList<CartItem>();
        checkout.setCartItem(cartItemList);
        return checkout;
    }

    @Override
    public Checkout retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Checkout> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        List<Checkout> checkoutList = new ArrayList<Checkout>();
        checkoutList.add(create());
        return checkoutList;
    }

    @Override
    public List<Checkout> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return retrieve(1, 1);
    }

    @Override
    public List<Checkout> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return retrieve(1, 1);
    }

    @Override
    public boolean update(Checkout oldModel, Checkout newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public CheckoutPayment createPaymentMethod() {
        return new PosCheckoutPayment();
    }

    @Override
    public CheckoutShipping createShipping() {
        return new PosCheckoutShipping();
    }

    @Override
    public Quote createQuote() {
        return new PosQuote();
    }

    @Override
    public QuoteItems createQuoteItems() {
        return new PosQuoteItems();
    }

    @Override
    public QuoteCustomer createQuoteCustomer() {
        return new PosQuoteCustomer();
    }

    @Override
    public QuoteCustomerAddress createCustomerAddress() {
        return new PosQuoteCustomerAddress();
    }

    @Override
    public List<QuoteItemExtension> createQuoteItemExtension() {
        return new ArrayList<>();
    }

    @Override
    public PlaceOrderParams createPlaceOrderParams() {
        return new PosPlaceOrderParams();
    }

    @Override
    public PaymentMethodDataParam createPaymentMethodParam() {
        return new PosPaymentMethodDataParam();
    }

    @Override
    public PlaceOrderExtensionParam createExtensionParam() {
        return new PosPlaceOrderExtensionParam();
    }

    @Override
    public SaveQuoteParam createSaveQuoteParam() {
        SaveQuoteParam saveQuoteParam = new PosSaveQuoteParam();
        PosSaveQuoteParam.QuoteData quoteData = saveQuoteParam.createQuoteData();
        saveQuoteParam.setQuoteData(quoteData);
        return saveQuoteParam;
    }

    @Override
    public QuoteAddCouponParam createQuoteAddCouponParam() {
        return new PosQuoteAddCouponParam();
    }

    @Override
    public PlaceOrderIntegrationParam createPlaceOrderIntegrationParam() {
        return new PosPlaceOrderIntegrationParam();
    }

    @Override
    public PlaceOrderIntegrationOrderData createPlaceOrderIntegrationOrderData() {
        return new PosPlaceOrderIntegrationOrderData();
    }

    @Override
    public PlaceOrderIntegrationExtension createPlaceOrderIntegrationExtension() {
        return new PosPlaceOrderIntegrationExtension();
    }

    @Override
    public CreditCard createCreditCard() {
        return new PosCreditCard();
    }

    @Override
    public StoreCredit createStoreCredit() {
        return new PosStoreCredit();
    }

    @Override
    public List<CustomerAddress> checkListAddress(Customer customer, Customer guest_customer) {
        if (!checkCustomerID(customer, guest_customer)) {
            return customer.getAddress();
        } else {
            List<CustomerAddress> nListAddress = new ArrayList<>();
            CustomerAddress guest_address = guest_customer.getAddress().get(0);
            String guest_address_id = guest_address.getID();
            for (CustomerAddress customerAddress : customer.getAddress()) {
                if (!customerAddress.getID().equals(guest_address_id)) {
                    nListAddress.add(customerAddress);
                }
            }
            return nListAddress;
        }
    }

    @Override
    public boolean checkIsVirtual(List<CartItem> cartItems) {
        boolean checkVitural = true;
        for (CartItem item : cartItems) {
            if (!StringUtil.isNullOrEmpty(item.getIsVirtual())) {
                if (item.getIsVirtual().equals("0") || (!item.getProductType().equals("virtual") && !item.getProductType().equals("customsale"))) {
                    checkVitural = false;
                }
            } else {
                checkVitural = false;
            }
        }
        return checkVitural;
    }

    @Override
    public boolean checkCustomerID(Customer customer, Customer guest_customer) {
        if (getCustomerId(customer).equals(getCustomerId(guest_customer))) {
            return false;
        }

        return true;
    }

    private String getCustomerId(Customer customer) {
        String customerId = "";
        String customerEmail = customer.getEmail();
        if (StringUtil.isNullOrEmpty(customer.getID())) {
            if (StringUtil.isNullOrEmpty(customerEmail)) {
                customerId = customerEmail;
            }
        } else {
            customerId = customer.getID();
        }
        return customerId;
    }

    private List<QuoteItems> addItemToQuote(Checkout checkout) {
        List<QuoteItems> listQuoteItem = new ArrayList<QuoteItems>();
        for (CartItem item : checkout.getCartItem()) {
            QuoteItems quoteItems = createQuoteItems();
            quoteItems.setQty(item.getQuantity());
            if (item.isTypeCustom()) {
                quoteItems.setCustomSale("1");
            }
            if (item.haveCustomPriceOrDiscount()) {
                quoteItems.setCustomPrice(Float.toString(ConfigUtil.convertToPrice(item.getUnitPrice())));
                quoteItems.setAmount(Float.toString(item.getUnitPrice()));
            } else {
                quoteItems.setCustomPrice(null);
                quoteItems.setAmount(null);
            }
            quoteItems.setId(item.getID());
            quoteItems.setItemId(item.getItemId());
            quoteItems.convertProductOption(item);

            List<QuoteItemExtension> quoteItemExtension = createQuoteItemExtension();
            addExtensionDataToQuote(item, quoteItemExtension);
            quoteItems.setExtensionData(quoteItemExtension);
            listQuoteItem.add(quoteItems);
        }
        return listQuoteItem;
    }

    private void addExtensionDataToQuote(CartItem item, List<QuoteItemExtension> quoteItemExtension) {
        float basePrice = item.getUnitPrice();
        float price = ConfigUtil.convertToPrice(basePrice);
        float basePriceInclTax = item.getPriceInclTax();
        float priceInclTax = ConfigUtil.convertToBasePrice(basePriceInclTax);
        float qty = item.getQuantity();
        float rowTotal = price * qty;
        float baseRowTotal = basePrice * qty;
        float rowTotalInclTax = priceInclTax * qty;
        float baseRowTotalInclTax = basePriceInclTax * qty;
        float discountAmount = item.getDiscountAmount();
        float baseDiscountAmount = item.getBaseDiscountAmount();

        float taxPercent = item.getTaxPercent();
        float unitTaxAmount = price * taxPercent / 100;
        float baseUnitTaxAmount = basePrice * taxPercent / 100;
        float taxAmount = unitTaxAmount * qty;
        float baseTaxAmount = baseUnitTaxAmount * qty;

        String customTaxClassId = item.getProduct().getTaxClassId();
        addKeyToExtensionData("row_total", String.valueOf(rowTotal), quoteItemExtension);
        addKeyToExtensionData("base_row_total", String.valueOf(baseRowTotal), quoteItemExtension);
        addKeyToExtensionData("row_total_incl_tax", String.valueOf(rowTotalInclTax), quoteItemExtension);
        addKeyToExtensionData("base_row_total_incl_tax", String.valueOf(baseRowTotalInclTax), quoteItemExtension);
        addKeyToExtensionData("price", String.valueOf(price), quoteItemExtension);
        addKeyToExtensionData("base_price", String.valueOf(basePrice), quoteItemExtension);
        addKeyToExtensionData("price_incl_tax", String.valueOf(priceInclTax), quoteItemExtension);
        addKeyToExtensionData("base_price_incl_tax", String.valueOf(basePriceInclTax), quoteItemExtension);
        addKeyToExtensionData("discount_amount", String.valueOf(discountAmount), quoteItemExtension);
        addKeyToExtensionData("base_discount_amount", String.valueOf(baseDiscountAmount), quoteItemExtension);
        addKeyToExtensionData("tax_amount", String.valueOf(taxAmount), quoteItemExtension);
        addKeyToExtensionData("base_tax_amount", String.valueOf(baseTaxAmount), quoteItemExtension);
        addKeyToExtensionData("custom_tax_class_id", String.valueOf(customTaxClassId), quoteItemExtension);
    }

    private void addKeyToExtensionData(String key, String value, List<QuoteItemExtension> quoteItemExtension) {
        QuoteItemExtension item = new PosQuoteItemExtension();
        item.setKey(key);
        item.setValue(value);
        quoteItemExtension.add(item);
    }

    private void addCustomerAddressToQuote(Checkout checkout, QuoteCustomer quoteCustomer) {
        List<CustomerAddress> listAddress = checkout.getCustomer().getAddress();
        if (!checkout.isPickAtStore()) {
            CustomerAddress address = ConfigUtil.getCustomerGuest().getAddress().get(0);
            QuoteCustomerAddress customerAddress = createCustomerAddress();
            customerAddress.setCountryId(address.getCountry());
            customerAddress.setRegionId(address.getRegion().getRegionID());
            customerAddress.setRegion(address.getRegion());
            customerAddress.setPostcode(address.getPostCode());
            customerAddress.setStreet(address.getStreet());
            customerAddress.setTelephone(address.getTelephone());
            customerAddress.setCity(address.getCity());
            customerAddress.setFirstname(checkout.getCustomer().getFirstName());
            customerAddress.setLastname(checkout.getCustomer().getLastName());
            customerAddress.setEmail(checkout.getCustomer().getEmail());
            quoteCustomer.setShippingAddress(customerAddress);
            if (listAddress != null && listAddress.size() > 2) {
                CustomerAddress billing_address;
                if (checkout.getCustomer().getUseOneAddress()) {
                    billing_address = listAddress.get(0);
                } else {
                    billing_address = listAddress.get(1);
                }
                if (!StringUtil.isNullOrEmpty(billing_address.isBilling())) {
                    if (billing_address.isBilling().equals("true")) {
                        QuoteCustomerAddress customerBillingAddress = createCustomerAddress();
                        customerBillingAddress.setCountryId(billing_address.getCountry());
                        customerBillingAddress.setRegionId(billing_address.getRegion().getRegionID());
                        customerAddress.setRegion(address.getRegion());
                        customerBillingAddress.setPostcode(billing_address.getPostCode());
                        customerBillingAddress.setStreet(billing_address.getStreet());
                        customerBillingAddress.setTelephone(billing_address.getTelephone());
                        customerBillingAddress.setCity(billing_address.getCity());
                        customerBillingAddress.setFirstname(checkout.getCustomer().getFirstName());
                        customerBillingAddress.setLastname(checkout.getCustomer().getLastName());
                        customerBillingAddress.setEmail(checkout.getCustomer().getEmail());
                        quoteCustomer.setBillingAddress(customerBillingAddress);
                    } else {
                        quoteCustomer.setBillingAddress(customerAddress);
                    }
                } else {
                    quoteCustomer.setBillingAddress(customerAddress);
                }
            } else {
                quoteCustomer.setBillingAddress(customerAddress);
            }
        } else {
            if (listAddress != null && listAddress.size() > 0) {
                if (listAddress.size() > 2) {
                    if (checkout.getCustomer().getUseOneAddress()) {
                        customerUseOneAddress(listAddress, checkout, quoteCustomer);
                    } else {
                        customerUseDiffentAddress(listAddress, checkout, quoteCustomer);
                    }
                } else {
                    customerUseOneAddress(listAddress, checkout, quoteCustomer);
                }
            }
        }
    }

    private void customerUseOneAddress(List<CustomerAddress> listAddress, Checkout checkout, QuoteCustomer quoteCustomer) {
        CustomerAddress address = listAddress.get(0);
        QuoteCustomerAddress customerAddress = createCustomerAddress();
        customerAddress.setCountryId(address.getCountry());
        customerAddress.setRegionId(address.getRegion().getRegionID());
        customerAddress.setRegion(address.getRegion());
        customerAddress.setPostcode(address.getPostCode());
        customerAddress.setStreet(address.getStreet());
        customerAddress.setTelephone(address.getTelephone());
        customerAddress.setCity(address.getCity());
        customerAddress.setFirstname(checkout.getCustomer().getFirstName());
        customerAddress.setLastname(checkout.getCustomer().getLastName());
        customerAddress.setEmail(checkout.getCustomer().getEmail());
        quoteCustomer.setShippingAddress(customerAddress);
        quoteCustomer.setBillingAddress(customerAddress);
    }

    private void customerUseDiffentAddress(List<CustomerAddress> listAddress, Checkout checkout, QuoteCustomer quoteCustomer) {
        CustomerAddress shippingAddress = listAddress.get(0);
        QuoteCustomerAddress customerShippingAddress = createCustomerAddress();
        customerShippingAddress.setCountryId(shippingAddress.getCountry());
        customerShippingAddress.setRegionId(shippingAddress.getRegion().getRegionID());
        customerShippingAddress.setRegion(shippingAddress.getRegion());
        customerShippingAddress.setPostcode(shippingAddress.getPostCode());
        customerShippingAddress.setStreet(shippingAddress.getStreet());
        customerShippingAddress.setTelephone(shippingAddress.getTelephone());
        customerShippingAddress.setCity(shippingAddress.getCity());
        customerShippingAddress.setFirstname(checkout.getCustomer().getFirstName());
        customerShippingAddress.setLastname(checkout.getCustomer().getLastName());
        customerShippingAddress.setEmail(checkout.getCustomer().getEmail());
        quoteCustomer.setShippingAddress(customerShippingAddress);
        CustomerAddress billingAddress = listAddress.get(1);
        CustomerAddress guest_address = ConfigUtil.getCustomerGuest().getAddress().get(0);
        if (billingAddress.getID().equals(guest_address.getID())) {
            if (listAddress.size() >= 3) {
                billingAddress = listAddress.get(2);
            } else {
                billingAddress = listAddress.get(0);
            }
        }
        QuoteCustomerAddress customerBillingAddress = createCustomerAddress();
        customerBillingAddress.setCountryId(billingAddress.getCountry());
        customerBillingAddress.setRegionId(billingAddress.getRegion().getRegionID());
        customerBillingAddress.setRegion(billingAddress.getRegion());
        customerBillingAddress.setPostcode(billingAddress.getPostCode());
        customerBillingAddress.setStreet(billingAddress.getStreet());
        customerBillingAddress.setTelephone(billingAddress.getTelephone());
        customerBillingAddress.setCity(billingAddress.getCity());
        customerBillingAddress.setFirstname(checkout.getCustomer().getFirstName());
        customerBillingAddress.setLastname(checkout.getCustomer().getLastName());
        customerBillingAddress.setEmail(checkout.getCustomer().getEmail());
        quoteCustomer.setBillingAddress(customerBillingAddress);
    }
}