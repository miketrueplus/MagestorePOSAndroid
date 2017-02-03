package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderInvoiceItemsListController;
import com.magestore.app.pos.databinding.PanelOrderInvoiceBinding;

/**
 * Created by Johan on 2/2/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderInvoicePanel extends AbstractDetailPanel<Order> {
    PanelOrderInvoiceBinding mBinding;
    Order mOrder;
    OrderInvoiceItemsListPanel mOrderInvoiceItemsListPanel;
    OrderInvoiceItemsListController mOrderInvoiceItemsListController;

    public OrderInvoicePanel(Context context) {
        super(context);
    }

    public OrderInvoicePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderInvoicePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_invoice, null);
        addView(view);

        mBinding = DataBindingUtil.bind(view);

        mOrderInvoiceItemsListPanel = (OrderInvoiceItemsListPanel) findViewById(R.id.order_invoice_items);

        initModel();
    }

    @Override
    public void initModel() {
        Controller controller = getController();

        mOrderInvoiceItemsListController = new OrderInvoiceItemsListController();
        mOrderInvoiceItemsListController.setView(mOrderInvoiceItemsListPanel);

        if (controller instanceof OrderHistoryListController)
            mOrderInvoiceItemsListController.setOrderService(((OrderHistoryListController) controller).getOrderService());
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrderDetail(item);
        mOrder = item;
        mOrderInvoiceItemsListController.doSelectOrder(item);
    }

    @Override
    public Order bind2Item() {

        return mOrder;
    }
}
