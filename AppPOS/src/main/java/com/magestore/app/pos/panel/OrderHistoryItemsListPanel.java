package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardOrderHistoryItemContentBinding;

/**
 * Created by Johan on 1/16/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderHistoryItemsListPanel extends AbstractListPanel<CartItem> {

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
    protected void bindItem(View view, CartItem item, int position) {
        CardOrderHistoryItemContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderItem(item);
    }
}
