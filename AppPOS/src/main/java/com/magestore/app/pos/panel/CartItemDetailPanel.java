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
import android.widget.LinearLayout;
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
import com.magestore.app.view.EditTextDecimal;
import com.magestore.app.view.EditTextFloat;
import com.magestore.app.view.EditTextQuantity;

/**
 * Created by folio on 3/6/2017.
 */
public class CartItemDetailPanel extends AbstractDetailPanel<CartItem> {
    PanelCartDetailBinding mBinding;
    CheckoutListController mCheckoutListController;
    Button mbtnCustomPriceFixed, mbtnCustomPricePercent, mbtnDiscountFixed, mbtnDiscountPercent;

    EditTextFloat mtxtCustomPrice;
    EditTextFloat mtxtCustomDiscount;
    EditTextQuantity mtxtQuantity;

    boolean mblnCustomPriceFixed;
    boolean mblnCustomDiscountFixed;

    boolean changePrice = false;
    boolean checkDiscount = false;

    LinearLayout ll_custom_price, ll_discount;

    CartItem mCartItem;
    float maximum_discount_currency;

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

        mtxtQuantity = (EditTextQuantity) findViewById(R.id.id_txt_cart_item_detail_quantity);
        mtxtQuantity.setOptionCart(true);
        mtxtCustomPrice = (EditTextFloat) findViewById(R.id.id_txt_cart_item_detail_custom_price);
        mtxtCustomDiscount = (EditTextFloat) findViewById(R.id.id_txt_cart_item_detail_custom_discount);

        ll_custom_price = (LinearLayout) findViewById(R.id.ll_custom_price);
        ll_discount = (LinearLayout) findViewById(R.id.ll_discount);

