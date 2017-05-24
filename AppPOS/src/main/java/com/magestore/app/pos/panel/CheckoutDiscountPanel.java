package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.QuoteAddCouponParam;
import com.magestore.app.lib.model.checkout.SaveQuoteParam;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 2/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutDiscountPanel extends AbstractDetailPanel<Checkout> {
    TextView txt_sales_discount, txt_sales_promotion, amount_currency, amount_percent;
    LinearLayout ll_sales_discount, ll_sales_promotion;
    CheckoutListController mCheckoutListController;
    EditText discount_name, coupon_code;
    EditTextFloat discount_amount, promotion_amount;
    boolean amountType = false;
    float maximum_discount_currency, maximum_discount_percent, grand_total;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutDiscountPanel(Context context) {
        super(context);
    }

    public CheckoutDiscountPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutDiscountPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_checkout_discount, null);
        addView(view);

        txt_sales_discount = (TextView) view.findViewById(R.id.txt_sales_discount);
        txt_sales_promotion = (TextView) view.findViewById(R.id.txt_sales_promotion);

        ll_sales_discount = (LinearLayout) view.findViewById(R.id.ll_sales_discount);
        ll_sales_promotion = (LinearLayout) view.findViewById(R.id.ll_sales_promotion);

        discount_name = (EditText) view.findViewById(R.id.discount_name);
        discount_amount = (EditTextFloat) view.findViewById(R.id.discount_amount);

        amount_currency = (TextView) view.findViewById(R.id.amount_currency);
        amount_percent = (TextView) view.findViewById(R.id.amount_percent);

        coupon_code = (EditText) view.findViewById(R.id.coupon_code);
        promotion_amount = (EditTextFloat) view.findViewById(R.id.promotion_amount);

        discount_amount.setText(ConfigUtil.formatNumber(0.00));
        promotion_amount.setText(ConfigUtil.formatNumber(0.00));
    }

    @Override
    public void initValue() {
        grand_total = ConfigUtil.convertToPrice(mCheckoutListController.getSelectedItem().getGrandTotalView());

        String sym = ConfigUtil.getCurrencySymbol();
        if (sym != null)
            amount_currency.setText(ConfigUtil.getCurrencySymbol());

        if (mCheckoutListController.getMaximumDiscount() >= 0) {
            float maximum_discount = mCheckoutListController.getMaximumDiscount();
            maximum_discount_percent = maximum_discount;
            maximum_discount_currency = ((grand_total * maximum_discount) / 100);
        }

        amount_currency.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(true, amount_currency);
                changeColor(false, amount_percent);
                amountType = false;
                if (maximum_discount_percent <= 0) {
                    discount_amount.setMaxValue(grand_total);
                } else {
                    discount_amount.setMaxValue(maximum_discount_currency);
                }
            }
        });

        amount_percent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(true, amount_percent);
                changeColor(false, amount_currency);
                amountType = true;
                // maximum 100%
                if (maximum_discount_percent <= 0) {
                    discount_amount.setMaxValue(100);
                } else {
                    discount_amount.setMaxValue(maximum_discount_percent);
                }

            }
        });

        actionChangeDiscountAmount();
    }

    private void actionChangeDiscountAmount() {
        discount_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float amount = discount_amount.getValueFloat();
                if (amountType) {
                    if (amount > maximum_discount_percent) {
                        discount_amount.setText(ConfigUtil.formatNumber(maximum_discount_percent));
                    }
                } else {
                    if (amount > maximum_discount_currency) {
                        discount_amount.setText(ConfigUtil.formatNumber(maximum_discount_currency));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void onClickDiscount() {
        changeColor(true, txt_sales_discount);
        changeColor(false, txt_sales_promotion);
        ll_sales_discount.setVisibility(VISIBLE);
        ll_sales_promotion.setVisibility(GONE);
    }

    public void onClickPromotion() {
        changeColor(true, txt_sales_promotion);
        changeColor(false, txt_sales_discount);
        ll_sales_discount.setVisibility(GONE);
        ll_sales_promotion.setVisibility(VISIBLE);
    }

    private void changeColor(boolean isSelect, TextView textView) {
        textView.setBackgroundColor(isSelect ? ContextCompat.getColor(getContext(), R.color.sales_discount_bg_select) : ContextCompat.getColor(getContext(), R.color.sales_discount_bg_not_select));
        textView.setTextColor(isSelect ? ContextCompat.getColor(getContext(), R.color.sales_discount_text_select) : ContextCompat.getColor(getContext(), R.color.sales_discount_text_not_select));
    }

    public SaveQuoteParam bindSaveQuoteItem() {
        SaveQuoteParam saveQuoteParam = mCheckoutListController.createSaveQuoteParam();
        saveQuoteParam.setDiscountName(discount_name.getText().toString().trim());
        if (!amountType) {
            saveQuoteParam.setDiscountType(getContext().getString(R.string.sales_discount_currency));
        } else {
            saveQuoteParam.setDiscountType(getContext().getString(R.string.percent));
        }
        float discount_value = discount_amount.getValueFloat();
        saveQuoteParam.setDiscountValue(discount_value);
        return saveQuoteParam;
    }

    public QuoteAddCouponParam binQuoteAddCouponItem() {
        QuoteAddCouponParam quoteAddCouponParam = mCheckoutListController.createQuoteAddCouponParam();

        quoteAddCouponParam.setCouponCode(coupon_code.getText().toString());

        return quoteAddCouponParam;
    }

    public boolean checkViewDiscount() {
        if (ll_sales_discount.getVisibility() == VISIBLE) {
            return true;
        } else {
            return false;
        }
    }
}
