package com.magestore.app.pos.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.model.checkout.payment.Authorizenet;
import com.magestore.app.lib.model.checkout.payment.CreditCard;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.plugins.RewardPoint;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.plugins.PluginsService;
import com.magestore.app.pos.PaymentPayPalActivity;
import com.magestore.app.pos.R;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.panel.CartItemDetailPanel;
import com.magestore.app.pos.panel.CartOrderListPanel;
import com.magestore.app.pos.panel.CheckoutAddPaymentPanel;
import com.magestore.app.pos.panel.CheckoutAddressListPanel;
import com.magestore.app.pos.panel.CheckoutDetailPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentCreditCardPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentWebviewPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.CheckoutSuccessPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.pos.panel.PluginGiftCardPanel;
import com.magestore.app.pos.panel.PluginRewardPointPanel;
import com.magestore.app.pos.panel.PluginStoreCreditPanel;
import com.magestore.app.pos.sdk.MultiReaderConnectionActivity;
import com.magestore.app.pos.sdk.PayPalHereSDKWrapper;
import com.magestore.app.pos.sdk.PayPalHereSDKWrapperCallbacks;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DataUtil;
import com.magestore.app.util.StringUtil;
import com.paypal.merchant.sdk.PayPalHereSDK;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutListController extends AbstractListController<Checkout> {
    public static final String STATE_ENABLE_CHANGE_CART_ITEM = "STATE_ENABLE_CHANGE_CART_ITEM";
    public static final String STATE_DISABLE_CHANGE_CART_ITEM = "STATE_DISABLE_CHANGE_CART_ITEM";

    public static final String STATE_ON_ADD_PAYMENT = "STATE_ON_ADD_PAYMENT";
    public static final String STATE_ON_PLACE_ORDER = "STATE_ON_PLACE_ORDER";
    public static final String STATE_ON_MARK_AS_PARTIAL = "STATE_ON_MARK_AS_PARTIAL";

    static final int ACTION_TYPE_USER_GUEST = 0;
    static final int ACTION_TYPE_UPDATE_ADDRESS = 1;
    static final int ACTION_TYPE_DELETE_ADDRESS = 2;
    static final int ACTION_TYPE_NEW_ADDRESS = 3;
    static final int ACTION_TYPE_SAVE_CART = 4;
    static final int ACTION_TYPE_SAVE_CART_DISCOUNT = 5;
    static final int ACTION_TYPE_SAVE_QUOTE = 6;
    static final int ACTION_TYPE_ADD_COUPON_TO_QUOTE = 7;
    static final int ACTION_TYPE_SAVE_SHIPPING = 8;
    static final int ACTION_TYPE_SAVE_PAYMENT = 9;
    static final int ACTION_TYPE_PLACE_ORDER = 10;
    static final int ACTION_TYPE_SEND_EMAIL = 11;
    static final int ACTION_TYPE_APPLY_REWARD_POINT = 12;
    static final int ACTION_TYPE_CHECK_APPROVED_PAYMENT_PAYPAL = 13;
    static final int ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET = 14;
    static final int ACTION_TYPE_INVOICE_PAYMENT_AUTHORIZENET = 15;
    static final int ACTION_TYPE_CANCEL_PAYMENT_AUTHORIZENET = 16;
    static final int ACTION_TYPE_CHECK_APPOVED_PAYMENT_STRIPE = 17;
    static final int ACTION_TYPE_REFRESH_TOKEN_PAYPAL_HERE = 18;
    static final int ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET_INTERATION = 19;

    static final int STATUS_CHECKOUT_ADD_ITEM = 0;
    public static final int STATUS_CHECKOUT_PROCESSING = 1;

    public static int START_ACTIVITY_MUTIREADER = 98;

    static final String PICK_AT_STORE_CODE = "webpos_shipping_storepickup";
    static final String PAYMENT_STRIPE_CODE = "stripe_integration";
    static final String PAYMENT_AUTHORIZE = "authorizenet_integration";

    Map<String, Object> wraper;
    CartItemListController mCartItemListController;
    CheckoutShippingListPanel mCheckoutShippingListPanel;
    CheckoutPaymentListPanel mCheckoutPaymentListPanel;
    ProductListController mProductListController;
    PaymentMethodListPanel mPaymentMethodListPanel;
    CheckoutAddPaymentPanel mCheckoutAddPaymentPanel;
    CartOrderListPanel mCartOrderListPanel;
    CheckoutAddressListPanel mCheckoutAddressListPanel;
    CheckoutSuccessPanel mCheckoutSuccessPanel;
    CheckoutPaymentWebviewPanel mCheckoutPaymentWebviewPanel;
    CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel;
    CartItemDetailPanel mCartItemDetailPanel;
    Context context;
    Customer guest_checkout;
    Currency currency;
    float maximum_discount;
    Map<String, String> configCCTypes;
    List<String> configMonths;
    Map<String, String> configCCYears;
    CustomerAddressService mCustomerAddressService;
    // plugins
    PluginsService pluginsService;
    PluginRewardPointPanel mPluginRewardPointPanel;
    PluginStoreCreditPanel mPluginStoreCreditPanel;
    PluginGiftCardPanel mPluginGiftCardPanel;

    @Override
    public List<Checkout> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (wraper == null)
            wraper = new HashMap<>();
        return super.onRetrieveBackground(page, pageSize);
    }

    @Override
    public synchronized void onRetrievePostExecute(List<Checkout> list) {
        super.onRetrievePostExecute(list);
        bindItem(list.get(0));
        doInputGuestCheckout();
        bindCustomer(guest_checkout);
    }

    public void bindCustomer(Customer customer) {
        if (customer != null) {
            wraper.put("customer", customer);

            getSelectedItem().setCustomer(customer);
            getSelectedItem().setCustomerID(customer.getID());
            getView().updateModel(getSelectedItem());
            mCartOrderListPanel.notifyDataSetChanged();
            if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE && getSelectedItem().getStatus() == STATUS_CHECKOUT_PROCESSING) {
                doInputSaveCart();
            }
        }
    }

    public void binCartItem() {
        Checkout checkout = getSelectedItem();
        checkout.setCartItem(mCartItemListController.getListCartItem());
    }

    public void doInputEditAddress(int type, Customer customer, CustomerAddress oldAddress, CustomerAddress newAddress) {
        wraper.put("type_update_address", type);
        wraper.put("customer_update_address", customer);
        wraper.put("old_address", oldAddress);
        doAction(ACTION_TYPE_UPDATE_ADDRESS, null, wraper, newAddress);
    }

    public void doInputDeleteAddress(int type, Customer customer, CustomerAddress customerAddress) {
        wraper.put("type_delete_address", type);
        wraper.put("customer_delete_address", customer);
        doAction(ACTION_TYPE_DELETE_ADDRESS, null, wraper, customerAddress);
    }

    public void doInputNewAddress(Customer customer, CustomerAddress customerAddress, int type) {
        wraper.put("customer_new_address", customer);
        wraper.put("type_new_address", type);
        doAction(ACTION_TYPE_NEW_ADDRESS, null, wraper, customerAddress);
    }

    /**
     * khi click checkout request savecart tạo quote
     */
    public void doInputSaveCart() {
        Checkout checkout = getSelectedItem();
        if (checkout.getCartItem().size() > 0) {
            checkout.setStatus(STATUS_CHECKOUT_PROCESSING);
            isShowLoadingDetail(true);
            ((CheckoutDetailPanel) mDetailView).isCheckCreateInvoice(true);
            // ẩn button checkout và hold order
            ((CheckoutListPanel) mView).changeActionButton(true);
            // show detail panel
            doShowDetailPanel(true);
            showSaleMenu(false);
//        binCartItem();
//            wraper.put("quote_id", );
            String store_id = DataUtil.getDataStringToPreferences(context, DataUtil.STORE_ID);
            checkout.setStoreId(store_id);
            wraper.put("quote_id", checkout.getQuoteId());
            doAction(ACTION_TYPE_SAVE_CART, null, wraper, checkout);
            ((CheckoutDetailPanel) mDetailView).hideCheckPaymenrRequired();
        } else {
            ((CheckoutDetailPanel) mDetailView).showNotifiAddItems();
            return;
        }
    }

    public void doInputSaveCartDiscount(int type, SaveQuoteParam quoteParam, QuoteAddCouponParam quoteAddCouponParam) {
        Checkout checkout = getSelectedItem();
        if (checkout.getCartItem().size() > 0) {
            showSaleMenu(true);
            ((CheckoutListPanel) mView).showLoading(true);
            // show detail panel
            String store_id = DataUtil.getDataStringToPreferences(context, DataUtil.STORE_ID);
            checkout.setStoreId(store_id);
            if (type == 0) {
                wraper.put("quote_param", quoteParam);
            } else {
                wraper.put("quote_add_coupon_param", quoteAddCouponParam);
            }
            wraper.put("quote_id", checkout.getQuoteId());
            wraper.put("type_save_cart_discount", type);
            doAction(ACTION_TYPE_SAVE_CART_DISCOUNT, null, wraper, checkout);
        } else {
            ((CheckoutDetailPanel) mDetailView).showNotifiAddItems();
            return;
        }
    }

    public void doInputSaveQuote(SaveQuoteParam quoteParam) {
        Checkout checkout = getSelectedItem();
        showButtonRemoveDiscount(true);
        ((CheckoutListPanel) mView).showLoading(true);
        // TODO: luôn save cart trước vì có trường hợp remove online xong save lại quote chưa có sản phẩm nên total = 0
        String store_id = DataUtil.getDataStringToPreferences(context, DataUtil.STORE_ID);
        checkout.setStoreId(store_id);
        wraper.put("quote_id", checkout.getQuoteId());
        wraper.put("quote_param", quoteParam);
        if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE) {
            wraper.put("type_save_quote", 1);
        } else {
            wraper.put("type_save_quote", 0);
        }
        doAction(ACTION_TYPE_SAVE_QUOTE, null, wraper, checkout);
    }

    public void doInputAddCouponToQuote(QuoteAddCouponParam quoteAddCouponParam) {
        Checkout checkout = getSelectedItem();
        ((CheckoutListPanel) mView).showLoading(true);
        // TODO: luôn save cart trước vì có trường hợp remove online xong save lại quote chưa có sản phẩm nên total = 0
        String store_id = DataUtil.getDataStringToPreferences(context, DataUtil.STORE_ID);
        checkout.setStoreId(store_id);
        wraper.put("quote_id", checkout.getQuoteId());
        wraper.put("quote_add_coupon_param", quoteAddCouponParam);
        if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE) {
            wraper.put("type_add_coupon", 1);
        } else {
            wraper.put("type_add_coupon", 0);
        }
        doAction(ACTION_TYPE_ADD_COUPON_TO_QUOTE, null, wraper, checkout);
    }

    public void doInputRemoveDiscount(String type_discount) {
        isShowSalesMenuToggle(false);
        Checkout checkout = getSelectedItem();
        ((CheckoutListPanel) mView).showLoading(true);
        SaveQuoteParam quoteParam = createSaveQuoteParam();
        quoteParam.setDiscountName("");
        quoteParam.setDiscountValue(0);
        quoteParam.setDiscountType(type_discount);
        String store_id = DataUtil.getDataStringToPreferences(context, DataUtil.STORE_ID);
        checkout.setStoreId(store_id);
        wraper.put("quote_id", checkout.getQuoteId());
        wraper.put("quote_param", quoteParam);
        if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE) {
            wraper.put("type_save_quote", 1);
        } else {
            wraper.put("type_save_quote", 0);
        }
        doAction(ACTION_TYPE_SAVE_QUOTE, null, wraper, checkout);
    }

    public void doInputApprovedPaymentPaypal(String payment_id) {
        isShowLoadingDetail(true);
        wraper.put("payment_id", payment_id);
        doAction(ACTION_TYPE_CHECK_APPROVED_PAYMENT_PAYPAL, null, wraper, null);
    }

    public void doInputApprovedAuthorizenet(Authorizenet authorizenet) {
        doAction(ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET, null, wraper, authorizenet);
    }

    public void doInputApprovedAuthorizenetIntergration(CheckoutPayment checkoutPayment) {
        doAction(ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET_INTERATION, null, wraper, checkoutPayment);
    }

    public void doInputRefreshTokenPaypalHere(CheckoutPayment paymentPayPalHere) {
        doAction(ACTION_TYPE_REFRESH_TOKEN_PAYPAL_HERE, null, wraper, paymentPayPalHere);
    }

    public void doInputPlaceOrderWithPaypalHere(String transaction_id) {
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        CheckoutPayment paymentPayPal = checkTypePaymentPaypalhere(listCheckoutPayment);
        paymentPayPal.setIsReferenceNumber(transaction_id.trim());
        wraper.put("list_payment", listCheckoutPayment);
        doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
    }

    public void doInputInvoiceAuthorize() {
        doAction(ACTION_TYPE_INVOICE_PAYMENT_AUTHORIZENET, null, wraper, null);
    }

    public void doInputCancelAuthorize() {
        doAction(ACTION_TYPE_CANCEL_PAYMENT_AUTHORIZENET, null, wraper, null);
    }

    public void doInputApprovedStripe(CheckoutPayment checkoutPayment) {
        doAction(ACTION_TYPE_CHECK_APPOVED_PAYMENT_STRIPE, null, wraper, checkoutPayment);
    }

    /**
     * khi chọn shipping request saveshipping và quote lưu lại shipping được chọn
     *
     * @param shippingCode
     */
    public void doInputSaveShipping(String shippingCode) {
        wraper.put("shipping_code", shippingCode);
        doAction(ACTION_TYPE_SAVE_SHIPPING, null, wraper, null);
    }

    public void doInputSavePayment(CheckoutPayment checkoutPayment) {
        wraper.put("select_payment", checkoutPayment);
        doAction(ACTION_TYPE_SAVE_PAYMENT, null, wraper, checkoutPayment);
    }

    /**
     * action khi click placeorder request tạo order hoặc invoice
     */
    public void doInputPlaceOrder() {
        // Check payment khác null hay ko
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            if (listCheckoutPayment.size() == 1 && listCheckoutPayment.get(0).getType().equals("1")) {
                CheckoutPayment paymentCreditCard = listCheckoutPayment.get(0);
                boolean requied_cvv = true;
                if (!StringUtil.isNullOrEmpty(paymentCreditCard.getUserCVV())) {
                    if (paymentCreditCard.getUserCVV().equals("1")) {
                        requied_cvv = false;
                    }
                }
                if (!mCheckoutPaymentCreditCardPanel.checkRequiedCard(requied_cvv)) {
                    return;
                }
                if (!mCheckoutPaymentCreditCardPanel.checkRequiedCreditCardType()) {
                    mCheckoutPaymentCreditCardPanel.showErrorCreditCardType();
                    return;
                }
                CheckoutPayment payment = mCheckoutPaymentCreditCardPanel.bind2Item();
                PosCheckoutPayment.AdditionalData additionalData = paymentCreditCard.createAdditionalData();
                paymentCreditCard.setAdditionalData(additionalData);
                paymentCreditCard.setCCOwner(payment.getCCOwner());
                paymentCreditCard.setCCType(payment.getCCType());
                paymentCreditCard.setCCNumber(payment.getCCNumber());
                paymentCreditCard.setCCExpMonth(payment.getCCExpMonth());
                paymentCreditCard.setCCExpYear(payment.getCCExpYear());
                paymentCreditCard.setCID(payment.getCID());
                // check có phải stripe payment không
                if (paymentCreditCard.getCode().equals(PAYMENT_STRIPE_CODE)) {
                    if (StringUtil.isNullOrEmpty(paymentCreditCard.getPublishKeyStripe())) {
                        ((CheckoutDetailPanel) mDetailView).showDialogError(getMagestoreContext().getActivity().getString(R.string.authorize_cancel_payment));
                        return;
                    }
                    isShowLoadingDetail(true);
                    new StripeTokenController(getMagestoreContext().getActivity(), paymentCreditCard.getPublishKeyStripe(), this, paymentCreditCard);
                } else if (paymentCreditCard.getCode().equals(PAYMENT_AUTHORIZE)) {
                    if (StringUtil.isNullOrEmpty(paymentCreditCard.getApiLogin()) || StringUtil.isNullOrEmpty(paymentCreditCard.getClientId())) {
                        ((CheckoutDetailPanel) mDetailView).showDialogError(getMagestoreContext().getActivity().getString(R.string.authorize_cancel_payment));
                        return;
                    }
                    isShowLoadingDetail(true);
                    new AuthorizeTokenController(getMagestoreContext().getActivity(), this, paymentCreditCard);
                } else {
                    doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
                    isShowLoadingDetail(true);
                }
            } else {
                CheckoutPayment paymentPayPal = checkTypePaymenPaypal(listCheckoutPayment);
                CheckoutPayment paymentPayPalHere = checkTypePaymentPaypalhere(listCheckoutPayment);
                if (paymentPayPal != null) {
                    Intent i = new Intent(getMagestoreContext().getActivity(), PaymentPayPalActivity.class);
                    i.putExtra("total", paymentPayPal.getAmount());
                    i.putExtra("client_id", paymentPayPal.getClientId());
                    i.putExtra("sandbox", paymentPayPal.getIsSandbox());
                    getMagestoreContext().getActivity().startActivity(i);
                } else if (paymentPayPalHere != null) {
                    isShowLoadingDetail(true);
                    actionPaypalHere(paymentPayPalHere);
                } else {
                    isShowLoadingDetail(true);
                    doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
                }
            }
        } else {
            if (getSelectedItem().getGrandTotal() == 0) {
                doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
                isShowLoadingDetail(true);
            } else {
                // hiển thị thông báo chọn payment
                ((CheckoutDetailPanel) mDetailView).showNotifiSelectPayment();
            }
        }
    }

    /**
     * lấy customer guest từ config và gán vào checkout
     */
    public void doInputGuestCheckout() {
        try {
            getSelectedItem().setIsPickAtStore(((CheckoutDetailPanel) mDetailView).getPickAtStore());
            guest_checkout = getConfigService().getGuestCheckout();
            currency = getConfigService().getDefaultCurrency();
            configCCTypes = getConfigService().getConfigCCTypes();
            configMonths = getConfigService().getConfigMonths();
            configCCYears = getConfigService().getConfigCCYears();
            maximum_discount = getConfigService().getConfigMaximumDiscount();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (wraper == null)
            wraper = new HashMap<>();
        wraper.put("customer", guest_checkout);
        doAction(ACTION_TYPE_USER_GUEST, null, wraper, guest_checkout);
    }

    public void doInputSendEmail(Map<String, Object> paramSendEmail) {
        showDetailOrderLoading(true);
        doAction(ACTION_TYPE_SEND_EMAIL, null, paramSendEmail, null);
    }

    public void doInputApplyRewardPoint(RewardPoint rewardPoint) {
        isShowLoadingDetail(true);
        doAction(ACTION_TYPE_APPLY_REWARD_POINT, null, wraper, rewardPoint);
    }

    private void showDetailOrderLoading(boolean b) {
        mCheckoutSuccessPanel.showDetailOrderLoading(b);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_TYPE_USER_GUEST) {
            return true;
        } else if (actionType == ACTION_TYPE_UPDATE_ADDRESS) {
            Customer customer = (Customer) wraper.get("customer_update_address");
            CustomerAddress oldAddress = (CustomerAddress) wraper.get("old_address");
            CustomerAddress newAddress = (CustomerAddress) models[0];
            mCustomerAddressService.update(customer, oldAddress, newAddress);
            return true;
        } else if (actionType == ACTION_TYPE_DELETE_ADDRESS) {
            Customer customer = (Customer) wraper.get("customer_delete_address");
            CustomerAddress customerAddress = (CustomerAddress) models[0];
            mCustomerAddressService.delete(customer, customerAddress);
            return true;
        } else if (actionType == ACTION_TYPE_NEW_ADDRESS) {
            Customer customer = (Customer) wraper.get("customer_new_address");
            CustomerAddress customerAddress = (CustomerAddress) models[0];
            mCustomerAddressService.insert(customer, customerAddress);
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_CART) {
            String quoteId = (String) wraper.get("quote_id");
            wraper.put("save_cart", ((CheckoutService) getListService()).saveCart((Checkout) models[0], quoteId));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_CART_DISCOUNT) {
            String quoteId = (String) wraper.get("quote_id");
            wraper.put("save_cart_discount", ((CheckoutService) getListService()).saveCart((Checkout) models[0], quoteId));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_QUOTE) {
            SaveQuoteParam saveQuoteParam = (SaveQuoteParam) wraper.get("quote_param");
            wraper.put("save_quote", ((CheckoutService) getListService()).saveQuote((Checkout) models[0], saveQuoteParam));
            return true;
        } else if (actionType == ACTION_TYPE_ADD_COUPON_TO_QUOTE) {
            QuoteAddCouponParam quoteAddCouponParam = (QuoteAddCouponParam) wraper.get("quote_add_coupon_param");
            wraper.put("save_add_coupon_to_quote", ((CheckoutService) getListService()).addCouponToQuote((Checkout) models[0], quoteAddCouponParam));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_SHIPPING) {
            String shippingCode = (String) wraper.get("shipping_code");
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("save_shipping", ((CheckoutService) getListService()).saveShipping(quoteId, shippingCode));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_PAYMENT) {
            String paymentCode = ((CheckoutPayment) models[0]).getCode();
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("save_payment", ((CheckoutService) getListService()).savePayment(quoteId, paymentCode));
            return true;
        } else if (actionType == ACTION_TYPE_PLACE_ORDER) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
            checkout.setCreateShip(((CheckoutDetailPanel) mDetailView).isCreateShip());
            checkout.setCreateInvoice(((CheckoutDetailPanel) mDetailView).isCreateInvoice());
            checkout.setNote(((CheckoutDetailPanel) mDetailView).getNote());
            checkout.setDeliveryDate(getSelectedItem().getDeliveryDate());
            checkout.setListGiftCardUse(mPluginGiftCardPanel.getListGiftCard());
            wraper.put("save_shipping", checkout);
            wraper.put("save_cart", checkout);
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("place_order", ((CheckoutService) getListService()).placeOrder(quoteId, checkout, listCheckoutPayment));
            return true;
        } else if (actionType == ACTION_TYPE_SEND_EMAIL) {
            String email = (String) wraper.get("email");
            String increment_id = (String) wraper.get("increment_id");
            String responServer = ((CheckoutService) getListService()).sendEmail(email, increment_id);
            wraper.put("send_email_response", responServer);
            return true;
        } else if (actionType == ACTION_TYPE_APPLY_REWARD_POINT) {
            RewardPoint rewardPoint = (RewardPoint) models[0];
            wraper.put("save_reward_point", pluginsService.applyRewarPoint(rewardPoint));
            return true;
        } else if (actionType == ACTION_TYPE_CHECK_APPROVED_PAYMENT_PAYPAL) {
            String payment_id = (String) wraper.get("payment_id");
            wraper.put("paypal_transaction_id", ((CheckoutService) getListService()).approvedPaymentPayPal(payment_id));
            return true;
        } else if (actionType == ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            Authorizenet authorizenet = (Authorizenet) models[0];
            wraper.put("authorize_respone", ((CheckoutService) getListService()).approvedAuthorizenet(authorizenet, listCheckoutPayment));
            return true;
        } else if (actionType == ACTION_TYPE_INVOICE_PAYMENT_AUTHORIZENET) {
            Authorizenet authorizenet = (Authorizenet) wraper.get("place_order");
            String order_id = authorizenet.getOrder().getID();
            ((CheckoutService) getListService()).invoicesPaymentAuthozire(order_id);
            return true;
        } else if (actionType == ACTION_TYPE_CANCEL_PAYMENT_AUTHORIZENET) {
            Authorizenet authorizenet = (Authorizenet) wraper.get("place_order");
            String order_id = authorizenet.getOrder().getID();
            ((CheckoutService) getListService()).cancelPaymentAuthozire(order_id);
            return true;
        } else if (actionType == ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET_INTERATION) {
            CheckoutPayment checkoutPayment = (CheckoutPayment) models[0];
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("authorize_transaction_id", ((CheckoutService) getListService()).approvedAuthorizeIntergration(quoteId, checkoutPayment.getAuthorizeToken(), checkoutPayment.getBaseAmount()));
            return true;
        } else if (actionType == ACTION_TYPE_CHECK_APPOVED_PAYMENT_STRIPE) {
            CheckoutPayment checkoutPayment = (CheckoutPayment) models[0];
            wraper.put("stripe_transaction_id", ((CheckoutService) getListService()).approvedPaymentStripe(checkoutPayment.getStripeToken(), checkoutPayment.getBaseAmount()));
            return true;
        } else if (actionType == ACTION_TYPE_REFRESH_TOKEN_PAYPAL_HERE) {
            wraper.put("access_token", ((CheckoutService) getListService()).getAccessTokenPaypalHere());
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_TYPE_USER_GUEST) {
            Customer customer = (Customer) models[0];
            ((CheckoutListPanel) mView).useDefaultGuestCheckout(customer);
            mCartOrderListPanel.bindList(getSelectedItems());
//            mCartItemDetailPanel.setCurrency(currency);
        } else if (success && actionType == ACTION_TYPE_UPDATE_ADDRESS) {
            int typeAddress = (int) wraper.get("type_update_address");
            CustomerAddress customerAddress = (CustomerAddress) wraper.get("old_address");
            ((CheckoutListPanel) mView).updateAddress(0, typeAddress, customerAddress);
        } else if (success && actionType == ACTION_TYPE_DELETE_ADDRESS) {
            int typeAddress = (int) wraper.get("type_delete_address");
            CustomerAddress customerAddress = (CustomerAddress) models[0];
            ((CheckoutListPanel) mView).updateAddress(1, typeAddress, customerAddress);
        } else if (success && actionType == ACTION_TYPE_NEW_ADDRESS) {
            CustomerAddress customerAddress = (CustomerAddress) models[0];
            int typeAddress = 3;
            int type_new_address = (int) wraper.get("type_new_address");
            if (type_new_address == 1) {
                ((CheckoutListPanel) mView).updateCheckoutAddress();
                Customer customer = (Customer) wraper.get("customer");
                mCheckoutAddressListPanel.bindListModel((List<Model>) (List<?>) customer.getAddress());
                customer.setAddressPosition(mCheckoutAddressListPanel.getSelectPos());
                changeShippingAddress(customerAddress);
            }
            ((CheckoutListPanel) mView).updateAddress(0, typeAddress, customerAddress);
        } else if (success && actionType == ACTION_TYPE_SAVE_CART) {
            Checkout checkout = (Checkout) wraper.get("save_cart");
            String quoteId = checkout.getQuote().getID();
            // cập nhật lại id trong cart item
            ((CheckoutService) getListService()).updateCartItemWithServerRespone(getSelectedItem(), checkout);
            mCartItemListController.bindList(getSelectedItem().getCartItem());

            // cập nhật list shipping và payment
            List<CheckoutShipping> listShipping = checkout.getCheckoutShipping();
            List<CheckoutPayment> listPayment = checkout.getCheckoutPayment();

            if (((CheckoutService) getListService()).checkIsVirtual(checkout.getCartItem())) {
                ((CheckoutDetailPanel) mDetailView).showPickAtStore(false);
                ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(false);
            } else {
                ((CheckoutDetailPanel) mDetailView).showPickAtStore(true);
                ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(true);
            }

            mPaymentMethodListPanel.bindList(listPayment);
            mCheckoutAddPaymentPanel.bindList(listPayment);

            // hiển thị list shipping address
            Customer customer = (Customer) wraper.get("customer");
            mCheckoutAddressListPanel.bindListModel((List<Model>) (List<?>) ((CheckoutService) getListService()).checkListAddress(customer, guest_checkout));
            mCheckoutAddressListPanel.setSelectPos(customer.getAddressPosition());
            mCheckoutAddressListPanel.scrollToPosition();

            // lưu quote data vào checkout
            getSelectedItem().setQuoteId(quoteId);
            //  cập nhật giá
            ((CheckoutService) getListService()).updateTotal(checkout);
            getSelectedItem().setGrandTotal(checkout.getGrandTotal());
//            showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
            showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());

            mCheckoutPaymentListPanel.setCheckout(checkout);

            // show shipping total
            ((CheckoutListPanel) mView).showSalesShipping(true);
            // câp nhật giá
            ((CheckoutListPanel) mView).updateTotalPrice(checkout);

            if (listShipping != null && listShipping.size() > 0) {
                // bind data to shipping method list
                bindDataToShippingMethodList(listShipping);
                // auto select shipping method
                autoSelectShipping(listShipping);
            } else {
                List<CheckoutPayment> listChoosePayment = (List<CheckoutPayment>) wraper.get("list_payment");
                if (listChoosePayment != null) {
                    listChoosePayment = new ArrayList<>();
                    wraper.put("list_payment", listChoosePayment);
                }
                checkout.setRemainMoney(0);
                checkout.setRealAmount(0);
                checkout.setExchangeMoney(0);
                checkout.setCustomer(getSelectedItem().getCustomer());
                checkout.setCustomerID(getSelectedItem().getCustomerID());
                mCheckoutPaymentListPanel.bindList(listChoosePayment);
                ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
                ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
                mCheckoutPaymentListPanel.setCheckout(checkout);
                mCheckoutPaymentListPanel.resetListPayment();
                mPluginGiftCardPanel.resetListGiftCard();
                // plugins
                if (checkout.getRewardPoint() != null) {
                    if (checkout.getRewardPoint().getBalance() != 0 && ConfigUtil.isEnableRewardPoint()) {
                        getSelectedItem().setRewardPoint(checkout.getRewardPoint());
//                        mPluginRewardPointPanel.resetPointValue();
                        mPluginRewardPointPanel.bindItem(checkout.getRewardPoint());
                        mPluginRewardPointPanel.setVisibility(View.VISIBLE);
                    } else {
                        mPluginRewardPointPanel.setVisibility(View.GONE);
                    }
                }
                if (checkout.getStoreCredit() != null) {
                    if (checkout.getStoreCredit().getBalance() != 0 && ConfigUtil.isEnableStoreCredit()) {
                        mPluginStoreCreditPanel.setVisibility(View.VISIBLE);
                        mPluginStoreCreditPanel.bindItem(checkout.getStoreCredit());
                    } else {
                        mPluginStoreCreditPanel.setVisibility(View.GONE);
                    }
                }
                autoSelectPaymentMethod(listPayment);
                isShowPluginStoreCredit(true);
                isShowPaymentMethod((checkout.getGrandTotal() == 0) ? false : true);
                isShowLoadingDetail(false);
            }
        } else if (success && actionType == ACTION_TYPE_SAVE_CART_DISCOUNT) {
            Checkout checkout = (Checkout) wraper.get("save_cart_discount");
            String quoteId = checkout.getQuote().getID();
            getSelectedItem().setQuoteId(quoteId);
            // cập nhật lại id trong cart item
            ((CheckoutService) getListService()).updateCartItemWithServerRespone(getSelectedItem(), checkout);
            ((CheckoutService) getListService()).updateTotal(checkout);
            mCartItemListController.bindList(getSelectedItem().getCartItem());

            int type_save_cart_discount = (int) wraper.get("type_save_cart_discount");

            if (type_save_cart_discount == 0) {
                SaveQuoteParam quoteParam = (SaveQuoteParam) wraper.get("quote_param");
                doInputSaveQuote(quoteParam);
            } else {
                QuoteAddCouponParam quoteAddCouponParam = (QuoteAddCouponParam) wraper.get("quote_add_coupon_param");
                doInputAddCouponToQuote(quoteAddCouponParam);
            }
        } else if (success && actionType == ACTION_TYPE_SAVE_QUOTE) {
            Checkout checkout = (Checkout) wraper.get("save_quote");
            wraper.put("save_cart", checkout);
            int type_save_quote = (int) wraper.get("type_save_quote");
            // cập nhật lại id trong cart item
            ((CheckoutService) getListService()).updateCartItemWithServerRespone(getSelectedItem(), checkout);
            mCartItemListController.bindList(getSelectedItem().getCartItem());
            if (type_save_quote == 1) {
                // cập nhật list shipping và payment
                List<CheckoutShipping> listShipping = checkout.getCheckoutShipping();
                List<CheckoutPayment> listPayment = checkout.getCheckoutPayment();

                if (((CheckoutService) getListService()).checkIsVirtual(checkout.getCartItem())) {
                    ((CheckoutDetailPanel) mDetailView).showPickAtStore(false);
                    ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(false);
                } else {
                    ((CheckoutDetailPanel) mDetailView).showPickAtStore(true);
                    ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(true);
                }

                if (listShipping != null && listShipping.size() > 0) {
                    // bind data to shipping method list
                    bindDataToShippingMethodList(listShipping);
                    // auto select shipping method
                    autoSelectShipping(listShipping);
                } else {
                    List<CheckoutPayment> listChoosePayment = (List<CheckoutPayment>) wraper.get("list_payment");
                    if (listChoosePayment != null) {
                        listChoosePayment = new ArrayList<>();
                        wraper.put("list_payment", listChoosePayment);
                    }
                    checkout.setRemainMoney(0);
                    checkout.setRealAmount(0);
                    checkout.setExchangeMoney(0);
                    checkout.setCustomer(getSelectedItem().getCustomer());
                    checkout.setCustomerID(getSelectedItem().getCustomerID());
                    mCheckoutPaymentListPanel.bindList(listChoosePayment);
                    ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
                    ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
                    wraper.put("save_quote", checkout);
                    mCheckoutPaymentListPanel.resetListPayment();
                    mPluginGiftCardPanel.resetListGiftCard();
                    // plugins
                    if (checkout.getRewardPoint() != null) {
                        if (checkout.getRewardPoint().getBalance() != 0 && ConfigUtil.isEnableRewardPoint()) {
                            getSelectedItem().setRewardPoint(checkout.getRewardPoint());
//                            mPluginRewardPointPanel.resetPointValue();
                            mPluginRewardPointPanel.bindItem(checkout.getRewardPoint());
                            mPluginRewardPointPanel.setVisibility(View.VISIBLE);
                        } else {
                            mPluginRewardPointPanel.setVisibility(View.GONE);
                        }
                    }
                    if (checkout.getStoreCredit() != null) {
                        if (checkout.getStoreCredit().getBalance() != 0 && ConfigUtil.isEnableStoreCredit()) {
                            mPluginStoreCreditPanel.setVisibility(View.VISIBLE);
                            mPluginStoreCreditPanel.bindItem(checkout.getStoreCredit());
                        } else {
                            mPluginStoreCreditPanel.setVisibility(View.GONE);
                        }
                    }
                    autoSelectPaymentMethod(listPayment);
                    isShowPluginStoreCredit(true);
                    isShowPaymentMethod((checkout.getGrandTotal() == 0) ? false : true);
                }

                mPaymentMethodListPanel.bindList(listPayment);
                mCheckoutAddPaymentPanel.bindList(listPayment);

                // hiển thị list shipping address
                Customer customer = (Customer) wraper.get("customer");
                mCheckoutAddressListPanel.bindListModel(((List<Model>) (List<?>) ((CheckoutService) getListService()).checkListAddress(customer, guest_checkout)));
                mCheckoutAddressListPanel.setSelectPos(customer.getAddressPosition());
                mCheckoutAddressListPanel.scrollToPosition();

                mCheckoutPaymentListPanel.setCheckout(checkout);

                // show shipping total
                ((CheckoutListPanel) mView).showSalesShipping(true);

                ((CheckoutListPanel) mView).showButtonCustomSales(false);
            }

            //  cập nhật giá
            ((CheckoutService) getListService()).updateTotal(checkout);
            getSelectedItem().setGrandTotal(checkout.getGrandTotal());
            getSelectedItem().setDiscountTitle(checkout.getDiscountTitle());
            getSelectedItem().setDiscountTotal(checkout.getDiscountTotal());

            if (!checkDiscount(checkout)) {
                showButtonRemoveDiscount(false);
            }
            showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);

            ((CheckoutListPanel) mView).updateTotalPrice(checkout);
            mCartItemListController.updateTotalPrice();
            ((CheckoutListPanel) mView).showLoading(false);
        } else if (success && actionType == ACTION_TYPE_ADD_COUPON_TO_QUOTE) {
            Checkout checkout = (Checkout) wraper.get("save_add_coupon_to_quote");
            wraper.put("save_cart", checkout);
            int type_add_coupon = (int) wraper.get("type_add_coupon");

            if (type_add_coupon == 1) {
                // cập nhật list shipping và payment
                List<CheckoutShipping> listShipping = checkout.getCheckoutShipping();
                List<CheckoutPayment> listPayment = checkout.getCheckoutPayment();

                // cập nhật lại id trong cart item
                ((CheckoutService) getListService()).updateCartItemWithServerRespone(getSelectedItem(), checkout);
                mCartItemListController.bindList(getSelectedItem().getCartItem());
                if (((CheckoutService) getListService()).checkIsVirtual(checkout.getCartItem())) {
                    ((CheckoutDetailPanel) mDetailView).showPickAtStore(false);
                    ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(false);
                } else {
                    ((CheckoutDetailPanel) mDetailView).showPickAtStore(true);
                    ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(true);
                }

                if (listShipping != null && listShipping.size() > 0) {
                    // bind data to shipping method list
                    bindDataToShippingMethodList(listShipping);
                    // auto select shipping method
                    autoSelectShipping(listShipping);
                } else {
                    List<CheckoutPayment> listChoosePayment = (List<CheckoutPayment>) wraper.get("list_payment");
                    if (listChoosePayment != null) {
                        listChoosePayment = new ArrayList<>();
                        wraper.put("list_payment", listChoosePayment);
                    }
                    checkout.setRemainMoney(0);
                    checkout.setRealAmount(0);
                    checkout.setExchangeMoney(0);
                    checkout.setCustomer(getSelectedItem().getCustomer());
                    checkout.setCustomerID(getSelectedItem().getCustomerID());
                    mCheckoutPaymentListPanel.bindList(listChoosePayment);
                    ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
                    ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
                    wraper.put("save_quote", checkout);
                    mCheckoutPaymentListPanel.resetListPayment();
                    mPluginGiftCardPanel.resetListGiftCard();
                    // plugins
                    if (checkout.getRewardPoint() != null) {
                        if (checkout.getRewardPoint().getBalance() != 0 && ConfigUtil.isEnableRewardPoint()) {
                            getSelectedItem().setRewardPoint(checkout.getRewardPoint());
//                            mPluginRewardPointPanel.resetPointValue();
                            mPluginRewardPointPanel.bindItem(checkout.getRewardPoint());
                            mPluginRewardPointPanel.setVisibility(View.VISIBLE);
                        } else {
                            mPluginRewardPointPanel.setVisibility(View.GONE);
                        }
                    }
                    if (checkout.getStoreCredit() != null) {
                        if (checkout.getStoreCredit().getBalance() != 0 && ConfigUtil.isEnableStoreCredit()) {
                            mPluginStoreCreditPanel.setVisibility(View.VISIBLE);
                            mPluginStoreCreditPanel.bindItem(checkout.getStoreCredit());
                        } else {
                            mPluginStoreCreditPanel.setVisibility(View.GONE);
                        }
                    }
                    autoSelectPaymentMethod(listPayment);
                    isShowPluginStoreCredit(true);
                    isShowPaymentMethod((checkout.getGrandTotal() == 0) ? false : true);
                }

                mPaymentMethodListPanel.bindList(listPayment);
                mCheckoutAddPaymentPanel.bindList(listPayment);

                // hiển thị list shipping address
                Customer customer = (Customer) wraper.get("customer");
                mCheckoutAddressListPanel.bindListModel(((List<Model>) (List<?>) ((CheckoutService) getListService()).checkListAddress(customer, guest_checkout)));
                mCheckoutAddressListPanel.setSelectPos(customer.getAddressPosition());
                mCheckoutAddressListPanel.scrollToPosition();

                mCheckoutPaymentListPanel.setCheckout(checkout);

                // show shipping total
                ((CheckoutListPanel) mView).showSalesShipping(true);

                ((CheckoutListPanel) mView).showButtonCustomSales(false);
            }
            //  cập nhật giá
            ((CheckoutService) getListService()).updateTotal(checkout);
            getSelectedItem().setGrandTotal(checkout.getGrandTotal());
            getSelectedItem().setDiscountTitle(checkout.getDiscountTitle());
            getSelectedItem().setDiscountTotal(checkout.getDiscountTotal());
            if (!checkDiscount(checkout)) {
                showButtonRemoveDiscount(false);
            }
            showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
            ((CheckoutListPanel) mView).updateTotalPrice(checkout);
            mCartItemListController.updateTotalPrice();
            ((CheckoutListPanel) mView).showLoading(false);
        } else if (success && actionType == ACTION_TYPE_SAVE_SHIPPING) {
            Checkout checkout = (Checkout) wraper.get("save_shipping");
            ((CheckoutService) getListService()).updateCartItemWithServerRespone(getSelectedItem(), checkout);
            mCartItemListController.bindList(getSelectedItem().getCartItem());
            // cập nhật list payment
            mPaymentMethodListPanel.bindList(checkout.getCheckoutPayment());
            mCheckoutAddPaymentPanel.bindList(checkout.getCheckoutPayment());
            //  cập nhật giá
            ((CheckoutService) getListService()).updateTotal(checkout);
            getSelectedItem().setGrandTotal(checkout.getGrandTotal());
//            showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
            showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());

            ((CheckoutListPanel) mView).updateTotalPrice(checkout);
            // show payment method
            ((CheckoutDetailPanel) mDetailView).showPaymentMethod();
            List<CheckoutPayment> listChoosePayment = (List<CheckoutPayment>) wraper.get("list_payment");
            if (listChoosePayment != null) {
                listChoosePayment = new ArrayList<>();
                wraper.put("list_payment", listChoosePayment);
            }
            checkout.setRemainMoney(0);
            checkout.setRealAmount(0);
            checkout.setExchangeMoney(0);
            checkout.setCustomer(getSelectedItem().getCustomer());
            checkout.setCustomerID(getSelectedItem().getCustomerID());
            mCheckoutPaymentListPanel.bindList(listChoosePayment);
            ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
            ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
            mCheckoutPaymentListPanel.resetListPayment();
            mCheckoutPaymentListPanel.setCheckout(checkout);
            wraper.put("save_shipping", checkout);
            mPluginGiftCardPanel.resetListGiftCard();
            // plugins
            if (checkout.getRewardPoint() != null) {
                if (checkout.getRewardPoint().getBalance() != 0 && ConfigUtil.isEnableRewardPoint()) {
                    getSelectedItem().setRewardPoint(checkout.getRewardPoint());
//                    mPluginRewardPointPanel.resetPointValue();
                    mPluginRewardPointPanel.bindItem(checkout.getRewardPoint());
                    mPluginRewardPointPanel.setVisibility(View.VISIBLE);
                } else {
                    mPluginRewardPointPanel.setVisibility(View.GONE);
                }
            }
            if (checkout.getStoreCredit() != null) {
                if (checkout.getStoreCredit().getBalance() != 0 && ConfigUtil.isEnableStoreCredit()) {
                    mPluginStoreCreditPanel.bindItem(checkout.getStoreCredit());
                    mPluginStoreCreditPanel.setVisibility(View.VISIBLE);
                } else {
                    mPluginStoreCreditPanel.setVisibility(View.GONE);
                }
            }
            autoSelectPaymentMethod(checkout.getCheckoutPayment());
            isShowPluginStoreCredit(true);
            isShowPaymentMethod((checkout.getGrandTotal() == 0) ? false : true);
            // hoàn thành save shipping  hiden progressbar
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_SAVE_PAYMENT) {
            Checkout checkout = (Checkout) wraper.get("save_payment");
        } else if (success && actionType == ACTION_TYPE_PLACE_ORDER) {
            Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            String payment_code = "";
            if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
                payment_code = listCheckoutPayment.get(0).getCode();
            }
