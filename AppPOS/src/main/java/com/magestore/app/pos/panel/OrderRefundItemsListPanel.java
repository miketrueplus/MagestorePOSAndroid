package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderRefundItemsListController;
import com.magestore.app.pos.databinding.CardOrderRefundItemContentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/25/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderRefundItemsListPanel extends AbstractListPanel<CartItem> {
    List<OrderItemParams> listItem;
    private static String RETURN_TO_STOCK = "back_to_stock";

    public OrderRefundItemsListPanel(Context context) {
        super(context);
    }

    public OrderRefundItemsListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderRefundItemsListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách items của khách hàng
        View v = inflate(getContext(), R.layout.panel_order_refund_item_list, null);
        addView(v);

        initListView(R.id.order_refund_items_list);

        // Chuẩn bị layout từng item trong danh sách items
        setLayoutItem(R.layout.card_order_refund_item_content);

        // Chuẩn bị list danh sách item
//        mRecycleView = (RecyclerView) findViewById(R.id.order_refund_items_list);
//        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
//        mRecycleView.setNestedScrollingEnabled(false);

        listItem = new ArrayList<>();
    }

    @Override
    protected void bindItem(View view, CartItem item, int position) {
        CardOrderRefundItemContentBinding binding = DataBindingUtil.bind(view);
        binding.setOrderItem(item);

        EditText edt_qty_to_refund = (EditText) view.findViewById(R.id.qty_to_refund);
        CheckBox cb_return_to_stock = (CheckBox) view.findViewById(R.id.return_to_stock);

        actionQtyToRefund(item, edt_qty_to_refund);
        item.setOrderItemId(item.getItemId());
        actionReturnToStock(item, cb_return_to_stock);
    }

    private void actionQtyToRefund(final CartItem item, final EditText qty_to_refund) {
        qty_to_refund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int qty_refunded;
                try {
                    qty_refunded = Integer.parseInt(qty_to_refund.getText().toString());
                } catch (Exception e) {
                    qty_refunded = 0;
                }

                int qty = item.QtyRefund();
                if (qty_refunded < 0 || qty_refunded > qty) {
                    qty_to_refund.setText(String.valueOf(qty));
                    item.setQuantity(qty);
                } else {
                    item.setQuantity(qty_refunded);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionReturnToStock(final CartItem item, final CheckBox cb_return_to_stock) {
        cb_return_to_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean cb_return_to_stock) {
                if (cb_return_to_stock) {
                    item.setReturnToStock(RETURN_TO_STOCK);
                }
            }
        });
    }

    public List<OrderItemParams> bind2List() {
        List<CartItem> listCartItems = ((OrderRefundItemsListController) mController).getSelectedItems();
        if (listCartItems != null && listCartItems.size() > 0) {
            for (CartItem item : listCartItems) {
                OrderItemParams param = ((OrderRefundItemsListController) mController).createOrderRefundItemParams();
                param.setOrderItemId(item.getOrderItemId());
                param.setQty(item.getQuantity());
                param.setAdditionalData(item.getReturnToStock());
                listItem.add(param);
            }
        }
        return listItem;
    }
}