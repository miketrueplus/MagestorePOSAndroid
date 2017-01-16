package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardOrderHistoryItemContentBinding;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryItemsListPanel extends AbstractListPanel<Items> {

    /**
     * Các hàm khởi tạo
     *
     * @param context
     */
    public OrderHistoryItemsListPanel(Context context) {
        super(context);
    }

    public OrderHistoryItemsListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderHistoryItemsListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách items của khách hàng
        View v = inflate(getContext(), R.layout.panel_order_history_items_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách items
        setLayoutItem(R.layout.card_order_history_item_content);

        // Chuẩn bị list danh sách item
        mRecycleView = (RecyclerView) findViewById(R.id.order_items_list);
        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mRecycleView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void bindItem(View view, Items item, int position) {
        CardOrderHistoryItemContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderItem(item);
    }
}
