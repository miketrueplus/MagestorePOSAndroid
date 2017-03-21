package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.databinding.PanelCheckoutCustomSaleBinding;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 3/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutCustomSalePanel extends AbstractDetailPanel<CartItem> {
    PanelCheckoutCustomSaleBinding mBinding;
    EditTextFloat mtxtPrice;
    EditText mtxtName;

    /**
     * Khởi tạo
     * @param context
     */
    public CheckoutCustomSalePanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     */
    public CheckoutCustomSalePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CheckoutCustomSalePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Chuẩn bị layout
     */
    @Override
    protected void initLayout() {
        setLayoutPanel(R.layout.panel_checkout_custom_sale);
//        View view = inflate(getContext(), R.layout.panel_checkout_custom_sale, null);
//        addView(view);

        mtxtPrice = (EditTextFloat) getView().findViewById(R.id.id_txt_custom_sale_price);
        mtxtName = (EditText) getView().findViewById(R.id.id_txt_custom_sale_name);

        mBinding = DataBindingUtil.bind(getView());
        mBinding.setPanel(this);
    }

    /**
     * Chuyển từ dataset sang giao diện
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        mBinding.setCartItem(item);
        mBinding.setProduct(item.getProduct());
    }

    /**
     * Chuyển từ giao diện sang data set
     * @param item
     */
    @Override
    public void bind2Item(CartItem item) {
        item.setQuantity(1);
        item.setTypeCustom();
        item.setPrice(mtxtPrice.getValueFloat());
        item.setUnitPrice(mtxtPrice.getValueFloat());
        item.setCustomPrice(mtxtPrice.getValueFloat());
        item.setOriginalPrice(mtxtPrice.getValueFloat());
        item.getProduct().setName(mtxtName.getText().toString().trim());
        super.bind2Item(item);
    }

    /**
     * Kiểm tra nhập vào
     * @return
     */
    public boolean validateInput() {
        if (mtxtName.getText().toString().trim().equals(StringUtil.STRING_EMPTY)) {
            mtxtName.setError(getResources().getString(R.string.err_field_required));
            return false;
        }
        return true;
    }

    /**
     * Nhất nút save
     * @param v
     */
    public void onSaveClick(View v) {
        if (!validateInput()) return;
        ((CartItemListController) getController()).updateToCart(bind2Item());
    }
}