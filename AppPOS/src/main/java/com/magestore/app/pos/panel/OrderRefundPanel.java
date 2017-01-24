package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.PanelOrderRefundBinding;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderRefundPanel extends AbstractDetailPanel<Order> {
    PanelOrderRefundBinding mBinding;

    public OrderRefundPanel(Context context) {
        super(context);
    }

    public OrderRefundPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderRefundPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_refund, null);
        addView(view);

        mBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrder(item);
    }
}
