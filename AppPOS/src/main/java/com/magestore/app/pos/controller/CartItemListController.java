package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.ProductOptionPanel;
import com.magestore.app.pos.view.MagestoreDialog;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CartItemListController extends AbstractChildListController<Checkout, CartItem> {
    public static final String STATE_ON_SHOW_PRODUCT_OPTION = "STATE_ON_SHOW_PRODUCT_OPTION";
    public static final String STATE_ON_UPDATE_CART_ITEM = "STATE_ON_UPDATE_CART_ITEM";
    MagestoreDialog mCartItemDetailDialog;
    MagestoreDialog mProductOptionDialog;
    CartService mCartService;
    ProductOptionService mProductOptionService;
    ProductOptionPanel mProductOptionPanel;

    @Override
    protected List<CartItem> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Bind 1 sản
     *
     * @param product
     */
    public void bindProduct(Product product) {
        if (!product.haveProductOption()) {
            try {
                // chèn vào cart item
                CartItem cartItem = mCartService.insert(getParent(), product, product.getQuantityIncrement());

                // cập nhật view và giá
                getView().updateModelToFirstInsertIfNotFound(cartItem);
                updateTotalPrice();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            doShowProductOptionInput(product);
        }
//        mView.notifyDataSetChanged();

    }

//    /**
//     * Thông báo cho các controller xử lý, đặc biệt các observe xử lý option
//     *
//     * @param product
//     */
//    public void showChooseProductOptionInput(Product product) {
//        doShowProductOptionInput(product);
//        GenericState<ListController> state = new GenericState<ListController>(this, STATE_ON_SHOW_PRODUCT_OPTION);
//        state.setTag(STATE_ON_SHOW_PRODUCT_OPTION, product);
//        if (getSubject() != null) getSubject().setState(state);
//    }

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
        // thông báo sự kiện update tổng giá
        GenericState<ListController> state = new GenericState<ListController>(this, STATE_ON_UPDATE_CART_ITEM);
        if (getSubject() != null) getSubject().setState(state);
//        mCartService.calculateLastTotalOrderItems();
//        if (mParrentController != null && (mParrentController instanceof  CheckoutListController))
//          ((CheckoutListController) mParrentController).updateTotalPrice();
    }

    /**
     * Xóa 1 sản phẩm khỏi đơn hàng
     *
     * @param product
     */
    public void deleteProduct(Product product) {
        try {
            CartItem cartItem = mCartService.delete(getParent(), product);
            getView().deleteList(cartItem);
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
     *
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
     *
     * @param product
     * @param quantity
     */
    public void addProduct(Product product, int quantity) {
        addProduct(product, quantity, product.getPrice());
    }

    /**
     * Thêm 1 sản phẩm vào đơn hàng
     *
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
     *
     * @param product
     * @param quantity
     */
    public void substructProduct(Product product, int quantity) {
        substructProduct(product, quantity, product.getPrice());
    }

    public List<CartItem> getListCartItem() {
        return mList;
    }


    /**
     * Gán product theo cơ chế subject observ
     *
     * @param state
     */
    public void bindProduct(State state) {
        bindProduct(((ProductListController) state.getController()).getSelectedItem());
    }

    /**
     * Hiển thị dialog detail cho phép chỉnh số lượng, giá cả, discount
     * @param show
     */
    @Override
    public void doShowDetailPanel(boolean show) {
        // khởi tạo và hiển thị dialog
        if (mCartItemDetailDialog == null) {
            mCartItemDetailDialog = com.magestore.app.pos.util.DialogUtil.dialog(getDetailView().getContext(),
                    getDetailView().getContext().getString(R.string.product_option),
                    getDetailView());
        }
        mCartItemDetailDialog.setTitle(getSelectedItem().getProduct().getName());
        mCartItemDetailDialog.setDialogTitle(getSelectedItem().getProduct().getName());
        mCartItemDetailDialog.show();
    }

    /**
     * Tham chiếu cart service
     * @param cartService
     */
    public void setCartService(CartService cartService) {
        mCartService = cartService;
    }

    /**
     * Đặt product option service
     * @param productOptionService
     */
    public void setProductOptionService(ProductOptionService productOptionService) {mProductOptionService = productOptionService; }

    /**
     * Đặt product option panel
     * @param productOptionPanel
     */
    public void setProductOptionPanel(ProductOptionPanel productOptionPanel) {
        mProductOptionPanel = productOptionPanel;
        mProductOptionPanel.setController(this);
    }

    public void doShowProductOptionInput(CartItem cartItem) {
        // khởi tạo và hiển thị dialog
        if (mProductOptionDialog == null) {
            mProductOptionDialog = com.magestore.app.pos.util.DialogUtil.dialog(mProductOptionPanel.getContext(),
                    cartItem.getProduct().getName(),
                    mProductOptionPanel);
        }

        // clear list option và hiện thị thông tin product và cart item
        mProductOptionPanel.clearList();
        mProductOptionPanel.showCartItemInfo(cartItem);
        mProductOptionDialog.show();

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
        mProductOptionPanel.bindItem(item);
    }

    /**
     * Load product option
     * @param item
     * @return
     * @throws Exception
     */
    @Override
    public boolean doLoadItemBackground(CartItem... item) throws Exception  {
        mProductOptionService.retrieve(item[0].getProduct());
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
