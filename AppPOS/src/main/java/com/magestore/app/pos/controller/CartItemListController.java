package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.observe.State;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.panel.CartItemListPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CartItemListController extends AbstractChildListController<Checkout, CartItem> {
    CartService mCartService;
    @Override
    protected List<CartItem> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Bind 1 sản
     * @param product
     */
    public void bindProduct(Product product) {
        try {
            CartItem cartItem = mCartService.insert(getParent(), product, product.getQuantityIncrement());
            mView.updateModelInsertAtLastIfNotFound(cartItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        mView.notifyDataSetChanged();
        updateTotalPrice();
    }


    @Override
    public void setChildListService(ChildListService<Checkout, CartItem> service) {
        super.setChildListService(service);
        if (service instanceof CartService) mCartService = (CartService) service;
    }


    /**
     * Cập nhật lại phần tính giá tiền, discount và tax
     */
    public void updateTotalPrice() {
        mCartService.calculateLastTotal(getParent());

//        mCartService.calculateLastTotalOrderItems();
        if (mParrentController != null && (mParrentController instanceof  CheckoutListController))
          ((CheckoutListController) mParrentController).updateTotalPrice();
    }

    /**
     * Xóa 1 sản phẩm khỏi đơn hàng
     * @param product
     */
    public void deleteProduct(Product product) {
        try {
            CartItem cartItem = mCartService.delete(getParent(), product);
            mView.deleteList(cartItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
//        mCartService.delOrderItem(product);
//        mView.notifyDataSetChanged();
        updateTotalPrice();
    }

    /**
     * Thêm 1 sản phẩm vào đơn hàng
     * @param product
     * @param quantity
     * @param price
     */
    public void addProduct(Product product, int quantity, float price) {
        try {
            CartItem cartItem = mCartService.insert(getParent(), product, quantity, price);
//            mView.updateModelInsertAtLastIfNotFound(cartItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mView.notifyDataSetChanged();
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
//        mCartService.subtructOrderItem(product, quantity);
        try {
            CartItem cartItem = mCartService.delete(getParent(), product, product.getQuantityIncrement());
//            mView.updateModel(cartItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mView.notifyDataSetChanged();
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

    public List<CartItem> getListCartItem(){
        return mList;
    }

    @Override
    public void notifyState(State state) {
//        super.notifyState(state);
//        if (ProductListController.STATE_ON_CHOOSE_PRODUCT.equals(state.getStateCode())) {
//            bindProduct(((ProductListController)state.getController()).getSelectedItem());
//        }
    }

    public void bindProduct(State state) {
        bindProduct(((ProductListController)state.getController()).getSelectedItem());
    }
}
