package com.magestore.app.pos.service.checkout;

import android.text.TextUtils;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.CheckoutTotals;
import com.magestore.app.lib.model.checkout.PaymentMethodDataParam;
import com.magestore.app.lib.model.checkout.PlaceOrderExtensionParam;
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
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.CheckoutDataAccess;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.pos.model.checkout.PosCheckout;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.model.checkout.PosCheckoutShipping;
import com.magestore.app.pos.model.checkout.PosPaymentMethodDataParam;
import com.magestore.app.pos.model.checkout.PosPlaceOrderExtensionParam;
import com.magestore.app.pos.model.checkout.PosPlaceOrderParams;
import com.magestore.app.pos.model.checkout.PosQuote;
import com.magestore.app.pos.model.checkout.PosQuoteAddCouponParam;
import com.magestore.app.pos.model.checkout.PosQuoteCustomer;
import com.magestore.app.pos.model.checkout.PosQuoteCustomerAddress;
import com.magestore.app.pos.model.checkout.PosQuoteItemExtension;
import com.magestore.app.pos.model.checkout.PosQuoteItems;
import com.magestore.app.pos.model.checkout.PosSaveQuoteParam;
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
            if (StringUtil.isNullOrEmpty(checkout.getCustomerID()) || checkCustomerID(checkout.getCustomer(), ConfigUtil.getCustomerGuest())) {
                quote.setCustomerId("");
            } else {
                quote.setCustomerId(checkout.getCustomerID());
            }
        } else {
            quote.setCustomerId("");
        }
        // TODO: Giả data với (till_id = 1) sau fix lại theo config
        quote.setCurrencyId(ConfigUtil.getCurrentCurrency().getCode());
        quote.setStoreId(checkout.getStoreId());
        quote.setTillId("1");
        quote.setItems(addItemToQuote(checkout));

        addCustomerAddressToQuote(checkout, quoteCustomer);
        quote.setCustomer(quoteCustomer);
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        return checkoutDataAccess.saveCart(quote);
    }

    @Override
    public Checkout saveQuote(Checkout checkout, SaveQuoteParam quoteParam) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        quoteParam.setQuoteId(checkout.getQuoteId());
        quoteParam.setStoreId(checkout.getStoreId());
        quoteParam.setCustomerId(checkout.getCustomerID());
        // TODO: đang fix giá trị till_id
        quoteParam.setCurrencyId(ConfigUtil.getCurrentCurrency().getCode());
        quoteParam.setTillId("1");

        return checkoutDataAccess.saveQuote(quoteParam);
    }

    @Override
    public Checkout addCouponToQuote(Checkout checkout, QuoteAddCouponParam quoteAddCouponParam) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        quoteAddCouponParam.setQuoteId(checkout.getQuoteId());

        return checkoutDataAccess.addCouponToQuote(quoteAddCouponParam);
    }

    @Override
    public Checkout saveShipping(String quoteId, String shippingCode) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();

        // TODO: với trường hợp shipping ko có default thì truyền lên webpos_shipping_storepickup
        if (shippingCode.equals("")) {
            shippingCode = "webpos_shipping_storepickup";
        }
        return checkoutDataAccess.saveShipping(quoteId, shippingCode);
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

    @Override
    public Model placeOrder(String quoteId, Checkout checkout, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        PlaceOrderParams placeOrderParams = createPlaceOrderParams();
        placeOrderParams.setQuoteId(quoteId);
        PosPlaceOrderParams.PlaceOrderIntegration placeOrderIntegration = placeOrderParams.createPlaceOrderIntegration();
        placeOrderParams.setIntegration(placeOrderIntegration);
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
            if (listCheckoutPayment.get(0).getCode().equals("paypal_integration")) {
                placeOrderParams.setMethod("multipaymentforpos");
            } else {
                placeOrderParams.setMethod(listCheckoutPayment.get(0).getCode());
            }
        }

        List<PaymentMethodDataParam> listPaymentMethodParam = placeOrderParams.createPaymentMethodData();

        for (CheckoutPayment checkoutPayment : listCheckoutPayment) {
            if (checkoutPayment.getAdditionalData() == null) {
                PosCheckoutPayment.AdditionalData paymentAdditionParam = checkoutPayment.createAdditionalData();
                checkoutPayment.setAdditionalData(paymentAdditionParam);
            }
            PaymentMethodDataParam paymentMethodDataParam = createPaymentMethodParam();
            PosPaymentMethodDataParam.PaymentMethodAdditionalParam additionalParam = paymentMethodDataParam.createAddition();
            paymentMethodDataParam.setPaymentMethodAdditionalParam(additionalParam);
            paymentMethodDataParam.setReferenceNumber(checkoutPayment.getIsReferenceNumber());
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
            paymentMethodDataParam.setCCExpMonth(checkoutPayment.getCCExpMonth());
            paymentMethodDataParam.setCCExpYear(checkoutPayment.getCCExpYear());
            paymentMethodDataParam.setCID(checkoutPayment.getCID());
            listPaymentMethodParam.add(paymentMethodDataParam);
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

        PlaceOrderExtensionParam extensionParamCreateAt = createExtensionParam();
        extensionParamCreateAt.setKey(KEY_EXTENSION_CREATE_AT);
        extensionParamCreateAt.setValue(ConfigUtil.convertToGMTTime(ConfigUtil.getCurrentDateTime()));

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

        listExtension.add(extensionParamGrandTotal);
        listExtension.add(extensionParamBaseGrandTotal);
        listExtension.add(extensionParamTaxAmount);
        listExtension.add(extensionParamBaseTaxAmount);
        listExtension.add(extensionParamShipping);
        listExtension.add(extensionParamBaseShipping);
        listExtension.add(extensionParamCustomerName);
        listExtension.add(extensionParamCreateAt);
        listExtension.add(extensionParamStaffID);
        listExtension.add(extensionParamStaffName);
        listExtension.add(extensionParamChange);
        listExtension.add(extensionParamBaseChange);

        placeOrderParams.setPlaceOrderExtensionData(listExtension);

        // Khởi tạo customer gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.placeOrder(placeOrderParams);
    }

    @Override
    public void updateCartItemWithServerRespone(Checkout oldCheckout, Checkout newCheckout) {
        List<CartItem> listCartNew = newCheckout.getCartItem();
        List<CartItem> listCartOld = oldCheckout.getCartItem();
        for (CartItem cartNew : listCartNew) {
            for (CartItem cartOld : listCartOld) {
                if (cartOld.getItemId().equals(cartNew.getOfflineItemId())) {
                    cartOld.setItemId(cartNew.getItemId());
                    cartOld.setIsSaveCart(true);
                    cartOld.setPrice(cartNew.getPrice());
                    cartOld.getProduct().setItemId(cartNew.getItemId());
                    cartOld.getProduct().setIsSaveCart(true);
                    cartOld.setIsVirtual(cartNew.getIsVirtual());
                }
            }
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
    public String approvedAuthorizenet(Authorizenet authorizenet, List<CheckoutPayment> listCheckoutPayment) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        String url = authorizenet.getPaymentInformation().getUrl();
        Map<String, String> params = authorizenet.getPaymentInformation().getParams();
        CheckoutPayment payment = listCheckoutPayment.get(0);
        params.put("x_exp_date", (payment.getCCExpMonth() + "/" + payment.getCCExpYear()));
        params.put("x_card_code", payment.getCode());
        params.put("x_card_num", payment.getCCNumber());
        params.put("cc_owner", payment.getCCOwner());
        params.put("cc_type", payment.getCCType());
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
    public String approvedPaymentPayPal(String payment_id) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        CheckoutDataAccess checkoutDataAccess = factory.generateCheckoutDataAccess();
        return checkoutDataAccess.approvedPaymentPayPal(payment_id);
    }

    @Override
    public Checkout updateTotal(Checkout checkout) {
        if (checkout != null) {
            if (checkout.getTotals() != null && checkout.getTotals().size() > 0) {
                for (CheckoutTotals checkoutTotals : checkout.getTotals()) {
                    if (checkoutTotals.getCode().equals("subtotal")) {
                        checkout.setSubTotal(checkoutTotals.getValue());
                        checkout.setSubTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("shipping")) {
                        checkout.setShippingTotal(checkoutTotals.getValue());
                        checkout.setShippingTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("discount")) {
                        checkout.setDiscountTotal(checkoutTotals.getValue());
                        checkout.setDiscountTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("tax")) {
                        checkout.setTaxTotal(checkoutTotals.getValue());
                        checkout.setTaxTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("grand_total")) {
                        checkout.setGrandTotal(checkoutTotals.getValue());
                        checkout.setGrandTitle(checkoutTotals.getTitle());
                    }

                    // plugins
                    else if (checkoutTotals.getCode().equals("giftvoucheraftertax")) {
                        checkout.setGiftCardDiscount(checkoutTotals.getValue());
                        checkout.setGiftCardTitle(checkoutTotals.getTitle());
                    } else if (checkoutTotals.getCode().equals("rewardpoints_label")) {
                        checkout.setRewardPointEarnPointValue((int) checkoutTotals.getValue());
                    } else if (checkoutTotals.getCode().equals("use-point")) {
                        checkout.setRewardPointUsePointValue(checkoutTotals.getValue());
                        checkout.setRewardPointUsePointTitle(checkoutTotals.getTitle());
                    }
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
        checkout.setCreateAt(ConfigUtil.getCurrentTime());
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
    public List<CustomerAddress> checkListAddress(Customer customer, Customer guest_customer) {
        if (checkCustomerID(customer, guest_customer)) {
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
        for (CartItem item : cartItems) {
            if (item.getIsVirtual() != null || StringUtil.STRING_EMPTY.equals(item.getIsVirtual()) || item.getIsVirtual().equals("0")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkCustomerID(Customer customer, Customer guest_customer) {
        if (StringUtil.isNullOrEmpty(getCustomerId(customer)) && StringUtil.isNullOrEmpty(getCustomerId(guest_customer))) {
            return false;
        }

        if (getCustomerId(customer).equals(getCustomerId(guest_customer))) {
            return true;
        }

        return false;
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
            if (item.haveCustomPriceOrDiscount())
                quoteItems.setCustomPrice(String.valueOf(item.getUnitPrice()));
            else
                quoteItems.setCustomPrice(null);
            quoteItems.setAmount(item.getQuantity());
            quoteItems.setId(item.getID());
            quoteItems.setItemId(item.getItemId());
            quoteItems.convertProductOption(item);

            List<QuoteItemExtension> quoteItemExtension = createQuoteItemExtension();
            quoteItems.setExtensionData(quoteItemExtension);
            listQuoteItem.add(quoteItems);
        }
        return listQuoteItem;
    }

    private void addCustomerAddressToQuote(Checkout checkout, QuoteCustomer quoteCustomer) {
        List<CustomerAddress> listAddress = checkout.getCustomer().getAddress();
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

    private void customerUseOneAddress(List<CustomerAddress> listAddress, Checkout checkout, QuoteCustomer quoteCustomer) {
        CustomerAddress address = listAddress.get(0);
        QuoteCustomerAddress customerAddress = createCustomerAddress();
        customerAddress.setCountryId(address.getCountry());
        customerAddress.setRegionId(address.getRegion().getRegionID());
        customerAddress.setPostcode(address.getPostCode());
        customerAddress.setStreet(address.getStreet());
        customerAddress.setTelephone(address.getTelephone());
        customerAddress.setCity(address.getCity());
        customerAddress.setFirstname(address.getFirstName());
        customerAddress.setLastname(address.getLastName());
        customerAddress.setEmail(checkout.getCustomer().getEmail());
        quoteCustomer.setShippingAddress(customerAddress);
        quoteCustomer.setBillingAddress(customerAddress);
    }

    private void customerUseDiffentAddress(List<CustomerAddress> listAddress, Checkout checkout, QuoteCustomer quoteCustomer) {
        CustomerAddress shippingAddress = listAddress.get(0);
        QuoteCustomerAddress customerShippingAddress = createCustomerAddress();
        customerShippingAddress.setCountryId(shippingAddress.getCountry());
        customerShippingAddress.setRegionId(shippingAddress.getRegion().getRegionID());
        customerShippingAddress.setPostcode(shippingAddress.getPostCode());
        customerShippingAddress.setStreet(shippingAddress.getStreet());
        customerShippingAddress.setTelephone(shippingAddress.getTelephone());
        customerShippingAddress.setCity(shippingAddress.getCity());
        customerShippingAddress.setFirstname(shippingAddress.getFirstName());
        customerShippingAddress.setLastname(shippingAddress.getLastName());
        customerShippingAddress.setEmail(checkout.getCustomer().getEmail());
        quoteCustomer.setShippingAddress(customerShippingAddress);
        CustomerAddress billingAddress = listAddress.get(1);
        QuoteCustomerAddress customerBillingAddress = createCustomerAddress();
        customerBillingAddress.setCountryId(billingAddress.getCountry());
        customerBillingAddress.setRegionId(billingAddress.getRegion().getRegionID());
        customerBillingAddress.setPostcode(billingAddress.getPostCode());
        customerBillingAddress.setStreet(billingAddress.getStreet());
        customerBillingAddress.setTelephone(billingAddress.getTelephone());
        customerBillingAddress.setCity(billingAddress.getCity());
        customerBillingAddress.setFirstname(billingAddress.getFirstName());
        customerBillingAddress.setLastname(billingAddress.getLastName());
        customerBillingAddress.setEmail(checkout.getCustomer().getEmail());
        quoteCustomer.setBillingAddress(customerBillingAddress);
    }
}