//            if (!StringUtil.isNullOrEmpty(payment_code) && payment_code.equals("authorizenet_directpost")) {
//                if (wraper.get("place_order") instanceof Authorizenet) {
//                    Authorizenet authorizenet = (Authorizenet) wraper.get("place_order");
//                    doInputApprovedAuthorizenet(authorizenet);
//                } else {
//                    Order order = (Order) wraper.get("place_order");
//                    getSelectedItem().setOrderSuccess(order);
//                    isShowButtonCheckout(false);
//                    isShowSalesMenuDiscount(false);
//                    mCheckoutSuccessPanel.bindItem(order);
//                    doShowDetailSuccess(true, order);
//                    // hoàn thành place order hiden progressbar
//                    isShowLoadingDetail(false);
//                }
////                isShowButtonCheckout(false);
////                isShowSalesMenuDiscount(false);
////                mCheckoutPaymentWebviewPanel.setAuthorizenet(authorizenet);
////                mCheckoutPaymentWebviewPanel.initValue();
////                doShowCheckoutWebview(true);
//            } else {
            Order order = (Order) wraper.get("place_order");
            getSelectedItem().setOrderSuccess(order);
            isShowButtonCheckout(false);
            isShowSalesMenuDiscount(false);
            mCheckoutSuccessPanel.bindItem(order);
            doShowDetailSuccess(true, order);
            // hoàn thành place order hiden progressbar
            isShowLoadingDetail(false);
