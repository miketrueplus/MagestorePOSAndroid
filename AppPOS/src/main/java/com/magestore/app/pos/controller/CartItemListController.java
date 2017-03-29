package com.magestore.app.pos.controller;

import android.text.TextUtils;
import android.view.View;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.catalog.Product;
import com.magestore.app.lib.model.catalog.ProductOption;
import com.magestore.app.lib.model.catalog.ProductOptionCustom;
import com.magestore.app.lib.model.catalog.ProductOptionCustomValue;
import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.service.catalog.ProductOptionService;
import com.magestore.app.lib.service.checkout.CartService;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.CheckoutCustomSalePanel;
import com.magestore.app.pos.panel.CustomerDetailPanel;
import com.magestore.app.pos.panel.ProductOptionPanel;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/11/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CartItemListController extends AbstractChildListController<Checkout, CartItem> {
    public static final String STATE_ON_UPDATE_CART_ITEM = "STATE_ON_UPDATE_CART_ITEM";

    // dialog
    MagestoreDialog mCartItemDetailDialog;
    MagestoreDialog mProductOptionDialog;
    MagestoreDialog mCustomeSaleDialog;

    // serrvice
    CartService mCartService;
    ProductOptionService mProductOptionService;

    // panel
    ProductOptionPanel mProductOptionPanel;
    CheckoutListController mCheckoutListController;
    CheckoutCustomSalePanel mCheckoutCustomSalePanel;

    static final int ACTION_CART_DELETE_ITEM = 0;
    Map<String, Object> wraper;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
        if (wraper == null) {
            wraper = new HashMap<>();
        }
    }

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
                mCheckoutListController.showButtonDiscount(true);
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

    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        if (actionType == ACTION_CART_DELETE_ITEM) {
            Product product = (Product) models[0];
            CartItem cartItem = mCartService.delete(getParent(), product);
            wraper.put("cart_respone", cartItem);
            return true;
        }
        return false;
    }

    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        if (success && actionType == ACTION_CART_DELETE_ITEM) {
            CartItem cartItem = (CartItem) wraper.get("cart_respone");
            Product product = (Product) models[0];
            if (cartItem != null) {
                getView().deleteList(cartItem);
                if (product.getIsSaveCart()) {
                    mCheckoutListController.updateTotal();
                }
                updateTotalPrice();
            }
            if(!mCheckoutListController.checkListCartItem()){
                mCheckoutListController.showButtonDiscount(false);
            }
            mCheckoutListController.isShowLoadingList(false);
        }
    }

    /**
     * Cập nhật lại phần tính giá tiền, discount và tax
     */
    public void updateTotalPrice() {
        mCartService.calculateLastTotal(getParent());

        // thông báo sự kiện update tổng giá
        GenericState<ListController> state = new GenericState<ListController>(this, STATE_ON_UPDATE_CART_ITEM);
        if (getSubject() != null) getSubject().setState(state);
    }

    /**
     * Xóa 1 sản phẩm khỏi đơn hàng
     *
     * @param product
     */
    public void deleteProduct(Product product) {
        if (product.getIsSaveCart()) {
            mCheckoutListController.isShowLoadingList(true);
        }
        doAction(ACTION_CART_DELETE_ITEM, null, wraper, product);
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
     * Hiển thị mô tả sản phẩm, cho phép add to card
     * @param state
     */
    public void showProductDetail(State state) {
        doShowProductOptionInput(((ProductListController) state.getController()).getSelectedItem());
    }

    /**
     * Hiển thị dialog detail cho phép chỉnh số lượng, giá cả, discount
     *
     * @param show
     */
    @Override
    public void doShowDetailPanel(boolean show) {
        // khởi tạo và hiển thị dialog
        if (mCartItemDetailDialog == null) {
            mCartItemDetailDialog = com.magestore.app.pos.util.DialogUtil.dialog(getDetailView().getContext(),
                    getDetailView().getContext().getString(R.string.product_option),
                    getDetailView());
            mCartItemDetailDialog.setGoneButtonSave(true);
        }

        // đổi title dialiog theo sản phẩm và hiển thị
        mCartItemDetailDialog.show();
        mCartItemDetailDialog.getDialogTitle().setText(getSelectedItem().getProduct().getName());
    }

    public void closeAllOpeningDialog() {
        if (mCartItemDetailDialog != null && mCartItemDetailDialog.isShowing())
            mCartItemDetailDialog.dismiss();
        if (mProductOptionDialog != null && mProductOptionDialog.isShowing())
            mProductOptionDialog.dismiss();
        if (mCustomeSaleDialog != null && mCustomeSaleDialog.isShowing())
            mCustomeSaleDialog.dismiss();
    }

    public void updateToCart(CartItem cartItem, String productID, String productName, int quantity, float price, int qtyIncrement) {
        try {
            // chèn vào data set
            CartItem item = mCartService.insert(getParent(), productID, productName, quantity, price);
            item.getProduct().setQuantityIncrement(qtyIncrement);
            // chèn model lên đầu
            getView().updateModelToFirstInsertIfNotFound(item);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // cập nhật giá tổng
        updateTotalPrice();
    }

    /**
     * Cập nhật cart item hiện tại. Tắt các dialog
     *
     * @param cartItem
     */
    public void updateToCart(CartItem cartItem) {
        try {
            CartItem itemInList = mCartService.insertWithOption(getParent(), cartItem);
            // chèn model lên đầu
            getView().updateModelToFirstInsertIfNotFound(itemInList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



        // ẩn các dialog đang hiển thị
        closeAllOpeningDialog();

        // cập nhật giá tổng
        updateTotalPrice();
    }

    /**
     * Cập nhật cart item hiện tại. Tắt các dialog
     *
     * @param cartItem
     */
    public void updateToCartNoOption(CartItem cartItem) {
        try {
            // chèn vào cart item tùy xem có hoặc chưa có trong cart
            CartItem newCartItem = mCartService.insert(getParent(), cartItem.getProduct(), cartItem.getQuantity());

            // cập nhật view và giá
            getView().updateModelToFirstInsertIfNotFound(newCartItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // ẩn các dialog đang hiển thị
        closeAllOpeningDialog();

        // cập nhật giá tổng
        updateTotalPrice();
    }

    /**
     * Chèn mới cartitem. Tắt các dialog
     *
     * @param cartItem
     */
    public void addToCart(CartItem cartItem) {
        try {
            mCartService.insert(getParent(), cartItem);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // chèn model lên đầu
        getView().updateModelToFirstInsertIfNotFound(cartItem);

        // ẩn các dialog đang hiển thị
        closeAllOpeningDialog();

        // cập nhật giá tổng
        updateTotalPrice();
    }

    /**
     * Tham chiếu cart service
     *
     * @param cartService
     */
    public void setCartService(CartService cartService) {
        mCartService = cartService;
    }

    /**
     * Đặt product option service
     *
     * @param productOptionService
     */
    public void setProductOptionService(ProductOptionService productOptionService) {
        mProductOptionService = productOptionService;
    }

    /**
     * Đặt product option panel
     *
     * @param productOptionPanel
     */
    public void setProductOptionPanel(ProductOptionPanel productOptionPanel) {
        mProductOptionPanel = productOptionPanel;
        mProductOptionPanel.setController(this);
    }

    /**
     * Cập nhật price của item theo option đã chọn
     *
     * @param cartItem
     */
    public void updateCartItemPrice(CartItem cartItem) {
        mCartService.updatePrice(cartItem);
    }

    /**
     * Đặt lại các chosen trên product tương ứng
     *
     * @param cartItem
     */
    public void setProductOptionChosen(CartItem cartItem) {
        if (cartItem == null) return;
        if (cartItem.getProduct() == null) return;
        if (cartItem.getProduct().getProductOption() == null) return;
        if (cartItem.getProduct().getProductOption().getCustomOptions() == null) return;
    }

    /**
     * Hiển thị dialog product option
     */
    public void doShowProductOptionInput() {
        doShowProductOptionInput(getSelectedItem());
    }

    /**
     * hiển thị dialog product option
     *
     * @param cartItem
     */
    public void doShowProductOptionInput(CartItem cartItem) {
        // khởi tạo và hiển thị dialog
        if (mProductOptionDialog == null) {
            mProductOptionDialog = DialogUtil.dialog(mProductOptionPanel.getContext(),
                    cartItem.getProduct().getName(),
                    mProductOptionPanel);
            mProductOptionDialog.setGoneButtonSave(true);
            mProductOptionDialog.setDialogTitle(cartItem.getProduct().getName());
        }

        // clear list option và hiện thị thông tin product và cart item
        mProductOptionPanel.setCheckoutListController(mCheckoutListController);
        setProductOptionChosen(cartItem);
        mProductOptionPanel.clearList();
        mProductOptionPanel.showCartItemInfo(cartItem);

        // đổi title dialog theo tên sản phẩm và hiển thị
        mProductOptionDialog.show();
        mProductOptionDialog.getDialogTitle().setText(cartItem.getProduct().getName());

        // gán cart item và load product option
        if (cartItem.getProduct().getProductOption() != null) {
            bindItem(cartItem);
            hideAllProgressBar();
        }
        else doLoadItem(cartItem);
    }

    /**
     * Hiển thị progress bar
     *
     * @param blnShow
     */
    @Override
    public void doShowProgress(boolean blnShow) {
        super.doShowProgress(blnShow);
        if (mProductOptionPanel != null) mProductOptionPanel.showProgress(blnShow);
    }

    /**
     * Ẩn các progress bar
     */
    @Override
    public void hideAllProgressBar() {
        super.hideAllProgressBar();
        if (mProductOptionPanel != null) mProductOptionPanel.hideAllProgressBar();
    }

    /**
     * Hiển thị dialog show chọn option
     *
     * @param product
     */
    public void doShowProductOptionInput(Product product) {
        CartItem cartItem = mCartService.create(product);
        doShowProductOptionInput(cartItem);
    }

    /**
     * Gán product vào controller và view
     *
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        if (item.getProduct().haveProductOption() && mProductOptionPanel != null)
            mProductOptionPanel.bindItem(item);
    }

    /**
     * Load product option
     *
     * @param item
     * @return
     * @throws Exception
     */
    @Override
    public boolean doLoadItemBackground(CartItem... item) throws Exception {
        mProductOptionService.retrieve(item[0].getProduct());
        return true;
    }

    /**
     * Load product option thành công, gán vào view để xử lý
     *
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
    public void addQuantity(CartItem cartItem) {
        mCartService.increase(cartItem);

    }

    /**
     * Trừ số lượng
     */
    public void substractQuantity(CartItem cartItem) {
        mCartService.substract(cartItem);
    }


    private boolean blnAlowRemoveCartItem = true;
    public void blnAlowRemoveCartItem(State state) {
        blnAlowRemoveCartItem = (CheckoutListController.STATE_ENABLE_CHANGE_CART_ITEM.equals(state.getStateCode()));
    }

    /*
    Cho phép change cart hay không
     */
    public boolean isAllowChangeCartItem() {
        return blnAlowRemoveCartItem;
    }

    /**
     * Hiển thị dialog custome sales
     */
    public void addCustomSale(State state) {
        doShowCustomeSale();
    }

    /**
     * Hiển thị dialog custome sales
     */
    public void doShowCustomeSale() {
        if (mCustomeSaleDialog == null) {
            mCustomeSaleDialog = com.magestore.app.pos.util.DialogUtil.dialog(mCheckoutCustomSalePanel.getContext(),
                    mCheckoutCustomSalePanel.getContext().getString(R.string.custom_sale),
                    mCheckoutCustomSalePanel);
            mCustomeSaleDialog.setGoneButtonSave(true);
        }

        // chèn vào cart item
        CartItem cartItem = mCartService.createCustomSale();
        mCheckoutCustomSalePanel.bindItem(cartItem);
        mCustomeSaleDialog.show();
    }

    /**
     * Đặt product option panel
     */
    public void setCustomSalePanel(CheckoutCustomSalePanel panel) {
        mCheckoutCustomSalePanel = panel;
        mCheckoutCustomSalePanel.setController(this);
    }

}
