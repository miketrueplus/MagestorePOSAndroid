package com.magestore.app.pos.controller;

import android.view.View;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.pos.panel.CheckoutListPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutListController extends AbstractListController<Checkout> {
    CartItemListController mCartItemListController;

    @Override
    public void bindItem(Checkout item) {
        super.bindItem(item);
        if (mCartItemListController != null) {
            mCartItemListController.bindParent(item);
            mCartItemListController.doRetrieve();
        }
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

}
