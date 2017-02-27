package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.sales.OrderWebposPayment;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardOrderPaymentContentBinding;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderPaymentListPanel extends AbstractListPanel<OrderWebposPayment> {

    /**
     * Các hàm khởi tạo
     *
     * @param context
     */
    public OrderPaymentListPanel(Context context) {
        super(context);
    }

    public OrderPaymentListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderPaymentListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Chuẩn bị layout
     */
    @Override
    public void initLayout() {
        // Load layout view danh sách payment của khách hàng
        View v = inflate(getContext(), R.layout.panel_order_payment_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách đơn hàng
        setLayoutItem(R.layout.card_order_payment_content);

        // Chuẩn bị list danh sách payment
//        mRecycleView = (RecyclerView) findViewById(R.id.order_payment_list);
//        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void bindItem(View view, OrderWebposPayment item, int position) {
        CardOrderPaymentContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderWebposPayment(item);
    }
}
