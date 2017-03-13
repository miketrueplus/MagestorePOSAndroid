package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.PanelCartDetailBinding;

/**
 * Created by folio on 3/6/2017.
 */
public class CartItemDetailPanel extends AbstractDetailPanel<CartItem> {
    PanelCartDetailBinding mBinding;

    public CartItemDetailPanel(Context context) {
        super(context);
    }

    public CartItemDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khowir
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CartItemDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Khởi tạo layout
     */
    @Override
    public void initLayout() {
        // đặt layout chung cả panel
        setLayoutPanel(R.layout.panel_cart_detail);
        mBinding = DataBindingUtil.bind(getView());
        mBinding.setPanel(this);
    }

    @Override
    public void initModel() {
        super.initModel();
    }

    /**
     * Bind value vào giao diện
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        mBinding.setCartItem(item);
    }

    /**
     * Nhấn nút trừ số lượng
     * @param view
     */
    public void onAddQuantity(View view) {
        ((CartItemListController) getController()).addQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút thêm số lượng
     * @param view
     */
    public void onSubstractQuantity(View view) {
        ((CartItemListController) getController()).substractQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút tăng giá
     * @param view
     */
    public void onAddPrice(View view) {

    }

    /**
     * Nhấn nút giảm giá
     * @param view
     */
    public void onSubstractPrice(View view) {

    }

    /**
     * Nhấn nút chuyển discout sang fixed
     * @param view
     */
    public void onDiscountChangeToFixed(View view) {

    }

    /**
     * Nhấn nút chuyển discout sang percent
     * @param view
     */
    public void onDiscountChangeToPercent(View view) {

    }
}
