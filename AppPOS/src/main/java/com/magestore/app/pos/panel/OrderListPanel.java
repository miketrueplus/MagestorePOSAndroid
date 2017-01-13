package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;

import java.util.List;

/**
 * Panel hiển thị danh sách đơn hàng chi tiết
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class OrderListPanel extends AbstractListPanel<Order> {
    // Danh sách đơn hàng
    List<Order> mListOrder;

    /**
     * Khởi tạo
     * @param context
     */
    public OrderListPanel(Context context) {
        super(context);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     */
    public OrderListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Khởi tạo
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public OrderListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách đơn hàng
        View v = inflate(getContext(), R.layout.panel_order_list, null);
        addView(v);

        // Xử lý sự kiện floating action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void bindItem(View view, Order item, int position) {

    }


}
