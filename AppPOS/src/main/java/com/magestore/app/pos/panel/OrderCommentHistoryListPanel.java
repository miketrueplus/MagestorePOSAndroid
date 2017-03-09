package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public void initLayout() {
        // Load layout view danh sách comment của khách hàng
        View v = inflate(getContext(), R.layout.panel_order_comment_history_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách comment
        setLayoutItem(R.layout.card_order_comment_history_content);

        // Chuẩn bị list danh sách comment
//        mRecycleView = (RecyclerView) findViewById(R.id.order_comment_history_list);
//        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
//        mRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void bindItem(View view, OrderStatus item, int position) {
        CardOrderCommentHistoryContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderStatus(item);
    }
}
