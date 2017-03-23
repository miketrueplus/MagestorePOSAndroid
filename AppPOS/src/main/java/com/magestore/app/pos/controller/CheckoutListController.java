package com.magestore.app.pos.controller;

import android.content.Context;
import android.view.View;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.pos.model.checkout.PosCheckoutPayment;
import com.magestore.app.pos.panel.CartItemDetailPanel;
import com.magestore.app.pos.panel.CartOrderListPanel;
import com.magestore.app.pos.panel.CheckoutAddPaymentPanel;
import com.magestore.app.pos.panel.CheckoutAddressListPanel;
import com.magestore.app.pos.panel.CheckoutDetailPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentCreditCardPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.CheckoutSuccessPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.util.DataUtil;

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
    public static final String STATE_ON_ADD_PAYMENT = "STATE_ON_ADD_PAYMENT";
    public static final String STATE_ON_PLACE_ORDER = "STATE_ON_PLACE_ORDER";
    public static final String STATE_ON_MARK_AS_PARTIAL = "STATE_ON_MARK_AS_PARTIAL";

    static final int ACTION_TYPE_USER_GUEST = 0;
    static final int ACTION_TYPE_UPDATE_ADDRESS = 1;
    static final int ACTION_TYPE_DELETE_ADDRESS = 2;
    static final int ACTION_TYPE_NEW_ADDRESS = 3;
    static final int ACTION_TYPE_SAVE_CART = 4;
    static final int ACTION_TYPE_SAVE_SHIPPING = 5;
    static final int ACTION_TYPE_SAVE_PAYMENT = 6;
    static final int ACTION_TYPE_PLACE_ORDER = 7;

    static final int STATUS_CHECKOUT_ADD_ITEM = 0;
    static final int STATUS_CHECKOUT_PROCESSING = 1;

    static final String PICK_AT_STORE_CODE = "webpos_shipping_storepickup";

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
    CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel;
    CartItemDetailPanel mCartItemDetailPanel;
    Context context;
    Customer guest_checkout;
    Currency currency;
    Map<String, String> configCCTypes;
    List<String> configMonths;
    Map<String, String> configCCYears;
    CustomerAddressService mCustomerAddressService;

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
            if (((CheckoutDetailPanel) mDetailView).getVisibility() == View.VISIBLE) {
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
            ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(true);
            // ẩn button checkout và hold order
            ((CheckoutListPanel) mView).changeActionButton(true);
            // show detail panel
            doShowDetailPanel(true);
//        binCartItem();
//            wraper.put("quote_id", );
            String store_id = DataUtil.getDataStringToPreferences(context, DataUtil.STORE_ID);
            checkout.setStoreId(store_id);
            wraper.put("quote_id", checkout.getQuoteId());
            doAction(ACTION_TYPE_SAVE_CART, null, wraper, checkout);
        } else {
            ((CheckoutDetailPanel) mDetailView).showNotifiAddItems();
            return;
        }
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
                if (!mCheckoutPaymentCreditCardPanel.checkRequiedCard()) {
                    return;
                }

                CheckoutPayment payment = mCheckoutPaymentCreditCardPanel.bind2Item();
                PosCheckoutPayment.AdditionalData additionalData = paymentCreditCard.createAdditionalData();
                paymentCreditCard.setAdditionalData(additionalData);
                paymentCreditCard.setCCOwner(payment.getCCOwner());
                paymentCreditCard.setType(payment.getCCType());
                paymentCreditCard.setCCNumber(payment.getCCNumber());
                paymentCreditCard.setCCExpMonth(payment.getCCExpMonth());
                paymentCreditCard.setCCExpYear(payment.getCCExpYear());
                paymentCreditCard.setCID(payment.getCID());

                doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
                ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(true);
            } else {
                doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
                ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(true);
            }
        } else {
            // hiển thị thông báo chọn payment
            ((CheckoutDetailPanel) mDetailView).showNotifiSelectPayment();
        }
    }

    /**
     * lấy customer guest từ config và gán vào checkout
     */
    public void doInputGuestCheckout() {
        try {
            guest_checkout = getConfigService().getGuestCheckout();
            currency = getConfigService().getDefaultCurrency();
            configCCTypes = getConfigService().getConfigCCTypes();
            configMonths = getConfigService().getConfigMonths();
            configCCYears = getConfigService().getConfigCCYears();
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
        } else if (actionType == ACTION_TYPE_SAVE_SHIPPING) {
            String shippingCode = (String) wraper.get("shipping_code");
//            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("save_shipping", ((CheckoutService) getListService()).saveShipping(quoteId, shippingCode));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_PAYMENT) {
            String paymentCode = ((CheckoutPayment) models[0]).getCode();
//            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("save_payment", ((CheckoutService) getListService()).savePayment(quoteId, paymentCode));
            return true;
        } else if (actionType == ACTION_TYPE_PLACE_ORDER) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            Checkout checkout = (Checkout) wraper.get("save_shipping");
            checkout.setCreateShip(((CheckoutDetailPanel) mDetailView).isCreateShip());
            checkout.setCreateInvoice(((CheckoutDetailPanel) mDetailView).isCreateInvoice());
            checkout.setNote(((CheckoutDetailPanel) mDetailView).getNote());
//            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            String quoteId = getSelectedItem().getQuoteId();
            wraper.put("place_order", ((CheckoutService) getListService()).placeOrder(quoteId, checkout, listCheckoutPayment));
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
            mCartItemDetailPanel.setCurrency(currency);
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

            // bind data to shipping method list
            bindDataToShippingMethodList(listShipping);

            mPaymentMethodListPanel.bindList(listPayment);
            mCheckoutAddPaymentPanel.bindList(listPayment);

            // hiển thị list shipping address
            Customer customer = (Customer) wraper.get("customer");
            mCheckoutAddressListPanel.bindListModel((List<Model>) (List<?>) customer.getAddress());
            mCheckoutAddressListPanel.setSelectPos(customer.getAddressPosition());
            mCheckoutAddressListPanel.scrollToPosition();

            // auto select shipping method
            autoSelectShipping(listShipping);
            // lưu quote data vào checkout
            getSelectedItem().setQuoteId(quoteId);
//            DataUtil.saveDataStringToPreferences(context, DataUtil.QUOTE, quoteId);
            //  cập nhật giá
            ((CheckoutService) getListService()).updateTotal(checkout);
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());

            mCheckoutPaymentListPanel.setCheckout(checkout);

            if (getView() != null && checkout != null && (getView() instanceof CheckoutListPanel)) {
                // show shipping total
                ((CheckoutListPanel) getView()).showSalesShipping(true);
                // câp nhật giá
                ((CheckoutListPanel) getView()).updateTotalPrice(checkout);
            }

        } else if (success && actionType == ACTION_TYPE_SAVE_SHIPPING) {
            Checkout checkout = (Checkout) wraper.get("save_shipping");
            // cập nhật list payment
            mPaymentMethodListPanel.bindList(checkout.getCheckoutPayment());
            mCheckoutAddPaymentPanel.bindList(checkout.getCheckoutPayment());
            //  cập nhật giá
            ((CheckoutService) getListService()).updateTotal(checkout);
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());

            mCheckoutPaymentListPanel.setCheckout(checkout);

            if (mView != null && checkout != null && (mView instanceof CheckoutListPanel))
                ((CheckoutListPanel) mView).updateTotalPrice(checkout);
            // show payment method
            ((CheckoutDetailPanel) mDetailView).showPaymentMethod();

            // hoàn thành save shipping  hiden progressbar
            ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(false);
        } else if (success && actionType == ACTION_TYPE_SAVE_PAYMENT) {
            Checkout checkout = (Checkout) wraper.get("save_payment");
        } else if (success && actionType == ACTION_TYPE_PLACE_ORDER) {
            Order order = (Order) wraper.get("place_order");
            getSelectedItem().setOrderSuccess(order);
            mCheckoutSuccessPanel.bindItem(order);
            doShowDetailSuccess(true);

            // hoàn thành place order hiden progressbar
            ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(false);
        } else {
            ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(false);
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
        setSelectedItem(checkout);
        getSelectedItems().add(checkout);
        mItem = checkout;
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
            Checkout checkout = ((CheckoutService) getListService()).create();
            checkout.setCustomerID(guest_checkout.getID());
            checkout.setCustomer(guest_checkout);
            getSelectedItems().clear();
            setSelectedItem(checkout);
            getSelectedItems().add(checkout);
            mItem = checkout;
            mCartItemListController.bindList(checkout.getCartItem());
            mCartItemListController.bindParent(checkout);
            mCartItemListController.doRetrieve();
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
            Checkout checkout = getSelectedItems().get(index);
            setSelectedItem(checkout);
            if (checkout.getStatus() == 1) {
                showSalesShipping();
                showActionButtonCheckout();
                doShowDetailPanel(false);
            }
            mItem = checkout;
            mCartItemListController.bindList(checkout.getCartItem());
            mCartItemListController.bindParent(checkout);
            mCartItemListController.doRetrieve();
            mCartOrderListPanel.setSelectPosition(index);
            mCartOrderListPanel.bindList(getSelectedItems());
            mCartOrderListPanel.scrollToPosition(index);
            ((CheckoutListPanel) mView).changeCustomerInToolBar(checkout.getCustomer());
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
                    listAddress.set(0, address);
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
        ((CheckoutListPanel) mView).changeCustomerInToolBar(checkout.getCustomer());
        updateTotalPrice();
        if (checkout.getStatus() == STATUS_CHECKOUT_PROCESSING) {
            if (checkout.getOrderSuccess() != null) {
                mCheckoutSuccessPanel.bindItem(checkout.getOrderSuccess());
                doShowDetailSuccess(true);
            } else {
                doShowDetailSuccess(false);
                doInputSaveCart();
            }
        } else {
            onBackTohome();
        }
    }

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

    private void autoSelectShipping(List<CheckoutShipping> listShipping) {
        if (!((CheckoutDetailPanel) mDetailView).getPickAtStore()) {
            if (listShipping != null && listShipping.size() > 0) {
                if (listShipping.size() == 1) {
                    ((CheckoutDetailPanel) mDetailView).getShippingMethod();
                } else {
                    CheckoutShipping shipping = checkListShippingMethodDefault(0, listShipping);
                    if (shipping != null) {
                        ((CheckoutDetailPanel) mDetailView).selectDefaultShippingMethod(shipping);
                    } else {
                        ((CheckoutDetailPanel) mDetailView).selectDefaultShippingMethod(null);
                    }
                }
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

    public void setCheckoutPaymentCreditCardPanel(CheckoutPaymentCreditCardPanel mCheckoutPaymentCreditCardPanel) {
        this.mCheckoutPaymentCreditCardPanel = mCheckoutPaymentCreditCardPanel;
    }

    public void setCartItemDetailPanel(CartItemDetailPanel mCartItemDetailPanel) {
        this.mCartItemDetailPanel = mCartItemDetailPanel;
    }

    @Override
    public void doShowDetailPanel(boolean show) {
        super.doShowDetailPanel(show);
        if (mProductListController != null) mProductListController.doShowListPanel(!show);
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
        Checkout checkout = (Checkout) wraper.get("save_shipping");
        if (method.getType().equals("1")) {
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

            ((CheckoutDetailPanel) mDetailView).updateTitlePaymentCreditCard(method.getTitle());
            ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(true);
            ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(true);
        } else {
            float total = 0;
            if (checkout.getRemainMoney() > 0) {
                total = checkout.getRemainMoney();
                ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(true);
            } else {
                total = checkout.getGrandTotal();
                ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(false);
            }

            method.setAmount(total);
            method.setBaseAmount(total);
            method.setRealAmount(total);
            method.setBaseRealAmount(total);

            if (listPayment == null) {
                listPayment = new ArrayList<>();
            }
            listPayment.add(method);
            wraper.put("list_payment", listPayment);
            mCheckoutPaymentListPanel.bindList(listPayment);
            mCheckoutPaymentListPanel.updateTotal(listPayment);
            ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(true);
            ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPayment();
        }
    }

    /**
     * xóa 1 payment method  checkout
     */
    public void onRemovePaymentMethod() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        Checkout checkout = (Checkout) wraper.get("save_shipping");
        if (listPayment.size() == 0) {
            ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());
            isEnableButtonAddPayment(false);
            ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
        }
    }

    public void onRemovePaymentCreditCard() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listPayment != null) {
            listPayment.clear();
        } else {
            listPayment = new ArrayList<>();
        }
        wraper.put("list_payment", listPayment);
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
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
    }

    public void isEnableCreateInvoice(boolean enable) {
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(enable);
    }

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
        removeOrder();
        if (getSelectedItems().size() > 1) {
            addNewOrder();
        }
        onBackTohome();
        doShowDetailSuccess(false);
    }

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
        ((CheckoutDetailPanel) mDetailView).setPickAtStoreDefault();
        ((CheckoutDetailPanel) mDetailView).isEnableButtonAddPayment(false);
        ((CheckoutDetailPanel) mDetailView).isCheckCreateInvoice(false);
        ((CheckoutDetailPanel) mDetailView).isCheckCreateShip(false);
        ((CheckoutDetailPanel) mDetailView).isEnableCreateInvoice(false);
        ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
        ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPaymentCreditCard(false);
        showSalesShipping();
        showActionButtonCheckout();
        doShowDetailSuccess(false);
        doShowDetailPanel(false);
    }

    /**
     * check list payment nếu còn thiếu money thì cho phép add thêm payment
     *
     * @return
     */
    public boolean checkListPaymentDialog() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        Checkout checkout = (Checkout) wraper.get("save_shipping");
        List<CheckoutPayment> listAllPayment = checkout.getCheckoutPayment();
        List<CheckoutPayment> listPaymentDialog = new ArrayList<>();
        listPaymentDialog.addAll(listAllPayment);
        for (CheckoutPayment checkoutPayment : listAllPayment) {
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

    public boolean checkListCartItem() {
        if (getSelectedItem().getCartItem() != null && getSelectedItem().getCartItem().size() > 0) {
            return true;
        }
        return false;
    }

    public void showButtonDiscount(boolean isShow) {
        ((CheckoutListPanel) mView).showButtonDiscount(isShow);
    }

    public CheckoutPayment createPaymentMethod() {
        return ((CheckoutService) getListService()).createPaymentMethod();
    }

    public void addNewAddress() {
        ((CheckoutListPanel) mView).checkoutAddNewAddress();
    }

    public void resetPositionAddress() {
        mCheckoutAddressListPanel.setSelectPos(0);
    }

    public void updateTotal() {
        ((CheckoutListPanel) getView()).updateTotalPrice(getSelectedItem());
    }

    public void isShowLoadingList(boolean isShow) {
        ((CheckoutListPanel) mView).showLoading(isShow);
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
}
