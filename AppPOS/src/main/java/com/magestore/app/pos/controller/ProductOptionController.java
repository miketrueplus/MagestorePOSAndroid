package com.magestore.app.pos.controller;

import android.view.View;

import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.model.checkout.cart.PosCartItem;
import com.magestore.app.pos.panel.ProductOptionPanel;
import com.magestore.app.pos.view.MagestoreDialog;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 3/1/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class ProductOptionController extends AbstractController<CartItem, ProductOptionPanel, ProductOptionService> {
    MagestoreDialog mDialog;
    CartService mCartService;

    /**
     * Tham chiếu cart service
     * @param cartService
     */
    public void setCartService(CartService cartService) {
        mCartService = cartService;
    }

    /**
     * Hiển thị dialog show chọn option
     * @param state
     */
    public void doShowProductOptionInput(State state) {
        Product product = (Product) state.getTag(CartItemListController.STATE_ON_SHOW_PRODUCT_OPTION);
        doShowProductOptionInput(product);
    }

    public void doShowProductOptionInput(CartItem cartItem) {
        // khởi tạo và hiển thị dialog
        if (mDialog == null) {
            mDialog = com.magestore.app.pos.util.DialogUtil.dialog(getView().getContext(),
                    cartItem.getProduct().getName(),
                    getView());
        }

        // clear list option và hiện thị thông tin product và cart item
        getView().clearList();
        getView().showCartItemInfo(cartItem);
        mDialog.show();

        // gán cart item và load product option
        if (cartItem.getProduct().getProductOption() != null) bindItem(cartItem);
        else doLoadItem(cartItem);
    }

    /**
     * Hiển thị dialog show chọn option
     * @param product
     */
    public void doShowProductOptionInput(Product product) {
        CartItem cartItem = mCartService.create(product);
        doShowProductOptionInput(cartItem);
    }

    /**
     * Gán product vào controller và view
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        getView().bindItem(item);
    }

    /**
     * Load product option
     * @param item
     * @return
     * @throws Exception
     */
    @Override
    public boolean doLoadItemBackground(CartItem... item) throws Exception  {
        getService().retrieve(item[0].getProduct());
        return true;
    }

    /**
     * Load product option thành công, gán vào view để xử lý
     * @param success
     * @param item
     */
    @Override
    public void onLoadItemPostExecute(boolean success, CartItem... item) {
        super.onLoadItemPostExecute(success, item);
        if (success) bindItem(item[0]);
    }

    public void setQuantity() {
    }

    /**
     * Tăng bớt số lượng
     */
    public void addQuantity() {
        mCartService.increase(getItem());

    }

    /**
     * Trừ số lượng
     */
    public void substractQuantity() {
        mCartService.substract(getItem());
    }
}
