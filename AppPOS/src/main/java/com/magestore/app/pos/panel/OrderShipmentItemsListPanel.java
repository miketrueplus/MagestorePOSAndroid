package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderShipmentItemsListController;
import com.magestore.app.pos.databinding.CardOrderShipmentItemContentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderShipmentItemsListPanel extends AbstractListPanel<CartItem> {
    List<OrderItemParams> listItem;

    public OrderShipmentItemsListPanel(Context context) {
        super(context);
    }

    public OrderShipmentItemsListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderShipmentItemsListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void initLayout() {
        // Load layout view danh sách items của khách hàng
        View v = inflate(getContext(), R.layout.panel_order_shipment_item_list, null);
        addView(v);

        // Chuẩn bị layout từng item trong danh sách items
        setLayoutItem(R.layout.card_order_shipment_item_content);

        // Chuẩn bị list danh sách item
        mRecycleView = (RecyclerView) findViewById(R.id.order_shipment_items_list);
        mRecycleView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        mRecycleView.setNestedScrollingEnabled(false);

        listItem = new ArrayList<>();
    }

    @Override
    protected void bindItem(View view, CartItem item, int position) {
        CardOrderShipmentItemContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setOrderItem(item);

        CartItem i = mList.get(position);

        EditText qty_to_ship = (EditText) view.findViewById(R.id.qty_to_ship);
        actionQtyToShip(i, qty_to_ship);
        i.setOrderItemId(i.getID());
    }

    private void actionQtyToShip(final CartItem item, final EditText qty_to_ship) {
        qty_to_ship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int qty_shipped;
                try {
                    qty_shipped = Integer.parseInt(qty_to_ship.getText().toString());
                } catch (Exception e) {
                    qty_shipped = 0;
                }

                int qty = item.QtyShip();
                if (qty_shipped < 0 || qty_shipped > qty) {
                    qty_to_ship.setText(String.valueOf(qty));
                    item.setQuantity(qty);
                } else {
                    item.setQuantity(qty_shipped);
                }
            }
        });
    }

    public List<OrderItemParams> bind2List() {
        for (CartItem item : mList) {
            OrderItemParams param = ((OrderShipmentItemsListController) mController).createOrderShipmentItemParams();
            param.setOrderItemId(item.getOrderItemId());
            param.setQty(item.getQuantity());
            listItem.add(param);
        }
        return listItem;
    }
}
