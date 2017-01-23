package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderShipmentItemsListController;
import com.magestore.app.pos.databinding.PanelOrderShipmentBinding;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderShipmentPanel extends AbstractDetailPanel<Order> {
    PanelOrderShipmentBinding mBinding;
    OrderShipmentItemsListPanel mOrderShipmentItemsListPanel;
    OrderShipmentItemsListController mOrderShipmentItemsListController;

    public OrderShipmentPanel(Context context) {
        super(context);
    }

    public OrderShipmentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderShipmentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_shipment, null);
        addView(view);

        mOrderShipmentItemsListPanel = (OrderShipmentItemsListPanel) findViewById(R.id.order_shipment_items);

        mBinding = DataBindingUtil.bind(view);

        initModel();
    }

    @Override
    public void initModel() {
        mOrderShipmentItemsListController = new OrderShipmentItemsListController();
        mOrderShipmentItemsListController.setView(mOrderShipmentItemsListPanel);
    }

    @Override
    public void bindItem(Order item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrder(item);
        mOrderShipmentItemsListController.doSelectOrder(item);
    }
}