//            }
        } else if (success && actionType == ACTION_TYPE_SEND_EMAIL) {
            //Show dialog khi gửi email thành công
            showDetailOrderLoading(false);
            mCheckoutSuccessPanel.showAlertRespone(true, (String) wraper.get("send_email_response"));
        } else if (success && actionType == ACTION_TYPE_APPLY_REWARD_POINT) {
            Checkout checkout = (Checkout) wraper.get("save_reward_point");
            if (checkout.getRewardPoint() != null) {
                getSelectedItem().setRewardPoint(checkout.getRewardPoint());
                mPluginRewardPointPanel.changeBalance(checkout.getRewardPoint());
            }
            updateToTal(checkout);
            isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_CHECK_APPROVED_PAYMENT_PAYPAL) {
            String transaction_id = (String) wraper.get("paypal_transaction_id");
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            CheckoutPayment paymentPayPal = checkTypePaymenPaypal(listCheckoutPayment);
            paymentPayPal.setIsReferenceNumber(transaction_id.trim());
            wraper.put("list_payment", listCheckoutPayment);
            doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
        } else if (success && actionType == ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET) {
            boolean authorize_respone = (boolean) wraper.get("authorize_respone");
            if (authorize_respone) {
                Authorizenet authorizenet = (Authorizenet) wraper.get("place_order");
                Order order = authorizenet.getOrder();
                order.setOrderItem(getSelectedItem().getCartItem());
                getSelectedItem().setOrderSuccess(order);
                isShowButtonCheckout(false);
                isShowSalesMenuDiscount(false);
                mCheckoutSuccessPanel.bindItem(order);
                doShowDetailSuccess(true, order);
                // request ngầm invoice order
                doInputInvoiceAuthorize();
                // hoàn thành place order hiden progressbar
                isShowLoadingDetail(false);
            } else {
                // request ngầm cancel order
                doInputCancelAuthorize();
                ((CheckoutDetailPanel) mDetailView).showDialogErrorAuthozire();
                getSelectedItem().setQuoteId("");
                doInputSaveCart();
            }
        } else if (success && actionType == ACTION_TYPE_CHECK_APPROVED_PAYMENT_AUTHORIZENET_INTERATION) {
            String transaction_id = (String) wraper.get("authorize_transaction_id");
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            CheckoutPayment paymentAuthorize = listCheckoutPayment.get(0);
            removeDataCreditCard(paymentAuthorize);
            paymentAuthorize.setIsReferenceNumber(transaction_id.trim());
            wraper.put("list_payment", listCheckoutPayment);
            doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
        } else if (success && actionType == ACTION_TYPE_CHECK_APPOVED_PAYMENT_STRIPE) {
            String transaction_id = (String) wraper.get("stripe_transaction_id");
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            CheckoutPayment paymentStripe = listCheckoutPayment.get(0);
            removeDataCreditCard(paymentStripe);
            paymentStripe.setIsReferenceNumber(transaction_id.trim());
            wraper.put("list_payment", listCheckoutPayment);
            doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
        } else if (success && actionType == ACTION_TYPE_REFRESH_TOKEN_PAYPAL_HERE) {
            CheckoutPayment paymentPaypalHere = (CheckoutPayment) models[0];
            String access_token = (String) wraper.get("access_token");
            paymentPaypalHere.setAccessToken(access_token);
            actionPaypalHere(paymentPaypalHere);
        }
    }

    @Override
    public void onCancelledBackground(Exception exp, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (actionType == ACTION_TYPE_ADD_COUPON_TO_QUOTE) {
            ((CheckoutDetailPanel) mDetailView).showErrorAddCouponCode();
        } else if (actionType == ACTION_TYPE_SEND_EMAIL) {
            showDetailOrderLoading(false);
            mCheckoutSuccessPanel.showAlertRespone(true, "");
        } else {
            super.onCancelledBackground(exp, actionType, actionCode, wraper, models);
        }
        ((CheckoutListPanel) mView).showLoading(false);
        isShowLoadingDetail(false);
    }

    public void updateToTal(Checkout checkout) {
//        mPluginRewardPointPanel.resetPointValue();
        ((CheckoutService) getListService()).updateTotal(checkout);
        getSelectedItem().setGrandTotal(checkout.getGrandTotal());
        if (checkout.getRewardPoint() != null) {
            getSelectedItem().setRewardPoint(checkout.getRewardPoint());
            getSelectedItem().setRewardPointUsePointValue(checkout.getRewardPointUsePointValue());
        }
//        showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
        ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());
        ((CheckoutListPanel) mView).updateTotalPrice(checkout);
        showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
        List<CheckoutPayment> listPayment = checkout.getCheckoutPayment();
        mPaymentMethodListPanel.bindList(listPayment);
        mCheckoutAddPaymentPanel.bindList(listPayment);
        List<CheckoutPayment> listChoosePayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listChoosePayment != null) {
            listChoosePayment = new ArrayList<>();
            wraper.put("list_payment", listChoosePayment);
        }
        checkout.setRemainMoney(0);
        checkout.setRealAmount(0);
        checkout.setExchangeMoney(0);
        checkout.setCustomer(getSelectedItem().getCustomer());
        checkout.setCustomerID(getSelectedItem().getCustomerID());
        mCheckoutPaymentListPanel.bindList(listChoosePayment);
        ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
        ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
        mCheckoutPaymentListPanel.resetListPayment();
        mCheckoutPaymentListPanel.setCheckout(checkout);
        wraper.put("save_cart", checkout);
        wraper.put("save_shipping", checkout);
        autoSelectPaymentMethod(listPayment);
        isShowPaymentMethod((checkout.getGrandTotal() == 0) ? false : true);
        if (checkout.getStoreCredit() != null) {
            if (checkout.getStoreCredit().getBalance() != 0 && ConfigUtil.isEnableStoreCredit() && checkout.getGrandTotal() != 0) {
                mPluginStoreCreditPanel.bindItem(checkout.getStoreCredit());
                mPluginStoreCreditPanel.setVisibility(View.VISIBLE);
            } else {
                mPluginStoreCreditPanel.setVisibility(View.GONE);
            }
        }
    }

    /**
     * add checkout to list order
     */
    public void addNewOrder() {
        if (mDetailView.getVisibility() == View.VISIBLE) {
            onBackTohome();
        }
        Checkout checkout = ((CheckoutService) getListService()).create();
        checkout.setCustomerID(guest_checkout.getID());
        checkout.setCustomer(guest_checkout);
        checkout.setStatus(STATUS_CHECKOUT_ADD_ITEM);
        setSelectedItem(checkout);
        bindCustomer(guest_checkout);
        getSelectedItems().add(checkout);
        mItem = checkout;
        isShowButtonCheckout(true);
        isShowSalesMenuDiscount(true);
        showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
        showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
        mCartItemListController.bindList(checkout.getCartItem());
        mCartItemListController.bindParent(checkout);
        mCartItemListController.doRetrieve();
        int position = getSelectedItems().size() - 1;
        mCartOrderListPanel.setSelectPosition(position);
        mCartOrderListPanel.bindList(getSelectedItems());
        mCartOrderListPanel.scrollToPosition(position);
        ((CheckoutListPanel) mView).changeCustomerInToolBar(guest_checkout);
        updateTotalPrice();
    }

    /**
     * remove checkout to list order
     */
    public void removeOrder() {
        if (getSelectedItems().size() == 1) {
            if (mDetailView.getVisibility() == View.VISIBLE) {
                onBackTohome();
            }
            int index = getSelectedItems().indexOf(getSelectedItem());
            getSelectedItems().remove(index);
            Checkout checkout = ((CheckoutService) getListService()).create();
            checkout.setCustomerID(guest_checkout.getID());
            checkout.setCustomer(guest_checkout);
            checkout.setStatus(STATUS_CHECKOUT_ADD_ITEM);
            setSelectedItem(checkout);
            bindCustomer(guest_checkout);
            getSelectedItems().clear();
            getSelectedItems().add(checkout);
            mItem = checkout;
            mCartItemListController.bindList(checkout.getCartItem());
            mCartItemListController.bindParent(checkout);
            mCartItemListController.doRetrieve();
            showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
            showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
            isShowButtonCheckout(true);
            isShowSalesMenuDiscount(true);
            int position = getSelectedItems().size() - 1;
            mCartOrderListPanel.setSelectPosition(position);
            mCartOrderListPanel.bindList(getSelectedItems());
            mCartOrderListPanel.scrollToPosition(position);
            ((CheckoutListPanel) mView).changeCustomerInToolBar(guest_checkout);
            updateTotalPrice();
        } else {
            int index = getSelectedItems().indexOf(getSelectedItem());
            getSelectedItems().remove(index);
            if (index == getSelectedItems().size()) {
                index = index - 1;
            }
            isShowButtonCheckout(true);
            isShowSalesMenuDiscount(true);
            Checkout checkout = getSelectedItems().get(index);
            setSelectedItem(checkout);
            doShowDetailSuccess(false);
            showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
            showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
            if (checkout.getStatus() != STATUS_CHECKOUT_PROCESSING) {
                showSalesShipping();
                showActionButtonCheckout();
                showSaleMenu(false);
                doShowDetailPanel(false);
            } else {
                doInputSaveCart();
            }
            if (checkout.getOrderSuccess() != null) {
                isShowButtonCheckout(false);
                isShowSalesMenuDiscount(false);
                doShowDetailSuccess(true, checkout.getOrderSuccess());
            }
            mItem = checkout;
            mCartItemListController.bindList(checkout.getCartItem());
            mCartItemListController.bindParent(checkout);
            mCartItemListController.doRetrieve();
            mCartOrderListPanel.setSelectPosition(index);
            mCartOrderListPanel.bindList(getSelectedItems());
            mCartOrderListPanel.scrollToPosition(index);
            ((CheckoutListPanel) mView).changeCustomerInToolBar(checkout.getCustomer());
            bindCustomer(checkout.getCustomer());
            updateTotalPrice();
        }
    }

    /**
     * kiểm tra item trong cart
     *
     * @return
     */
    public boolean checkItemInOrder() {
        if (getSelectedItems().size() == 1) {
            if (getSelectedItems().get(0).getCartItem().size() <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Thay đổi shipping address
     */
    public void changeShippingAddress(CustomerAddress customerAddress) {
        List<CustomerAddress> listAddress = getSelectedItem().getCustomer().getAddress();
        if (listAddress.size() > 1) {
            for (CustomerAddress address : listAddress) {
                if (address.getID().equals(customerAddress.getID())) {
                    listAddress.remove(address);
                    listAddress.add(0, address);
                    break;
                }
            }
            doInputSaveCart();
        }
    }

    /**
     * select order from list order
     *
     * @param checkout
     */
    public void selectOrder(Checkout checkout) {
        setSelectedItem(checkout);
        mItem = checkout;
        mCartItemListController.bindList(checkout.getCartItem());
        mCartItemListController.bindParent(checkout);
        mCartItemListController.doRetrieve();
        mCartOrderListPanel.bindList(getSelectedItems());
        bindCustomer(checkout.getCustomer());
        ((CheckoutListPanel) mView).changeCustomerInToolBar(checkout.getCustomer());
        updateTotalPrice();
        showButtonRemoveDiscount(checkDiscount(checkout) ? true : false);
        showButtonDiscount(checkout.getGrandTotal() != 0 && checkListCartItem() ? true : false);
        if (checkout.getStatus() == STATUS_CHECKOUT_PROCESSING) {
            if (checkout.getOrderSuccess() != null) {
                mCheckoutSuccessPanel.bindItem(checkout.getOrderSuccess());
                isShowButtonCheckout(false);
                isShowSalesMenuDiscount(false);
                doShowDetailSuccess(true, checkout.getOrderSuccess());
            } else {
                isShowButtonCheckout(true);
                isShowSalesMenuDiscount(true);
                doShowDetailSuccess(false);
                mCheckoutPaymentCreditCardPanel.clearDataForm();
                ((CheckoutDetailPanel) mDetailView).clearNote();
                ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(true);
                ((CheckoutDetailPanel) mDetailView).showShippingAdrress(false);
                ((CheckoutDetailPanel) mDetailView).setPickAtStoreDefault();
                ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(false);
                ((CheckoutDetailPanel) mDetailView).isCheckCreateInvoice(false);
                ((CheckoutDetailPanel) mDetailView).isCheckCreateShip(false);
                ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
                doInputSaveCart();
            }
            showSaleMenu(false);
        } else {
            onBackTohome();
        }
    }

    /**
     * khi switch pick at store load lại shipping
     */
    public void changePickAtStoreAndReloadShipping() {
        Checkout checkout = (Checkout) wraper.get("save_cart");
        if (checkout != null) {
            List<CheckoutShipping> listShipping = checkout.getCheckoutShipping();
            if (listShipping != null && listShipping.size() > 0) {
                bindDataToShippingMethodList(listShipping);
                autoSelectShipping(listShipping);
            }
        }
    }

    /**
     * bind data vào spinner shipping
     *
     * @param listShipping
     */
    private void bindDataToShippingMethodList(List<CheckoutShipping> listShipping) {
        if (!((CheckoutDetailPanel) mDetailView).getPickAtStore()) {
            List<CheckoutShipping> nListShipping = listShipping;
            for (CheckoutShipping shipping : nListShipping) {
                if (shipping.getCode().equals(PICK_AT_STORE_CODE)) {
                    nListShipping.remove(shipping);
                }
            }
            ((CheckoutDetailPanel) mDetailView).setShippingDataSet(nListShipping);
        } else {
            ((CheckoutDetailPanel) mDetailView).setShippingDataSet(listShipping);
        }
    }

    /**
     * Tự động chọn shipping default
     *
     * @param listShipping
     */
    private void autoSelectShipping(List<CheckoutShipping> listShipping) {
        if (!((CheckoutDetailPanel) mDetailView).getPickAtStore()) {
            if (listShipping != null && listShipping.size() > 0) {
//                if (listShipping.size() == 1) {
//                    ((CheckoutDetailPanel) mDetailView).getShippingMethod();
//                } else {
                CheckoutShipping shipping = checkListShippingMethodDefault(0, listShipping);
                if (shipping != null) {
                    ((CheckoutDetailPanel) mDetailView).selectDefaultShippingMethod(shipping);
                } else {
                    ((CheckoutDetailPanel) mDetailView).selectDefaultShippingMethod(null);
                }
//                }
            }
        } else {
            if (listShipping != null && listShipping.size() > 0) {
                if (listShipping.size() == 1) {
                    ((CheckoutDetailPanel) mDetailView).getShippingMethod();
                } else {
                    CheckoutShipping shipping = checkListShippingMethodDefault(1, listShipping);
                    if (shipping != null) {
                        ((CheckoutDetailPanel) mDetailView).selectDefaultShippingMethod(shipping);
                    } else {
                        ((CheckoutDetailPanel) mDetailView).selectDefaultShippingMethod(listShipping.get(0));
                    }
                }
            }
        }
    }

    /**
     * tự động chọn payment default
     *
     * @param listPayment
     */
    private void autoSelectPaymentMethod(List<CheckoutPayment> listPayment) {
        if (listPayment != null && listPayment.size() > 0) {
            CheckoutPayment paymentDefault = null;
            if (getSelectedItem().getGrandTotal() == 0) {
                paymentDefault = checkListPaymentNoInformation(listPayment);
            } else {
                paymentDefault = checkListPaymentDefault(listPayment);
            }
            if (paymentDefault != null) {
                onAddPaymentMethod(paymentDefault);
            } else {
                mCheckoutPaymentListPanel.updateTotal(listPayment);
            }
        }
    }

    /**
     * tham chiếu context từ sales activity
     *
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    public void setCustomerAddressService(CustomerAddressService mCustomerAddressService) {
        this.mCustomerAddressService = mCustomerAddressService;
    }

    /**
     * Tham chiếu sang cart item controller
     *
     * @param controller
     */
    public void setCartItemListController(CartItemListController controller) {
        mCartItemListController = controller;
    }

    public Customer getGuestCheckout() {
        return guest_checkout;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * Cập nhật tổng giá
     */
    public void updateTotalPrice() {
        if (mView != null && mItem != null && (mView instanceof CheckoutListPanel))
            ((CheckoutListPanel) mView).updateTotalPrice(mItem);
    }

    /**
     * Cập nhật tổng giá theo observe mỗi khi có state từ cartitem
     *
     * @param state
     */
    public void updateTotalPrice(State state) {
        updateTotalPrice();
    }

    @Override
    public void doShowDetailPanel(boolean show) {
        super.doShowDetailPanel(show);
        enableRemoveCartItem(show);
        if (mProductListController != null) mProductListController.doShowListPanel(!show);
    }

    public void doShowDetailSuccess(boolean show, Order order) {
        mCheckoutSuccessPanel.setVisibility(show ? View.VISIBLE : View.GONE);
        if (show && order != null) mCheckoutSuccessPanel.fillEmailCustomer(order);
    }

    public void doShowCheckoutWebview(boolean show) {
        mCheckoutPaymentWebviewPanel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void doShowDetailSuccess(boolean show) {
        mCheckoutSuccessPanel.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setPaymentMethodListPanel(PaymentMethodListPanel panel) {
        mPaymentMethodListPanel = panel;
    }

    @Override
    public void notifyState(State state) {
        String strS = state.getStateCode();
    }

    /**
     * Khi đánh dấu thanh toán 1 phần
     */
    public void onMarkAsPartial() {
        doInsert(getSelectedItem());
    }

    public void addPaymentFromDialog(CheckoutPayment method) {
        onAddPaymentMethod(method);
        ((CheckoutDetailPanel) mDetailView).dismissDialogAddPayment();
    }

    /**
     * add thêm payment trongvaof checkout
     *
     * @param method
     */
    public void onAddPaymentMethod(CheckoutPayment method) {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
        if (method.getType().equals("1")) {
            if (listPayment != null) {
                listPayment = new ArrayList<>();
                wraper.put("list_payment", listPayment);
            }
            updateMoneyTotal(true, 0);
            float total = checkout.getGrandTotal();
            method.setAmount(total);
            method.setBaseAmount(total);
            method.setRealAmount(total);
            method.setBaseRealAmount(total);
            mCheckoutPaymentCreditCardPanel.setCardTypeDataSet(configCCTypes);
            mCheckoutPaymentCreditCardPanel.setCardMonthDataSet(configMonths);
            mCheckoutPaymentCreditCardPanel.setCardYearDataSet(configCCYears);

            if (listPayment == null) {
                listPayment = new ArrayList<>();
            }

            listPayment.add(method);
            wraper.put("list_payment", listPayment);
            isShowPluginStoreCredit(false);
            ((CheckoutDetailPanel) mDetailView).updateTitlePaymentCreditCard(method.getTitle());
            //Felix edit 03/05/2017
            if (method.getCode().equals("authorizenet_directpost")) {
                isEnableCreateInvoice(false);
            } else {
                isEnableCreateInvoice(true);
            }
            //End Felix edit 03/05/2017
            ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(true);
        } else {
            checkIsPayLater(method, listPayment);
            float total = 0;
            if (method.isPaylater().equals("1")) {
                if (checkout.getRemainMoney() > 0) {
                    isEnableButtonAddPayment(true);
                } else {
                    isEnableButtonAddPayment(false);
                }
            } else {
                float remain_money = checkout.getRemainMoney();
                if (remain_money > 0) {
                    isEnableButtonAddPayment(true);
                    if (method.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                        if (method.getAmount() == remain_money) {
                            total = remain_money;
                        } else {
                            total = method.getAmount();
                            float money = remain_money - method.getAmount();
                            checkout.setRemainMoney(money);
                        }
                    } else {
                        total = remain_money;
                    }
                } else {
                    total = checkout.getGrandTotal();
                    isEnableButtonAddPayment(false);
                }
            }

            CheckoutPayment paymentStoreCredit = getPaymentStoreCredit(listPayment);
            if (paymentStoreCredit == null) {
                method.setAmount(total);
                method.setBaseAmount(total);
                method.setRealAmount(total);
                method.setBaseRealAmount(total);

                if (listPayment == null) {
                    listPayment = new ArrayList<>();
                }
                listPayment.add(method);
            } else {
                if (method.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                    paymentStoreCredit.setAmount(total);
                    paymentStoreCredit.setBaseAmount(total);
                    paymentStoreCredit.setRealAmount(total);
                    paymentStoreCredit.setBaseRealAmount(total);
                } else {
                    method.setAmount(total);
                    method.setBaseAmount(total);
                    method.setRealAmount(total);
                    method.setBaseRealAmount(total);
                }
                if (listPayment == null) {
                    listPayment = new ArrayList<>();
                }
                listPayment.add(method);
            }
            isEnableCreateInvoice(true);
            wraper.put("list_payment", listPayment);
            mCheckoutPaymentListPanel.bindList(listPayment);
            mCheckoutPaymentListPanel.updateTotal(listPayment);
            if (method.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                isShowPluginStoreCredit(false);
            }
            ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPayment();
        }

        ((CheckoutDetailPanel) mDetailView).hideCheckPaymenrRequired();
    }

    /**
     * xóa 1 payment method  checkout
     */
    public void onRemovePaymentMethod() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
        if (listPayment.size() == 0) {
            ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());
            isEnableButtonAddPayment(false);
            isShowPluginStoreCredit(true);
            ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
        }
    }

    /**
     * xóa payment creditcard method  checkout
     */
    public void onRemovePaymentCreditCard() {
        Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listPayment != null) {
            listPayment.clear();
        } else {
            listPayment = new ArrayList<>();
        }
        wraper.put("list_payment", listPayment);
        ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
        isShowPluginStoreCredit(true);
        ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
    }

    /**
     * ẩn button checkout và hold order
     */
    public void showActionButtonCheckout() {
        if (mView != null && (mView instanceof CheckoutListPanel)) {
            // show button checkout và hold order
            ((CheckoutListPanel) mView).changeActionButton(false);
        }
    }

    /**
     * ẩn shipping và total in cart list
     */
    public void showSalesShipping() {
        if (mView != null && (mView instanceof CheckoutListPanel)) {
            // show button checkout và hold order
            ((CheckoutListPanel) mView).showSalesShipping(false);
        }
    }

    public void isShowLoadingDetail(boolean iShow) {
        ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(iShow);
    }

    public void isShowPaymentMethod(boolean isShow) {
//        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(true);
        ((CheckoutDetailPanel) mDetailView).isShowPaymentMethod(isShow);
    }

    public void showSaleMenu(boolean isShow) {
        ((CheckoutListPanel) mView).showSalesMenuToCheckout(isShow);
    }

    // khi thay đổi value từng payment update giá trị money
    public void updateMoneyTotal(boolean type, float totalPrice) {
        ((CheckoutDetailPanel) mDetailView).updateMoneyTotal(type, totalPrice);
    }

    /**
     * ẩn hoặc hiện button add payment
     *
     * @param enable
     */
    public void isEnableButtonAddPayment(boolean enable) {
        ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(enable);
        isShowPluginStoreCredit(enable);
    }

    /**
     * ẩn hoặc hiện button create invoice
     *
     * @param enable
     */
    public void isEnableCreateInvoice(boolean enable) {
        ((CheckoutDetailPanel) mDetailView).isCheckCreateInvoice(enable);
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(enable);
    }

    /**
     * add 1 order mới vào list order
     */
    public void actionNewOrder() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listPayment != null) {
            listPayment.clear();
        } else {
            listPayment = new ArrayList<>();
        }
        wraper.put("list_payment", listPayment);
        mCheckoutPaymentListPanel.bindList(listPayment);
        mCheckoutPaymentListPanel.updateTotal(listPayment);
        mCheckoutPaymentCreditCardPanel.clearDataForm();
        ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(false);
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
        ((CheckoutDetailPanel) mDetailView).isCheckCreateInvoice(false);
        ((CheckoutDetailPanel) mDetailView).isCheckCreateShip(false);
        ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
        ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
        showSaleMenu(true);
        removeOrder();
        if (getSelectedItems().size() > 1) {
            addNewOrder();
        }
        onBackTohome();
        doShowDetailSuccess(false);
    }

    /**
     * back trở về trang home
     */
    public void onBackTohome() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listPayment != null) {
            listPayment.clear();
        } else {
            listPayment = new ArrayList<>();
        }
        getSelectedItem().setStatus(STATUS_CHECKOUT_ADD_ITEM);
        wraper.put("list_payment", listPayment);
        mCheckoutPaymentListPanel.bindList(listPayment);
        mCheckoutPaymentListPanel.updateTotal(listPayment);
        mCheckoutPaymentCreditCardPanel.clearDataForm();