        initValue();
    }

    /**
     * Đặt đơn vị là % hay $
     *
     * @param fixed
     * @param percent
     * @param isFixed
     */
    private void actionChangeValue(Button fixed, Button percent, final boolean isFixed) {
        fixed.setBackgroundColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_bg_select) : ContextCompat.getColor(getContext(), R.color.card_option_bg_not_select));
        fixed.setTextColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_text_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_not_select));
        percent.setBackgroundResource(isFixed ? R.color.card_option_bg_not_select : R.color.card_option_bg_select);
        percent.setTextColor(isFixed ? ContextCompat.getColor(getContext(), R.color.card_option_text_not_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_select));

        mtxtCustomDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float value = mtxtCustomDiscount.getValueFloat();
                if (checkDiscount) {
                    if (value > mCheckoutListController.getMaximumDiscount()) {
                        mtxtCustomDiscount.setError(getContext().getString(R.string.err_discount, ("%" + mCheckoutListController.getMaximumDiscount())));
                        mtxtCustomDiscount.setText(ConfigUtil.formatNumber(mCheckoutListController.getMaximumDiscount()));
                        mtxtCustomDiscount.dismisPopup();
                    }
                } else {
                    if (value > maximum_discount_currency) {
                        mtxtCustomDiscount.setError(getContext().getString(R.string.err_discount, (ConfigUtil.formatPrice(maximum_discount_currency))));
                        mtxtCustomDiscount.setText(ConfigUtil.formatNumber(maximum_discount_currency));
                        mtxtCustomDiscount.dismisPopup();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Đơn vị tiền tệ
     */
    public void setCurrency() {
        String sym = ConfigUtil.getCurrentCurrency().getCurrencySymbol();
        if (sym != null) {
//            String currency_symbol = currency.getCurrencySymbol();
            mbtnCustomPriceFixed.setText(sym);
            mbtnDiscountFixed.setText(sym);
        }
    }

    @Override
    public void initModel() {
        setCurrency();
    }

    /**
     * Bind value vào giao diện
     *
     * @param item
     */
    @Override
    public void bindItem(CartItem item) {
        super.bindItem(item);
        if (ConfigUtil.isManageAllDiscount()) {
            ll_custom_price.setVisibility(VISIBLE);
            ll_discount.setVisibility(VISIBLE);
        } else {
            ll_discount.setVisibility(ConfigUtil.isDiscountPerItem() ? VISIBLE : GONE);
            ll_custom_price.setVisibility(ConfigUtil.isApplyCustomPrice() ? VISIBLE : GONE);
        }
        mBinding.setCartItem(item);
        mCartItem = item;
        maximum_discount_currency = ((mCartItem.getDefaultCustomPrice() * mCheckoutListController.getMaximumDiscount()) / 100);

        // đặt % hau $
        mblnCustomPriceFixed = item.isCustomPriceTypeFixed();
        mblnCustomDiscountFixed = item.isDiscountTypeFixed();
        actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, mblnCustomPriceFixed);
        actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, mblnCustomDiscountFixed);

        mtxtCustomPrice.setText(item.isCustomPrice() ? ConfigUtil.formatNumber(item.getCustomPrice()) : ConfigUtil.formatNumber(ConfigUtil.convertToPrice(item.getDefaultCustomPrice())));

        if (item.getProduct() != null) {
            mtxtQuantity.setDecimal(item.getProduct().isDecimal());
        }

        // nhỏ nhất cho ô số lượng
        mtxtQuantity.setMinValue(item.getProduct().getQuantityIncrement());
        mtxtQuantity.setError(null);
        mtxtCustomDiscount.setError(null);
        mtxtCustomPrice.setError(null);

        // giấu nút option nếu k0 có product option
        findViewById(R.id.id_btn_cart_option).setVisibility(item.getProduct().haveProductOption() ? View.VISIBLE : View.GONE);
        actionChangeCustomDiscount(item);
        actionChangeCustomPrice(item);
    }

    private void actionChangeCustomDiscount(final CartItem cartItem) {
        mtxtCustomDiscount.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mblnCustomPriceFixed = true;
                    actionChangeValue(mbtnCustomPriceFixed, mbtnCustomPricePercent, mblnCustomPriceFixed);
                    mtxtCustomPrice.setText(ConfigUtil.formatNumber(ConfigUtil.convertToPrice(mCartItem.getDefaultCustomPrice())));
                }
            }
        });
    }

    private void actionChangeCustomPrice(final CartItem cartItem) {
        mtxtCustomPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mblnCustomDiscountFixed = true;
                    actionChangeValue(mbtnDiscountFixed, mbtnDiscountPercent, mblnCustomDiscountFixed);
                    mtxtCustomDiscount.setText(ConfigUtil.formatNumber(ConfigUtil.convertToPrice(0)));
                }
            }
        });
    }

    /**
     * Bind từ giao diện lên data set
     *
     * @param item
     */
    @Override
    public void bind2Item(CartItem item) {
        if (mtxtCustomPrice.getValueFloat() != item.getCustomPrice() || mtxtCustomDiscount.getValueFloat() != item.getDiscountAmount()) {
            item.setIsCustomPrice(true);
        }
        item.setCustomPrice(mtxtCustomPrice.getValueFloat());
        item.setDiscountAmount(mtxtCustomDiscount.getValueFloat());
        item.setUnitPrice(mblnCustomPriceFixed ? ConfigUtil.convertToBasePrice(mtxtCustomPrice.getValueFloat()) : item.getOriginalPrice() * mtxtCustomPrice.getValueFloat() / 100);
        item.setUnitPrice(mblnCustomDiscountFixed ? item.getUnitPrice() - ConfigUtil.convertToBasePrice(mtxtCustomDiscount.getValueFloat()) : item.getUnitPrice() - (item.getUnitPrice() * mtxtCustomDiscount.getValueFloat() / 100));
        item.setQuantity(mtxtQuantity.getValueFloat());
        item.setPriceShowView(item.getPrice());
        // đặt loại với custom price
        if (mblnCustomPriceFixed) item.setCustomPriceTypeFixed();
        else item.setCustomPriceTypePercent();

        // đặt loại với custom discount
        if (mblnCustomDiscountFixed) item.setDiscountTypeFixed();
        else item.setDiscountTypePercent();
    }

    /**
     * Nhấn nút trừ số lượng
     *
     * @param view
     */
    public void onAddQuantity(View view) {
        mtxtQuantity.add(getItem().getProduct().getQuantityIncrement());
    }

    /**
     * Nhấn nút thêm số lượng
     *
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
        changePrice = false;
        if (mCartItem.isCustomPrice()) {
            mtxtCustomDiscount.setText(ConfigUtil.formatNumber(mCartItem.getDiscountAmount()));
        } else {
            mtxtCustomDiscount.setText(ConfigUtil.formatNumber(ConfigUtil.convertToPrice(mCartItem.getDiscountAmount())));
        }
        checkDiscount = false;
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
        changePrice = false;
        checkDiscount = true;
        mtxtCustomDiscount.setText(ConfigUtil.formatNumber(0));
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
        changePrice = false;
        if (mCartItem.isCustomPrice()) {
            mtxtCustomPrice.setText(ConfigUtil.formatNumber(mCartItem.getCustomPrice()));
        } else {
            mtxtCustomPrice.setText(ConfigUtil.formatNumber(ConfigUtil.convertToPrice(mCartItem.getDefaultCustomPrice())));
        }
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
        changePrice = false;
        mtxtCustomPrice.setText(ConfigUtil.formatNumber(0));
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
     *
     * @return
     */
    private boolean validateInput() {
        boolean blnRight = true;
        // valid số lượng phải lớn hơn mức tối thiểu
        float quantity = mtxtQuantity.getValueFloat();
        if (!getItem().isTypeCustom()) {
            if (quantity < getItem().getProduct().getAllowMinQty()) {
                mtxtQuantity.setError(String.format(getResources().getString(R.string.err_field_must_greater_than), ConfigUtil.formatQuantity(getItem().getProduct().getAllowMinQty())));
                blnRight = false;
            }

            // valid số lượng phải lớn hơn mức được phép trong kho
            if (quantity > getItem().getProduct().getAllowMaxQty() && getItem().getProduct().getAllowMaxQty() > getItem().getProduct().getAllowMinQty()) {
                mtxtQuantity.setError(String.format(getResources().getString(R.string.err_field_must_less_than), ConfigUtil.formatQuantity(getItem().getProduct().getAllowMaxQty())));
                blnRight = false;
            }
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
