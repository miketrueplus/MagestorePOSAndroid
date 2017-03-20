package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelCartDetailBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by folio on 3/6/2017.
 */
public class CartItemDetailPanel extends AbstractDetailPanel<CartItem> {
    PanelCartDetailBinding mBinding;
    CheckoutListController mCheckoutListController;
    Button mbtnCustomPriceFixed, mbtnCustomPricePercent, mbtnDiscountFixed, mbtnDiscountPercent;

    EditTextFloat mtxtCustomPrice;
    EditTextFloat mtxtCustomDiscount;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CartItemDetailPanel(Context context) {
        super(context);
    }

    public CartItemDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khowir
     *
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

        mbtnCustomPriceFixed = (Button) findViewById(R.id.id_btn_cart_item_detail_custom_price_fixed);
        mbtnCustomPricePercent = (Button) findViewById(R.id.id_btn_cart_item_detail_custom_price_percent);
        mbtnDiscountFixed = (Button) findViewById(R.id.id_btn_cart_item_detail_discount_fixed);
        mbtnDiscountPercent = (Button) findViewById(R.id.id_btn_cart_item_detail_discount_percent);

        mtxtCustomPrice = (EditTextFloat) findViewById(R.id.id_txt_cart_item_detail_custom_price);
        mtxtCustomDiscount = (EditTextFloat) findViewById(R.id.id_txt_cart_item_detail_custom_discount);

        initValue();
    }

    private void actionChangeValue(Button fixed, Button percent, boolean isFixed) {
        fixed.setBackgroundColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_bg_select) : ContextCompat.getColor(getContext(), R.color.card_option_bg_not_select));
        fixed.setTextColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_text_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_not_select));
        percent.setBackgroundResource(isFixed ? R.color.card_option_bg_not_select : R.color.card_option_bg_select);
        percent.setTextColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_text_not_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_select));
    }

    public void setCurrency(Currency currency) {
        if (currency != null) {
            String currency_symbol = currency.getCurrencySymbol();
            mbtnCustomPriceFixed.setText(currency_symbol);
            mbtnDiscountFixed.setText(currency_symbol);
        }
    }

    @Override
    public void initModel() {
        super.initModel();
    }

    /**
     * Bind value vào giao diện
     *
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        mBinding.setCartItem(item);
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, item.isCustomPriceTypeFixed());
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, item.isDiscountTypeFixed());

    }

    @Override
    public void bind2Item(CartItem item) {
        item.setCustomPrice(mtxtCustomPrice.getValueFloat());
        item.setDiscountAmount(mtxtCustomDiscount.getValueFloat());
        item.setUnitPrice(item.isCustomPriceTypeFixed() ? mtxtCustomPrice.getValueFloat() : item.getOriginalPrice() * mtxtCustomPrice.getValueFloat() / 100);
    }

    /**
     * Nhấn nút trừ số lượng
     *
     * @param view
     */
    public void onAddQuantity(View view) {
        bindItem(bind2Item());
        ((CartItemListController) getController()).addQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút thêm số lượng
     *
     * @param view
     */
    public void onSubstractQuantity(View view) {
        bindItem(bind2Item());
        ((CartItemListController) getController()).substractQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút chuyển discout sang fixed
     *
     * @param view
     */
    public void onDiscountChangeToFixed(View view) {
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, true);
        getItem().setDiscountTypeFixed();
    }

    /**
     * Nhấn nút chuyển discout sang percent
     *
     * @param view
     */
    public void onDiscountChangeToPercent(View view) {
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, false);
        getItem().setDiscountTypePercent();

    }

    /**
     * Nhấn nút chuyển discout sang fixed
     *
     * @param view
     */
    public void onCustomPriceChangeToFixed(View view) {
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, true);
        getItem().setCustomPriceTypeFixed();
    }

    /**
     * Nhấn nút chuyển discout sang percent
     *
     * @param view
     */
    public void onCustomPriceChangeToPercent(View view) {
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, false);
        getItem().setCustomPriceTypePercent();
    }

    /**
     * Nhấn nút giảm giá
     *
     * @param view
     */
    public void onOptionClick(View view) {
        ((CartItemListController) getController()).doShowProductOptionInput();
    }

    /**
     * Nhấn nút giảm giá
     *
     * @param view
     */
    public void onSaveClick(View view) {
        ((CartItemListController) getController()).updateToCart(bind2Item());
    }
}
