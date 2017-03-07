package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;

/**
 * Created by Johan on 3/7/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutSuccessPanel extends AbstractDetailPanel<Order> {
    TextView txt_order_id;
    Button btn_new_order;
    CheckoutListController mCheckoutListController;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

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
    protected void initLayout() {
        super.initLayout();
        txt_order_id = (TextView) findViewById(R.id.order_id);
        btn_new_order = (Button) findViewById(R.id.btn_new_order);
        initValue();
    }

    @Override
    public void initValue() {
        btn_new_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCheckoutListController.actionNewOrder();
            }
        });
    }

    @Override
    public void bindItem(Order item) {
        super.bindItem(item);
        txt_order_id.setText(getContext().getString(R.string.checkout_order_id, item.getIncrementId()));
    }
}
