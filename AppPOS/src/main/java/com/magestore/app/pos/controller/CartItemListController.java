package com.magestore.app.pos.controller;

import android.text.TextUtils;
import android.view.View;

import com.magestore.app.lib.controller.AbstractChildListController;
import com.magestore.app.lib.controller.AbstractController;
import com.magestore.app.lib.controller.ListController;
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
import com.magestore.app.pos.panel.CustomerDetailPanel;
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
     *
     * @param show
     */
    @Override
    public void doShowDetailPanel(boolean show) {
//        if (!getSelectedItem().getProduct().haveProductOption()) {
        // khởi tạo và hiển thị dialog
        if (mCartItemDetailDialog == null) {
            mCartItemDetailDialog = com.magestore.app.pos.util.DialogUtil.dialog(getDetailView().getContext(),
                    getDetailView().getContext().getString(R.string.product_option),
                    getDetailView());
        }
        mCartItemDetailDialog.setTitle(getSelectedItem().getProduct().getName());
        mCartItemDetailDialog.setDialogTitle(getSelectedItem().getProduct().getName());
        mCartItemDetailDialog.show();

        // Xử lý khi nhấn save trên dialog
        mCartItemDetailDialog.getButtonSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToCart(getDetailView().bind2Item());
            }
        });

        // Xử lý khi nhấn cancel trên dialog
        mCartItemDetailDialog.getButtonCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToCart(getDetailView().bind2Item());
            }
        });
//        }
//        else {
//            doShowProductOptionInput(getSelectedItem());
//        }
    }

    /**
     * Cập nhật cart item hiện tại. Tắt các dialog
     *
     * @param cartItem
     */
    public void updateToCart(CartItem cartItem) {
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
        getView().updateModelToFirstInsertIfNotFound(cartItem);
        if (mCartItemDetailDialog != null && mCartItemDetailDialog.isShowing())
            mCartItemDetailDialog.dismiss();
        if (mProductOptionDialog != null && mProductOptionDialog.isShowing())
            mProductOptionDialog.dismiss();
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
        getView().updateModelToFirstInsertIfNotFound(cartItem);
        if (mCartItemDetailDialog != null && mCartItemDetailDialog.isShowing())
            mCartItemDetailDialog.dismiss();
        if (mProductOptionDialog != null && mProductOptionDialog.isShowing())
            mProductOptionDialog.dismiss();
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
     * Clear các chosen
     */
//    public void clearProductOptionChosen() {
//        if (getItem() == null) return;
//        if (getItem().getProduct() == null) return;
//        if (getItem().getProduct().getProductOption() == null) return;
//        if (getItem().getProduct().getProductOption().getCustomOptions() == null) return;
//
//        // clear các chosen
//        for (ProductOptionCustom productCustomOption : getItem().getProduct().getProductOption().getCustomOptions()) {
//            if (productCustomOption.getOptionValueList() == null) continue;
//            for (ProductOptionCustomValue customValue : productCustomOption.getOptionValueList()) {
//                customValue.setChosen(false);
//            }
//        }
//    }

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
//        if (cartItem.getChooseProductOptions() == null) return;
//
//        // clear các chosen
//        for (ProductOptionCustom optionCustom : cartItem.getChooseProductOptions().keySet()) {
//            if (cartItem.getChooseProductOptions().get(optionCustom) == null) continue;
//            for (ProductOptionCustomValue optionCustomValue : cartItem.getChooseProductOptions().get(optionCustom).productOptionCustomValueList) {
//                optionCustomValue.setChosen(true);
//            }
//        }
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
            mProductOptionDialog = com.magestore.app.pos.util.DialogUtil.dialog(mProductOptionPanel.getContext(),
                    cartItem.getProduct().getName(),
                    mProductOptionPanel);
        }
        mProductOptionDialog.setGoneButtonSave(true);
        // clear list option và hiện thị thông tin product và cart item
        setProductOptionChosen(cartItem);
        mProductOptionPanel.clearList();
        mProductOptionPanel.showCartItemInfo(cartItem);
        mProductOptionDialog.show();

        // Xử lý khi nhấn save trên dialog
        mProductOptionDialog.getButtonSave().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(mProductOptionPanel.bind2Item());
            }
        });

        // gán cart item và load product option
        if (cartItem.getProduct().getProductOption() != null) bindItem(cartItem);
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
}
