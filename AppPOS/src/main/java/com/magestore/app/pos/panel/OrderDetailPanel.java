package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class OrderDetailPanel extends AbstractDetailPanel<Order> {

    public OrderDetailPanel(Context context) {
        super(context);
        init();
    }

    public OrderDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OrderDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initControlLayout();
        initControlValue();
        initTask();
    }

    private void initControlLayout() {
        // Load layout view danh sách khách hàng
        View v = inflate(getContext(), R.layout.panel_order_detail, null);
        addView(v);
    }

    private void initControlValue() {

    }

    private void initTask() {

    }
}
