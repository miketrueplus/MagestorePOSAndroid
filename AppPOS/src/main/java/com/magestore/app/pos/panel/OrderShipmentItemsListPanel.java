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

import com.magestore.app.lib.model.checkout.cart.Items;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.CardOrderShipmentItemContentBinding;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderShipmentItemsListPanel extends AbstractListPanel<Items> {
    private EditText qty_to_ship;

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
    }

    @Override
    protected void bindItem(View view, Items item, int position) {
        CardOrderShipmentItemContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setOrderItem(item);

        qty_to_ship = (EditText) view.findViewById(R.id.qty_to_ship);
        actionQtyToShip(item);
    }

    private void actionQtyToShip(final Items item) {
        qty_to_ship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int qty_shipped = i;
                int qty = item.QtyShip();
                if (qty_shipped < 0 || qty_shipped > qty) {
                    qty_to_ship.setText(qty);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