//        mPluginRewardPointPanel.resetPointValue();
        isShowButtonCheckout(true);
        isShowSalesMenuDiscount(true);
        ((CheckoutDetailPanel) mDetailView).showPickAtStore(true);
        ((CheckoutDetailPanel) mDetailView).isEnableCreatShip(true);
        ((CheckoutDetailPanel) mDetailView).setPickAtStoreDefault();
        ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(false);
        ((CheckoutDetailPanel) mDetailView).isCheckCreateInvoice(false);
        ((CheckoutDetailPanel) mDetailView).isCheckCreateShip(false);
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
        ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
        ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
        ((CheckoutDetailPanel) mDetailView).showShippingAdrress(false);
        ((CheckoutDetailPanel) mDetailView).clearNote();
        mPluginGiftCardPanel.resetListGiftCard();
        showSaleMenu(true);
        showSalesShipping();
        showActionButtonCheckout();
        doShowDetailSuccess(false);
        doShowDetailPanel(false);
    }

    /**
     * checkdata nếu không có shipping thì dùng data khi save cart
     *
     * @param checkout
     */
    private Checkout checkDataCheckout(Checkout checkout) {
        if (checkout == null) {
            return (Checkout) wraper.get("save_cart");
        }

        return checkout;
    }

    /**
     * check list payment nếu còn thiếu money thì cho phép add thêm payment
     *
     * @return
     */
    public boolean checkListPaymentDialog() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
        List<CheckoutPayment> listAllPayment = checkout.getCheckoutPayment();
        List<CheckoutPayment> listPaymentDialog = new ArrayList<>();
        listPaymentDialog.addAll(listAllPayment);
        boolean isPaymentSDK = checkPaymentSDK(listPayment);
        for (CheckoutPayment checkoutPayment : listAllPayment) {
            if (checkoutPayment.getType().equals("1")) {
                listPaymentDialog.remove(checkoutPayment);
            }
            if (isPaymentSDK) {
                if (checkoutPayment.getType().equals("2")) {
                    listPaymentDialog.remove(checkoutPayment);
                }
            }
            for (CheckoutPayment paymentMethod : listPayment) {
                if (checkoutPayment.getCode().equals(paymentMethod.getCode())) {
                    listPaymentDialog.remove(checkoutPayment);
                }
            }
        }
        if (listPaymentDialog.size() > 0) {
            mCheckoutAddPaymentPanel.bindList(listPaymentDialog);
            return true;
        }
        return false;
    }

    private boolean checkPaymentSDK(List<CheckoutPayment> listPayment) {
        boolean isPaymentSDK = false;
        for (CheckoutPayment payment : listPayment) {
            if (payment.getType().equals("2")) {
                isPaymentSDK = true;
            }
        }
        return isPaymentSDK;
    }

    /**
     * kiểm tra và chọn shipping default
     *
     * @param type
     * @param listShipping
     * @return
     */
    public CheckoutShipping checkListShippingMethodDefault(int type, List<CheckoutShipping> listShipping) {
        for (CheckoutShipping shipping : listShipping) {
            if (type == 0) {
                if (shipping.getCode().equals(PICK_AT_STORE_CODE)) {
                    return shipping;
                }
            } else {
                if (shipping.getIsDefault().equals("1")) {
                    return shipping;
                }
            }
        }
        return null;
    }

    /**
     * kiểm tra và chọn payment default
     *
     * @param listPayment
     * @return
     */
    public CheckoutPayment checkListPaymentDefault(List<CheckoutPayment> listPayment) {
        for (CheckoutPayment payment : listPayment) {
            if (payment.getIsDefault().equals("1")) {
                return payment;
            }
        }
        return null;
    }

    /**
     * nếu grand total = 0 chọn payment type = 0 add list
     *
     * @param listPayment
     * @return
     */
    public CheckoutPayment checkListPaymentNoInformation(List<CheckoutPayment> listPayment) {
        for (CheckoutPayment payment : listPayment) {
            if (payment.getType().equals("0")) {
                return payment;
            }
        }
        return null;
    }

    /**
     * kiểm tra nếu payment truyền vào ko phải pay later thì remove all payment is_pay_later
     *
     * @param checkoutPayment
     * @param listPayment
     */
    public void checkIsPayLater(CheckoutPayment checkoutPayment, List<CheckoutPayment> listPayment) {
        if (!checkoutPayment.isPaylater().equals("1")) {
            if (listPayment != null && listPayment.size() > 0) {
                for (int i = 0; i < listPayment.size(); i++) {
                    CheckoutPayment payment = listPayment.get(i);
                    if (payment.isPaylater().equals("1")) {
                        listPayment.remove(payment);
                        i--;
                    }
                }
            }
        }
    }

    /**
     * check payment store credit
     *
     * @param listPayment
     * @return
     */
    public CheckoutPayment getPaymentStoreCredit(List<CheckoutPayment> listPayment) {
        if (listPayment != null) {
            for (CheckoutPayment payment : listPayment) {
                if (payment.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                    return payment;
                }
            }
        }
        return null;
    }

    /**
     * check payment paypal
     *
     * @param listCheckoutPayment
     * @return
     */
    public CheckoutPayment checkTypePaymenPaypal(List<CheckoutPayment> listCheckoutPayment) {
        for (CheckoutPayment payment : listCheckoutPayment) {
            if (payment.getType().equals("2") && payment.getCode().equals("paypal_integration")) {
                return payment;
            }
        }
        return null;
    }

    /**
     * Xóa thông tin thẻ khách hàng trước khi place order
     *
     * @param paymentCreditCard
     */
    private void removeDataCreditCard(CheckoutPayment paymentCreditCard) {
        paymentCreditCard.setCCType("");
        paymentCreditCard.setCCNumber("");
        paymentCreditCard.setCCExpMonth("");
        paymentCreditCard.setCCExpYear("");
        paymentCreditCard.setCID("");
    }

    /**
     * action processing paypal here
     *
     * @param paymentPayPalHere
     */
    public void actionPaypalHere(final CheckoutPayment paymentPayPalHere) {
        String enviroment = PayPalHereSDK.Live;
        if (paymentPayPalHere.getIsSandbox().equals("1")) {
            enviroment = PayPalHereSDK.Sandbox;
        }

        if (PayPalHereSDK.isInitialized()) {
            Intent readerConnectionIntent = new Intent(getMagestoreContext().getActivity(), MultiReaderConnectionActivity.class);
            readerConnectionIntent.putExtra("amount", paymentPayPalHere.getBaseAmount());
            readerConnectionIntent.putExtra("quote_id", getSelectedItem().getQuoteId());
            getMagestoreContext().getActivity().startActivityForResult(readerConnectionIntent, START_ACTIVITY_MUTIREADER);
        } else {
            PayPalHereSDKWrapper.getInstance().initializeSDK(getMagestoreContext().getActivity(), enviroment, paymentPayPalHere.getAccessToken(), new PayPalHereSDKWrapperCallbacks() {
                @Override
                public void onErrorWhileSettingAccessTokenToSDK(boolean errorToken) {
                    if (!errorToken) {
                        isShowLoadingDetail(false);
                        Log.d(CheckoutListController.class.getName(), "PayPalHere SDK initialize onErrorWhileSettingAccessTokenToSDK");
                        new AlertDialog.Builder(getMagestoreContext().getActivity())
                                .setMessage(R.string.paypal_here_init_error)
                                .setPositiveButton(R.string.paypal_here_try_again, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isShowLoadingDetail(true);
                                        actionPaypalHere(paymentPayPalHere);
                                    }
                                })
                                .setNegativeButton(R.string.no, null)
                                .show();
                    }
                }

                @Override
                public void onSuccessfulCompletionOfSettingAccessTokenToSDK() {
                    Log.d(CheckoutListController.class.getName(), "PayPalHere SDK initialize onSuccessfulCompletionOfSettingAccessTokenToSDK");
                    Intent readerConnectionIntent = new Intent(getMagestoreContext().getActivity(), MultiReaderConnectionActivity.class);
                    readerConnectionIntent.putExtra("amount", paymentPayPalHere.getBaseAmount());
                    readerConnectionIntent.putExtra("quote_id", getSelectedItem().getQuoteId());
                    getMagestoreContext().getActivity().startActivityForResult(readerConnectionIntent, START_ACTIVITY_MUTIREADER);
                }

                @Override
                public void onErrorAccessToken() {
                    doInputRefreshTokenPaypalHere(paymentPayPalHere);
                }
            });
        }
    }

    /**
     * check payment paypal here
     *
     * @param listCheckoutPayment
     * @return
     */
    public CheckoutPayment checkTypePaymentPaypalhere(List<CheckoutPayment> listCheckoutPayment) {
        for (CheckoutPayment payment : listCheckoutPayment) {
            if (payment.getType().equals("2") && payment.getCode().equals("paypal_here")) {
                return payment;
            }
        }
        return null;
    }

    /**
     * 0
     * check list cart xem còn sản phẩm hay không
     *
     * @return
     */
    public boolean checkListCartItem() {
        if (getSelectedItem().getCartItem() != null && getSelectedItem().getCartItem().size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * check discount có hay ko
     *
     * @param checkout
     * @return
     */
    public boolean checkDiscount(Checkout checkout) {
        if (checkout.getDiscountTotal() != 0) {
            return true;
        }
        return false;
    }

    public void changeCustomerShippingAdrress(boolean isChange) {
        if (checkCustomerID(getSelectedItem().getCustomer(), guest_checkout)) {
            return;
        }
        if (isChange) {

        } else {

        }
    }

    public boolean checkCustomerID(Customer customer, Customer guest_customer) {
        return ((CheckoutService) getListService()).checkCustomerID(customer, guest_customer);
    }

    public void checkShowRemoveDiscount() {
        if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE) {
            showButtonRemoveDiscount(checkDiscount(checkDataCheckout((Checkout) wraper.get("save_shipping"))));
        }
    }

    public List<CheckoutPayment> getListChoosePayment() {
        return (List<CheckoutPayment>) wraper.get("list_payment");
    }

    /**
     * ẩn hiện button remove discount
     *
     * @param isShow
     */
    public void showButtonRemoveDiscount(boolean isShow) {
        ((CheckoutListPanel) mView).showButtonRemoveDiscount(isShow);
    }

    /**
     * ẩn hiện button discount
     *
     * @param isShow
     */
    public void showButtonDiscount(boolean isShow) {
        if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE) {
            if (!isShow) {
                ((CheckoutListPanel) mView).showButtonDiscount(isShow);
            }
        } else {
            ((CheckoutListPanel) mView).showButtonDiscount(isShow);
        }
    }

    /**
     * show dialog error
     *
     * @param message
     */
    public void showDialogError(String message) {
        ((CheckoutDetailPanel) mDetailView).showDialogError(message);
    }

    /**
     * khởi tạo payment method
     *
     * @return
     */
    public CheckoutPayment createPaymentMethod() {
        return ((CheckoutService) getListService()).createPaymentMethod();
    }

    /**
     * khởi tạo store credit
     * @return
     */
    public StoreCredit createStoreCredit(){
        return ((CheckoutService) getListService()).createStoreCredit();
    }

    /**
     * khởi tạo credit card
     *
     * @return
     */
    public CreditCard createCreditCard() {
        return ((CheckoutService) getListService()).createCreditCard();
    }

    /**
     * khởi tạo saveQuoteData
     *
     * @return
     */
    public SaveQuoteParam createSaveQuoteParam() {
        return ((CheckoutService) getListService()).createSaveQuoteParam();
    }

    /**
     * khởi tạo quoteAddCouponParam
     *
     * @return
     */
    public QuoteAddCouponParam createQuoteAddCouponParam() {
        return ((CheckoutService) getListService()).createQuoteAddCouponParam();
    }

    /**
     * add một address mới cho customer
     */
    public void addNewAddress() {
        ((CheckoutListPanel) mView).checkoutAddNewAddress();
    }

    /**
     * reset position của list address về 0
     */
    public void resetPositionAddress() {
        mCheckoutAddressListPanel.setSelectPos(0);
    }

    public RewardPoint createRewardPoint() {
        return pluginsService.createRewardPoint();
    }

    /**
     * cập nhật total
     */
    public void updateTotal() {
        Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
        ((CheckoutService) getListService()).updateTotal(checkout);
        ((CheckoutListPanel) getView()).updateTotalPrice(getSelectedItem());
    }

    public void updateTotalWithDeleteCartItem(Checkout checkout) {
        ((CheckoutService) getListService()).updateTotal(checkout);
        ((CheckoutListPanel) getView()).updateTotalPrice(getSelectedItem());
    }

    /**
     * ẩn hiện loading cart list
     *
     * @param isShow
     */
    public void isShowLoadingList(boolean isShow) {
        ((CheckoutListPanel) mView).showLoading(isShow);
    }

    /**
     * ẩn hiện button checkout
     *
     * @param isShow
     */
    public void isShowButtonCheckout(boolean isShow) {
        ((CheckoutListPanel) mView).showButtonCheckout(isShow);
    }

    /**
     * ẩn hiện button sales menu discount
     *
     * @param isShow
     */
    public void isShowSalesMenuDiscount(boolean isShow) {
        ((CheckoutListPanel) mView).showSalesMenuDiscount(isShow);
    }

    /**
     * toggle sales menu
     *
     * @param isShow
     */
    public void isShowSalesMenuToggle(boolean isShow) {
        ((CheckoutListPanel) mView).showSalesMenuToggle(isShow);
    }

    public void changeTitlePlaceOrder(boolean ischange) {
        ((CheckoutListPanel) mView).changeTitlePlaceOrder(ischange);
    }

    public void isShowPluginStoreCredit(boolean isShow) {
        Checkout checkout = checkDataCheckout((Checkout) wraper.get("save_shipping"));
        if (checkout.getStoreCredit() != null && checkout.getStoreCredit().getBalance() != 0 && checkout.getGrandTotal() != 0) {
            mPluginStoreCreditPanel.setVisibility(isShow ? View.VISIBLE : View.GONE);
        } else {
            mPluginStoreCreditPanel.setVisibility(View.GONE);
        }
    }

    public void updateMaxAmountStoreCredit(float total) {
        mPluginStoreCreditPanel.updateMaxAmountStoreCredit(total);
    }

    public void removePaymentStoreCredit() {
        mCheckoutPaymentListPanel.removePaymentStoreCredit();
    }

    /**
     * disable hoặc enable remove item cart
     *
     * @param isEnable
     */
    public void enableRemoveCartItem(boolean isEnable) {
        // báo cho các observ khác về việc enable thay đổi cart item
        String keyObserv = STATE_ENABLE_CHANGE_CART_ITEM;
        if (!isEnable) {
            keyObserv = STATE_ENABLE_CHANGE_CART_ITEM;
        } else {
            keyObserv = STATE_DISABLE_CHANGE_CART_ITEM;
        }
        GenericState<CheckoutListController> state = new GenericState<CheckoutListController>(this, keyObserv);
        if (getSubject() != null) getSubject().setState(state);
    }

    public float getMaximumDiscount() {
        return maximum_discount;
    }

    /**
     * Place thực hiện order
     */
    public void onPlaceOrder() {
        doInsert(getSelectedItem());
    }

    public static final String STATE_ADD_CUSTOM_SALE = "ON_ADD_CUSTOM_SALE";

    public void onShowCustomSale() {
        // báo cho các observ khác về việc bind item
        GenericState<CheckoutListController> state = new GenericState<CheckoutListController>(this, STATE_ADD_CUSTOM_SALE);
        if (getSubject() != null) getSubject().setState(state);
    }

    public static final String STATE_ADD_CUSTOM_DISCOUNT = "ON_ADD_CUSTOM_DISCOUNT";

    public void onShowCustomDiscount() {
        // báo cho các observ khác về việc bind item
        GenericState<CheckoutListController> state = new GenericState<CheckoutListController>(this, STATE_ADD_CUSTOM_DISCOUNT);
        if (getSubject() != null) getSubject().setState(state);
    }

    public void setCartOrderListPanel(CartOrderListPanel mCartOrderListPanel) {
        this.mCartOrderListPanel = mCartOrderListPanel;
    }

    public void setCheckoutShippingListPanel(CheckoutShippingListPanel panel) {
        mCheckoutShippingListPanel = panel;
    }

    public void setCheckoutPaymentListPanel(CheckoutPaymentListPanel panel) {
        mCheckoutPaymentListPanel = panel;
    }

    public void setProductListController(ProductListController controller) {
        mProductListController = controller;
    }

    public void setCheckoutAddPaymentPanel(CheckoutAddPaymentPanel mCheckoutAddPaymentPanel) {
        this.mCheckoutAddPaymentPanel = mCheckoutAddPaymentPanel;
    }

    public void setCheckoutAddressListPanel(CheckoutAddressListPanel mCheckoutAddressListPanel) {
        this.mCheckoutAddressListPanel = mCheckoutAddressListPanel;
    }

    public void setCheckoutSuccessPanel(CheckoutSuccessPanel mCheckoutSuccessPanel) {
        this.mCheckoutSuccessPanel = mCheckoutSuccessPanel;
    }

    public void setCheckoutPaymentWebviewPanel(CheckoutPaymentWebviewPanel mCheckoutPaymentWebviewPanel) {
        this.mCheckoutPaymentWebviewPanel = mCheckoutPaymentWebviewPanel;
    }

    public void setCheckoutPaymentCreditCardPanel(CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel) {
        this.mCheckoutPaymentCreditCardPanel = mCheckoutPaymentCreditCardPanel;
    }

    public void setCartItemDetailPanel(CartItemDetailPanel mCartItemDetailPanel) {
        this.mCartItemDetailPanel = mCartItemDetailPanel;
    }

    public void setPluginRewardPointPanel(PluginRewardPointPanel mPluginRewardPointPanel) {
        this.mPluginRewardPointPanel = mPluginRewardPointPanel;
    }

    public void setPluginStoreCreditPanel(PluginStoreCreditPanel mPluginStoreCreditPanel) {
        this.mPluginStoreCreditPanel = mPluginStoreCreditPanel;
    }

    public void setPluginGiftCardPanel(PluginGiftCardPanel mPluginGiftCardPanel) {
        this.mPluginGiftCardPanel = mPluginGiftCardPanel;
    }

    public void setPluginsService(PluginsService pluginsService) {
        this.pluginsService = pluginsService;
    }

    /**
     * Xử lý Re-order
     *
     * @param order
     */
    public static final String STATE_CODE_REORDER = "STATE_CODE_REORDER";

    public void reOrder(Order order) {
        // khởi tạo 1 check out
        addNewOrder();

        // báo state re-order cho các controller khác
        GenericState<CheckoutListController> state = new GenericState<CheckoutListController>(this, STATE_CODE_REORDER);
        state.setTag(STATE_CODE_REORDER, order);
        if (getSubject() != null) getSubject().setState(state);
    }
}
