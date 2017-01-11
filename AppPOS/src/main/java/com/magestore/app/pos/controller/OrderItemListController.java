package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.service.catalog.ProductService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.lib.service.sales.OrderService;

import java.util.List;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class OrderItemListController extends AbstractListController<Items> {
    /**
     * Service xử lý các vấn đề liên quan đến customer
     */
    CartService mCartService;

    public void setCartService(CartService service) {
        mCartService = service;
    }

    public CartService getCartService() {
        return mCartService;
    }

    @Override
    protected List<Items> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    public void bindItem(Product product) {
//        super.bindItem(item);
//        if (mOrderItemListController != null) mOrderItemListController.bindItem(item);

        if (mCartService == null) return;
        mCartService.addOrderItem(product, 1, product.getPrice());
        mView.notifyDatasetChanged();
//        mOrderItemListAdapter.notifyDataSetChanged();
//
//        // cập nhật tổng lên cuối order
//        updateTotalPrice();
    }
}
