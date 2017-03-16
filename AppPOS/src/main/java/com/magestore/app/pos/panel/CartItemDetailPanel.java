package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.directory.Currency;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CartItemListController;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.PanelCartDetailBinding;

/**
 * Created by folio on 3/6/2017.
 */
public class CartItemDetailPanel extends AbstractDetailPanel<CartItem> {
    PanelCartDetailBinding mBinding;
    CheckoutListController mCheckoutListController;
    Button custom_dicount_money, discount_money, custom_dicount_percent, discount_percent;

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

        custom_dicount_money = (Button) findViewById(R.id.id_txt_cart_item_detail_custom_dicount_money);
        discount_money = (Button) findViewById(R.id.id_btn_cart_item_detail_discount_money);
        custom_dicount_percent = (Button) findViewById(R.id.id_txt_cart_item_detail_custom_dicount_percent);
        discount_percent = (Button) findViewById(R.id.id_btn_cart_item_detail_discount_percent);

        initValue();
    }

    private void actionChangeValue(Button money, Button percent, boolean isMoney) {
        money.setBackgroundColor(isMoney ? ContextCompat.getColor(getContext(), R.color.card_option_bg_select) : ContextCompat.getColor(getContext(), R.color.card_option_bg_not_select));
        money.setTextColor(isMoney ? ContextCompat.getColor(getContext(), R.color.card_option_text_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_not_select));
        percent.setBackgroundResource(isMoney ? R.drawable.cart_option_discount_percent_not_select : R.drawable.cart_option_discount_percent_select);
        percent.setTextColor(isMoney ? ContextCompat.getColor(getContext(), R.color.card_option_text_not_select) : ContextCompat.getColor(getContext(), R.color.card_option_text_select));
    }

    public void setCurrency(Currency currency) {
        if (currency != null) {
            String currency_symbol = currency.getCurrencySymbol();
            custom_dicount_money.setText(currency_symbol);
            discount_money.setText(currency_symbol);
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
    }

    /**
     * Nhấn nút trừ số lượng
     *
     * @param view
     */
    public void onAddQuantity(View view) {
        ((CartItemListController) getController()).addQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút thêm số lượng
     *
     * @param view
     */
    public void onSubstractQuantity(View view) {
        ((CartItemListController) getController()).substractQuantity(getItem());
        mBinding.setCartItem(getItem());
    }

    /**
     * Nhấn nút tăng giá
     *
     * @param view
     */
    public void onAddPrice(View view) {

    }

    /**
     * Nhấn nút giảm giá
     *
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

    /**
     * Nhấn nút giảm giá
     * @param view
     */
    public void onOptionClick(View view) {
        ((CartItemListController) getController()).doShowProductOptionInput();
    }

    /**
     * Nhấn nút giảm giá
     * @param view
     */
    public void onSaveClick(View view) {
        ((CartItemListController) getController()).updateToCart(bind2Item());
    }
}
