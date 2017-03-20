package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;

/**
 * Created by Johan on 3/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutCustomSalePanel extends AbstractDetailPanel<CartItem> {
    public CheckoutCustomSalePanel(Context context) {
        super(context);
    }

    public CheckoutCustomSalePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutCustomSalePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_checkout_custom_sale, null);
        addView(view);
    }
}
