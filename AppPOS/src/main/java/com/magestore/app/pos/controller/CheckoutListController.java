package com.magestore.app.pos.controller;

import android.content.Context;
import android.util.Log;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.checkout.CheckoutShipping;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.observe.State;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.pos.panel.CheckoutAddPaymentPanel;
import com.magestore.app.pos.panel.CheckoutDetailPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.util.DataUtil;

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

    static final int ACTION_TYPE_SAVE_CART = 0;
    static final int ACTION_TYPE_SAVE_SHIPPING = 1;
    static final int ACTION_TYPE_SAVE_PAYMENT = 2;
    static final int ACTION_TYPE_PLACE_ORDER = 3;

    Map<String, Object> wraper;
    CartItemListController mCartItemListController;
    CheckoutShippingListPanel mCheckoutShippingListPanel;
    CheckoutPaymentListPanel mCheckoutPaymentListPanel;
    ProductListController mProductListController;
    PaymentMethodListPanel mPaymentMethodListPanel;
    CheckoutAddPaymentPanel mCheckoutAddPaymentPanel;
    Context context;

    @Override
    public void bindItem(Checkout item) {
        super.bindItem(item);
        if (mCartItemListController != null) {
            mCartItemListController.bindParent(item);
            mCartItemListController.doRetrieve();
        }
    }

    @Override
    public List<Checkout> onRetrieveBackground(int page, int pageSize) throws Exception {
        wraper = new HashMap<>();
        return super.onRetrieveBackground(page, pageSize);
    }

    /**
     * Tương ứng khi 1 product được chọn trên product list
     *
     * @param product
     */
    public void bindProduct(Product product) {
        if (mCartItemListController != null) mCartItemListController.bindProduct(product);
    }

    public void bindCustomer(Customer customer) {
        if (customer != null) {
            Checkout checkout = getSelectedItem();
            checkout.setCustomer(customer);
            checkout.setCustomerID(customer.getID());
        }
    }

    public void binCartItem() {
        Checkout checkout = getSelectedItem();
        checkout.setCartItem(mCartItemListController.getListCartItem());
    }

    public void doInputSaveCart() {
        binCartItem();
        Checkout checkout = getSelectedItem();
        wraper.put("quote_id", DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE));
        doAction(ACTION_TYPE_SAVE_CART, null, wraper, checkout);
    }

    public void doInputSaveShipping(CheckoutShipping checkoutShipping) {
        ((CheckoutDetailPanel) mDetailView).setTitleShippingMethod(checkoutShipping.getTitle());
        doAction(ACTION_TYPE_SAVE_SHIPPING, null, wraper, checkoutShipping);
    }

    public void doInputSavePayment(CheckoutPayment checkoutPayment) {
        wraper.put("select_payment", checkoutPayment);
        doAction(ACTION_TYPE_SAVE_PAYMENT, null, wraper, checkoutPayment);
    }

    public void doInputPlaceOrder() {
        // Check payment khác null hay ko
        List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listCheckoutPayment != null && listCheckoutPayment.size() > 0) {
            doAction(ACTION_TYPE_PLACE_ORDER, null, wraper, null);
        } else {
            // hiển thị thông báo chọn payment
            ((CheckoutDetailPanel) mDetailView).showNotifiSelectPayment();
        }
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_TYPE_SAVE_CART) {
            String quoteId = (String) wraper.get("quote_id");
            wraper.put("save_cart", ((CheckoutService) mListService).saveCart((Checkout) models[0], quoteId));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_SHIPPING) {
            String shippingCode = ((CheckoutShipping) models[0]).getCode();
            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            wraper.put("save_shipping", ((CheckoutService) mListService).saveShipping(quoteId, shippingCode));
            return true;
        } else if (actionType == ACTION_TYPE_SAVE_PAYMENT) {
            String paymentCode = ((CheckoutPayment) models[0]).getCode();
            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            wraper.put("save_payment", ((CheckoutService) mListService).savePayment(quoteId, paymentCode));
            return true;
        } else if (actionType == ACTION_TYPE_PLACE_ORDER) {
            List<CheckoutPayment> listCheckoutPayment = (List<CheckoutPayment>) wraper.get("list_payment");
            Checkout checkout = (Checkout) wraper.get("save_shipping");
            checkout.setCreateShip(((CheckoutDetailPanel) mDetailView).isCreateShip());
            checkout.setCreateInvoice(((CheckoutDetailPanel) mDetailView).isCreateInvoice());
            String quoteId = DataUtil.getDataStringToPreferences(context, DataUtil.QUOTE);
            wraper.put("place_order", ((CheckoutService) mListService).placeOrder(quoteId, checkout, listCheckoutPayment));
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_TYPE_SAVE_CART) {
            Checkout checkout = (Checkout) wraper.get("save_cart");
            String quoteId = checkout.getQuote().getID();
            bindItem(checkout);
            // cập nhật list shipping và payment
            mCheckoutShippingListPanel.bindList(checkout.getCheckoutShipping());
            mPaymentMethodListPanel.bindList(checkout.getCheckoutPayment());
            mCheckoutAddPaymentPanel.bindList(checkout.getCheckoutPayment());
            // auto select shipping method
            mCheckoutShippingListPanel.getShippingMethodDefault();
            // lưu quote data vào system
            DataUtil.saveDataStringToPreferences(context, DataUtil.QUOTE, quoteId);
            //  cập nhật giá
            ((CheckoutService) mListService).updateTotal(checkout);
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());

            mCheckoutPaymentListPanel.setCheckout(checkout);

            if (mView != null && checkout != null && (mView instanceof CheckoutListPanel)) {
                // ẩn button checkout và hold order
                ((CheckoutListPanel) mView).hidenActionButton(true);
                // show shipping total
                ((CheckoutListPanel) mView).showSalesShipping(true);
                // câp nhật giá
                ((CheckoutListPanel) mView).updateTotalPrice(checkout);
            }
            // show detail panel
            doShowDetailPanel(true);
        } else if (success && actionType == ACTION_TYPE_SAVE_SHIPPING) {
            Checkout checkout = (Checkout) wraper.get("save_shipping");
            // cập nhật list payment
            mPaymentMethodListPanel.bindList(checkout.getCheckoutPayment());
            mCheckoutAddPaymentPanel.bindList(checkout.getCheckoutPayment());
            //  cập nhật giá
            ((CheckoutService) mListService).updateTotal(checkout);
            ((CheckoutDetailPanel) mDetailView).bindTotalPrice(checkout.getGrandTotal());

            mCheckoutPaymentListPanel.setCheckout(checkout);

            if (mView != null && checkout != null && (mView instanceof CheckoutListPanel))
                ((CheckoutListPanel) mView).updateTotalPrice(checkout);
            // hiden shipping method
            ((CheckoutDetailPanel) mDetailView).onClickTitleShippingMethod();
            // show payment method
            ((CheckoutDetailPanel) mDetailView).showPaymentMethod();

            // TODO: hoàn thành save shipping
            Log.e("CheckListController", "finish shipping");
        } else if (success && actionType == ACTION_TYPE_SAVE_PAYMENT) {
            Checkout checkout = (Checkout) wraper.get("save_payment");
            // TODO: Action khi save payment method xong

            // TODO: hoàn thành save payment
            Log.e("CheckListController", "finish payment");
        } else if (success && actionType == ACTION_TYPE_PLACE_ORDER) {
            Order order = (Order) wraper.get("place_order");
            // TODO: Action khi place order

            // TODO: hoàn thành place order
            Log.e("CheckListController", "finish place order");
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Tham chiếu sang cart item controller
     *
     * @param controller
     */
    public void setCartItemListController(CartItemListController controller) {
        mCartItemListController = controller;
    }

    /**
     * Cập nhật tổng giá
     */
    public void updateTotalPrice() {
        if (mView != null && mItem != null && (mView instanceof CheckoutListPanel))
            ((CheckoutListPanel) mView).updateTotalPrice(mItem);
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

    @Override
    public void doShowDetailPanel(boolean show) {
        super.doShowDetailPanel(show);
        if (mProductListController != null) mProductListController.doShowListPanel(!show);
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

    public void onAddPaymentMethod(CheckoutPayment method) {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listPayment == null) {
            listPayment = new ArrayList<>();
        }
        listPayment.add(method);
        wraper.put("list_payment", listPayment);
        mCheckoutPaymentListPanel.bindList(listPayment);
        ((CheckoutDetailPanel) mDetailView).showPanelCheckoutPayment();
    }

    public void onRemovePaymentMethod() {
        List<CheckoutPayment> listPayment = (List<CheckoutPayment>) wraper.get("list_payment");
        if (listPayment.size() == 0) {
            ((CheckoutDetailPanel) mDetailView).showPanelPaymentMethod();
        }
    }

    public void showActionButtonCheckout() {
        if (mView != null && (mView instanceof CheckoutListPanel)) {
            // show button checkout và hold order
            ((CheckoutListPanel) mView).hidenActionButton(false);
        }
    }

    public void showSalesShipping(){
        if (mView != null && (mView instanceof CheckoutListPanel)) {
            // show button checkout và hold order
            ((CheckoutListPanel) mView).showSalesShipping(false);
        }
    }

    // khi thay đổi value từng payment update giá trị money
    public void updateMoneyTotal(boolean type, float totalPrice){
        ((CheckoutDetailPanel) mDetailView).updateMoneyTotal(type, totalPrice);
    }

    /**
     * Place thực hiện order
     */
    public void onPlaceOrder() {
        doInsert(getSelectedItem());
    }
}
