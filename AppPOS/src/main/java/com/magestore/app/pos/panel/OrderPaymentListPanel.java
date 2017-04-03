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

    @Override
    protected void bindItem(View view, OrderWebposPayment item, int position) {
        CardOrderPaymentContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderWebposPayment(item);
    }
}
