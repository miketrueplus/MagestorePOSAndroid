package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
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
}
