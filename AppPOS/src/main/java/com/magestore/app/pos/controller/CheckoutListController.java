package com.magestore.app.pos.controller;

import android.view.View;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CheckoutPaymentListPanel;
import com.magestore.app.pos.panel.CheckoutShippingListPanel;
import com.magestore.app.pos.panel.PaymentMethodListPanel;
import com.magestore.app.pos.panel.ProductListPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutListController extends AbstractListController<Checkout> {
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
     * @param product
     */
    public void bindProduct(Product product) {
        if (mCartItemListController != null) mCartItemListController.bindProduct(product);
    }

    @Override
    public void onRetrievePostExecute(List<Checkout> list) {
        super.onRetrievePostExecute(list);
        bindItem(list.get(0));
        try {
            mCheckoutShippingListPanel.bindList(getConfigService().getShippingMethodList());
            mCheckoutPaymentListPanel.bindList(getConfigService().getCheckoutPaymentList());
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

    /**
     * Tham chiếu sang cart item controller
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
}
