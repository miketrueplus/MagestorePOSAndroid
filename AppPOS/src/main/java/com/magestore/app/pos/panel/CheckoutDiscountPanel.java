package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;

/**
 * Created by Johan on 2/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutDiscountPanel extends AbstractDetailPanel<Checkout> {
    TextView txt_sales_discount, txt_sales_promotion;
    LinearLayout ll_sales_discount, ll_sales_promotion;

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

        initValue();
    }

    @Override
    public void initValue() {
        txt_sales_discount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_sales_discount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sales_discount_bg_select));
                txt_sales_discount.setTextColor(ContextCompat.getColor(getContext(), R.color.sales_discount_text_select));
                txt_sales_promotion.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sales_discount_bg_not_select));
                txt_sales_promotion.setTextColor(ContextCompat.getColor(getContext(), R.color.sales_discount_text_not_select));
                ll_sales_discount.setVisibility(VISIBLE);
                ll_sales_promotion.setVisibility(GONE);
            }
        });

        txt_sales_promotion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_sales_promotion.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sales_discount_bg_select));
                txt_sales_promotion.setTextColor(ContextCompat.getColor(getContext(), R.color.sales_discount_text_select));
                txt_sales_discount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sales_discount_bg_not_select));
                txt_sales_discount.setTextColor(ContextCompat.getColor(getContext(), R.color.sales_discount_text_not_select));
                ll_sales_discount.setVisibility(GONE);
                ll_sales_promotion.setVisibility(VISIBLE);
            }
        });
    }
}
