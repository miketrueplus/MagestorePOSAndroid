package com.magestore.app.pos.controller;

import android.util.Log;
import android.view.View;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.PaymentMethod;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Payment;
import com.magestore.app.lib.observe.State;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.checkout.CheckoutService;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.pos.panel.ProductListPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
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

    CartItemListController mCartItemListController;
    CheckoutShippingListPanel mCheckoutShippingListPanel;
    CheckoutPaymentListPanel mCheckoutPaymentListPanel;
    ProductListController mProductListController;
    PaymentMethodListPanel mPaymentMethodListPanel;

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

    @Override
    public void onRetrievePostExecute(List<Checkout> list) {
        super.onRetrievePostExecute(list);
        bindItem(list.get(0));
        try {
            mCheckoutShippingListPanel.bindList(getConfigService().getShippingMethodList());
            mCheckoutPaymentListPanel.bindList(getConfigService().getCheckoutPaymentList());
            mPaymentMethodListPanel.bindList(getConfigService().getPaymentMethodList());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void doInputSaveCart() {
        binCartItem();
        Checkout checkout = getSelectedItem();
        doAction(ACTION_TYPE_SAVE_CART, null, null, checkout);
    }

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_TYPE_SAVE_CART) {
            Checkout checkout = ((CheckoutService) mListService).savePayment((Checkout) models[0]);
            Log.e("Checkout", checkout.getCustomerID());
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_TYPE_SAVE_CART) {

        }
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

    public void onAddPaymentMethod(PaymentMethod method) {

    }

    /**
     * Place thực hiện order
     */
    public void onPlaceOrder() {
        doInsert(getSelectedItem());
    }
}
