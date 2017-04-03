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
import com.magestore.app.view.EditTextInteger;

/**
 * Created by folio on 3/6/2017.
 */
public class CartItemDetailPanel extends AbstractDetailPanel<CartItem> {
    PanelCartDetailBinding mBinding;
    CheckoutListController mCheckoutListController;
    Button mbtnCustomPriceFixed, mbtnCustomPricePercent, mbtnDiscountFixed, mbtnDiscountPercent;

    EditTextFloat mtxtCustomPrice;
    EditTextFloat mtxtCustomDiscount;
    EditTextInteger mtxtQuantity;

    boolean mblnCustomPriceFixed;
    boolean mblnCustomDiscountFixed;

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

        mtxtQuantity = (EditTextInteger) findViewById(R.id.id_txt_cart_item_detail_quantity);
        mtxtCustomPrice = (EditTextFloat) findViewById(R.id.id_txt_cart_item_detail_custom_price);
        mtxtCustomDiscount = (EditTextFloat) findViewById(R.id.id_txt_cart_item_detail_custom_discount);

        initValue();
    }

    /**
     * Đặt đơn vị là % hay $
     * @param fixed
     * @param percent
     * @param isFixed
     */
    private void actionChangeValue(Button fixed, Button percent, boolean isFixed) {
        fixed.setBackgroundColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_bg_select) : ContextCompat.getColor(getContext(), R.color.card_option_bg_not_select));
        fixed.setTextColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_text_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_not_select));
        percent.setBackgroundResource(isFixed ? R.color.card_option_bg_not_select : R.color.card_option_bg_select);
        percent.setTextColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_text_not_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_select));
    }

    /**
     * Đơn vị tiền tệ
     * @param currency
     */
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

        // đặt % hau $
        mblnCustomPriceFixed = item.isCustomPriceTypeFixed();
        mblnCustomDiscountFixed = item.isDiscountTypeFixed();
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, mblnCustomPriceFixed);
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, mblnCustomDiscountFixed);

        // nhỏ nhất cho ô số lượng
        mtxtQuantity.setMinValue(item.getProduct().getQuantityIncrement());
        mtxtQuantity.setError(null);
        mtxtCustomDiscount.setError(null);
        mtxtCustomPrice.setError(null);

        // giấu nút option nếu k0 có product option
        findViewById(R.id.id_btn_cart_option).setVisibility(item.getProduct().haveProductOption() ? View.VISIBLE : View.GONE);

    }

    /**
     * Bind từ giao diện lên data set
     * @param item
     */
    @Override
    public void bind2Item(CartItem item) {
        item.setCustomPrice(mtxtCustomPrice.getValueFloat());
        item.setDiscountAmount(mtxtCustomDiscount.getValueFloat());
        item.setUnitPrice(mblnCustomPriceFixed ? mtxtCustomPrice.getValueFloat() : item.getOriginalPrice() * mtxtCustomPrice.getValueFloat() / 100);
        item.setUnitPrice(mblnCustomDiscountFixed ? item.getUnitPrice() - mtxtCustomDiscount.getValueFloat() : item.getUnitPrice() - item.getUnitPrice() * mtxtCustomDiscount.getValueFloat() / 100);
        item.setQuantity(mtxtQuantity.getValueInteger());

        // đặt loại với custom price
        if (mblnCustomPriceFixed) item.setCustomPriceTypeFixed();
        else item.setCustomPriceTypePercent();

        // đặt loại với custom discount
        if (mblnCustomDiscountFixed) item.setDiscountTypeFixed();
        else item.setDiscountTypePercent();
    }

    /**
     * Nhấn nút trừ số lượng
     * @param view
     */
    public void onAddQuantity(View view) {
        mtxtQuantity.add(getItem().getProduct().getQuantityIncrement());
    }

    /**
     * Nhấn nút thêm số lượng
     * @param view
     */
    public void onSubstractQuantity(View view) {
        mtxtQuantity.substract(getItem().getProduct().getQuantityIncrement());
    }

    /**
     * Nhấn nút chuyển discout sang fixed
     *
     * @param view
     */
    public void onDiscountChangeToFixed(View view) {
        mblnCustomDiscountFixed = true;
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, true);
//        getItem().setDiscountTypeFixed();
    }

    /**
     * Nhấn nút chuyển discout sang percent
     *
     * @param view
     */
    public void onDiscountChangeToPercent(View view) {
        mblnCustomDiscountFixed = false;
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, false);
//        getItem().setDiscountTypePercent();
    }

    /**
     * Nhấn nút chuyển discout sang fixed
     *
     * @param view
     */
    public void onCustomPriceChangeToFixed(View view) {
        mblnCustomPriceFixed = true;
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, true);
//        getItem().setCustomPriceTypeFixed();
    }

    /**
     * Nhấn nút chuyển discout sang percent
     *
     * @param view
     */
    public void onCustomPriceChangeToPercent(View view) {
        mblnCustomPriceFixed = false;
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, false);
//        getItem().setCustomPriceTypePercent();
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
        if (!validateInput()) return;
        ((CartItemListController) getController()).updateToCart(bind2Item());
    }

    /**
     * Kiểm tra số liệu đầu vào, đảm bảo nhập chính xác
     * @return
     */
    private boolean validateInput() {
        boolean blnRight = true;
        // valid số lượng phải lớn hơn mức tối thiểu
        int quantity = mtxtQuantity.getValueInteger();
        if (quantity < getItem().getProduct().getAllowMinQty()) {
            mtxtQuantity.setError(String.format(getResources().getString(R.string.err_field_must_greater_than), ConfigUtil.formatQuantity(getItem().getProduct().getAllowMinQty())));
            blnRight = false;
        }

        // valid số lượng phải lớn hơn mức được phép trong kho
        if (quantity > getItem().getProduct().getAllowMaxQty()) {
            mtxtQuantity.setError(String.format(getResources().getString(R.string.err_field_must_less_than), ConfigUtil.formatQuantity(getItem().getProduct().getAllowMaxQty())));
            blnRight = false;
        }

        // valid custom price
        if (mtxtCustomPrice.getValueFloat() < 0.0f) {
            mtxtCustomPrice.setError(String.format(getResources().getString(R.string.err_field_must_greater_than), ConfigUtil.formatPrice(0.0f)));
            blnRight = false;
        }

        // valid discount
        if (mblnCustomDiscountFixed && mtxtCustomDiscount.getValueFloat() < 0.0f) {
            mtxtCustomDiscount.setError(String.format(getResources().getString(R.string.err_field_must_greater_than), ConfigUtil.formatNumber(0.0f)));
            blnRight = false;
        }

        // valid custom discount k0 được
        if (mblnCustomDiscountFixed) {
            float tempPrice = mblnCustomPriceFixed ? mtxtCustomPrice.getValueFloat() : getItem().getOriginalPrice() * mtxtCustomPrice.getValueFloat() / 100;
            if (mtxtCustomDiscount.getValueFloat() > tempPrice) {
                mtxtCustomDiscount.setError(String.format(getResources().getString(R.string.err_field_must_less_than), ConfigUtil.formatNumber(tempPrice)));
                blnRight = false;
            }
        }

        // valid % của discount k0 được hơn 100%
        if (!mblnCustomDiscountFixed && mtxtCustomDiscount.getValueFloat() > 100) {
            mtxtCustomDiscount.setError(String.format(getResources().getString(R.string.err_field_must_less_than), ConfigUtil.formatNumber(100)));
            blnRight = false;
        }
        return blnRight;
    }
}
