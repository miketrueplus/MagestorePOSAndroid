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
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.pos.panel.CartOrderListPanel;
import com.magestore.app.pos.panel.CheckoutAddPaymentPanel;
import com.magestore.app.pos.panel.CheckoutAddressListPanel;
import com.magestore.app.pos.panel.CheckoutDetailPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
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

    Map<String, Object> wraper;
    CartItemListController mCartItemListController;
    CheckoutShippingListPanel mCheckoutShippingListPanel;
    CheckoutPaymentListPanel mCheckoutPaymentListPanel;
    ProductListController mProductListController;
    PaymentMethodListPanel mPaymentMethodListPanel;
    CheckoutAddPaymentPanel mCheckoutAddPaymentPanel;
    Context context;
    Customer guest_checkout;
    Currency currency;
    CartOrderListPanel mCartOrderListPanel;
    CheckoutAddressListPanel mCheckoutAddressListPanel;
    CheckoutSuccessPanel mCheckoutSuccessPanel;
    CustomerAddressService mCustomerAddressService;

//    @Override
//    public void bindItem(Checkout item) {
//        super.bindItem(item);
//        if (mCartItemListController != null) {
//            mCartItemListController.bindParent(item);
//            mCartItemListController.doRetrieve();
//        }
//    }

//    @Override
//    public void doRetrieve() {
//        super.doRetrieve();
//    }

    @Override
    public List<Checkout> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (wraper == null)
            wraper = new HashMap<>();
        return super.onRetrieveBackground(page, pageSize);
    }

//    /**
//     * Tương ứng khi 1 product được chọn trên product list
//     *
//     * @param product
//     */
//    public void bindProduct(Product product) {
//        if (mCartItemListController != null) mCartItemListController.bindProduct(product);
//    }

    public void bindCustomer(Customer customer) {
        if (customer != null) {
            wraper.put("customer", customer);
            Checkout checkout = getSelectedItem();
            if (checkout == null) {
                if (mList == null) {
                    mList = new ArrayList<>();
                }
                Checkout new_checkout = ((CheckoutService) getListService()).create();
                new_checkout.setCustomer(customer);
                new_checkout.setCustomerID(customer.getID());
                mList.add(new_checkout);
                setSelectedItem(new_checkout);
                getView().notifyDataSetChanged();
            } else {
                checkout.setCustomer(customer);
                checkout.setCustomerID(customer.getID());
            }
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

    public void doInputNewAddress(Customer customer, CustomerAddress customerAddress) {
        wraper.put("customer_new_address", customer);
        doAction(ACTION_TYPE_NEW_ADDRESS, null, wraper, customerAddress);
    }

    /**
     * khi click checkout request savecart tạo quote
     */
    public void doInputSaveCart() {
        ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(true);
        // ẩn button checkout và hold order
        ((CheckoutListPanel) mView).changeActionButton(true);
        // show detail panel
        doShowDetailPanel(true);
        binCartItem();
        Checkout checkout = getSelectedItem();
        wraper.put("quote_id", DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE));
        doAction(ACTION_TYPE_SAVE_CART, null, wraper, checkout);
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
        ((CheckoutDetailPanel) mDetailView).isShowLoadingDetail(true);
        // Check payment khác null hay ko
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
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
            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            wraper.put("save_shipping", ((CheckoutService) getListService()).saveShipping(quoteId, shippingCode));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_PAYMENT) {
            String paymentCode = ((CheckoutPayment) models[0]).getCode();
            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            wraper.put("save_payment", ((CheckoutService) getListService()).savePayment(quoteId, paymentCode));
            return true;
        } else if (actionType == ACTION_TYPE_PLACE_ORDER) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            Checkout checkout = (Checkout) wraper.get("save_shipping");
            checkout.setCreateShip(((CheckoutDetailPanel) mDetailView).isCreateShip());
            checkout.setCreateInvoice(((CheckoutDetailPanel) mDetailView).isCreateInvoice());
            checkout.setNote(((CheckoutDetailPanel) mDetailView).getNote());
            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
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
            ((CheckoutListPanel) mView).updateAddress(0, typeAddress, customerAddress);
        } else if (success && actionType == ACTION_TYPE_SAVE_CART) {
            Checkout checkout = (Checkout) wraper.get("save_cart");
            String quoteId = checkout.getQuote().getID();
            // cập nhật list shipping và payment
            List<CheckoutShipping> listShipping = checkout.getCheckoutShipping();
            List<CheckoutPayment> listPayment = checkout.getCheckoutPayment();
            ((CheckoutDetailPanel) mDetailView).setShippingDataSet(listShipping);
            mPaymentMethodListPanel.bindList(listPayment);
            mCheckoutAddPaymentPanel.bindList(listPayment);
            // auto select shipping method
            autoSelectShipping(listShipping);
            // lưu quote data vào system
            DataUtil.saveDataStringToPreferences(context, DataUtil.QUOTE, quoteId);
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

            // hiển thị list shipping address
            Customer customer = (Customer) wraper.get("customer");
            mCheckoutAddressListPanel.bindList(customer.getAddress());
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
            if (checkout.getCartItem().size() <= 0) {
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
        if (mDetailView.getVisibility() == View.VISIBLE) {
            doInputSaveCart();
        }
    }

    private void autoSelectShipping(List<CheckoutShipping> listShipping) {
        if (((CheckoutDetailPanel) mDetailView).getPickAtStore()) {
            if (listShipping != null && listShipping.size() > 0) {
                if (listShipping.size() == 1) {
                    ((CheckoutDetailPanel) mDetailView).getShippingMethod();
                } else {
                    CheckoutShipping shipping = checkListShippingMethodDefault(listShipping);
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
                    CheckoutShipping shipping = checkListShippingMethodDefault(listShipping);
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

    public void actionNewOrder() {
        onBackTohome();
        doShowDetailSuccess(false);
    }

    public void onBackTohome(){
        showSalesShipping();
        showActionButtonCheckout();
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

    public CheckoutShipping checkListShippingMethodDefault(List<CheckoutShipping> listShipping) {
        for (CheckoutShipping shipping : listShipping) {
            if (shipping.getIsDefault().equals("1")) {
                return shipping;
            }
        }
        return null;
    }

    /**
     * Place thực hiện order
     */
    public void onPlaceOrder() {
        doInsert(getSelectedItem());
    }
}
