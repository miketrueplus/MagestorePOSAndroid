package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardOrderCommentHistoryContentBinding;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderCommentHistoryListPanel extends AbstractListPanel<OrderStatus> {

    /**
     * Các hàm khởi tạo
     *
     * @param context
     */
    public OrderCommentHistoryListPanel(Context context) {
        super(context);
    }

    public OrderCommentHistoryListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderCommentHistoryListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void bindItem(View view, OrderStatus item, int position) {
        CardOrderCommentHistoryContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderStatus(item);
    }
}
