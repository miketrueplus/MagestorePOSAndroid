package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.panel.CartItemListPanel;

import java.util.List;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CartItemListController extends AbstractListController<Items> {
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

    /**
     * Bind 1 sản
     * @param product
     */
    public void bindItem(Product product) {
        if (mCartService == null) return;

        // Bổ sung thêm product vào cart
        mCartService.addOrderItem(product, 1, product.getPrice());
        mView.notifyDataSetChanged();

//
        // cập nhật tổng lên cuối order
        updateTotalPrice();
    }

    /**
     * Tạo một phiên bán hàng mới
     */
    public void newSales() {
        mCartService.newSales();
        super.bindList(mCartService.getOrder().getOrderItems());
    }

    /**
     * Cập nhật lại phần tính giá tiền, discount và tax
     */
    public void updateTotalPrice() {
        mCartService.calculateLastTotalOrderItems();
        ((CartItemListPanel) mView).updateTotalPrice(mCartService.getOrder());
    }

    /**
     * Xóa 1 sản phẩm khỏi đơn hàng
     * @param product
     */
    public void deleteProduct(Product product) {
        mCartService.delOrderItem(product);
        mView.notifyDataSetChanged();
        updateTotalPrice();
    }

    /**
     * Thêm 1 sản phẩm vào đơn hàng
     * @param product
     * @param quantity
     * @param price
     */
    public void addProduct(Product product, int quantity, float price) {
        mCartService.addOrderItem(product, quantity, price);
//        mView.notifyDatasetChanged();
        updateTotalPrice();
    }

    /**
     * Thêm 1 sản phẩm vào đơn hàng
     * @param product
     * @param quantity
     */
    public void addProduct(Product product, int quantity) {
        addProduct(product, quantity, product.getPrice());
    }

    /**
     * Thêm 1 sản phẩm vào đơn hàng
     * @param product
     * @param quantity
     * @param price
     */
    public void substructProduct(Product product, int quantity, float price) {
        mCartService.subtructOrderItem(product, quantity);
//        mView.notifyDatasetChanged();
        updateTotalPrice();
    }

    /**
     * Thêm 1 sản phẩm vào đơn hàng
     * @param product
     * @param quantity
     */
    public void substructProduct(Product product, int quantity) {
        substructProduct(product, quantity, product.getPrice());
    }
}
