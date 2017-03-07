package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;

/**
 * Created by Johan on 3/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutSuccessPanel extends AbstractDetailPanel<Order> {

    public CheckoutSuccessPanel(Context context) {
        super(context);
    }

    public CheckoutSuccessPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutSuccessPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void bindItem(Order item) {
        super.bindItem(item);
    }
}
