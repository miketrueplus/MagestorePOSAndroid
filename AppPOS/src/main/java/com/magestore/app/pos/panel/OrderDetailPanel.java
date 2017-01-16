package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderCommentListController;
import com.magestore.app.pos.controller.OrderHistoryItemsListController;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderPaymentListController;
import com.magestore.app.pos.databinding.PanelOrderDetailBinding;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class OrderDetailPanel extends AbstractDetailPanel<Order> {

    PanelOrderDetailBinding mBinding;
    OrderPaymentListPanel mOrderPaymentListPanel;
    OrderPaymentListController mOrderPaymentListController;
    OrderCommentHistoryListPanel mOrderCommentHistoryListPanel;
    OrderCommentListController mOrderCommentHistoryController;
    OrderHistoryItemsListPanel mOrderHistoryItemsListPanel;
    OrderHistoryItemsListController mOrderHistoryItemsListController;

    public OrderDetailPanel(Context context) {
        super(context);
    }

    public OrderDetailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderDetailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        // Load layout view danh sách khách hàng
        View v = inflate(getContext(), R.layout.panel_order_detail, null);
        addView(v);
        mBinding = DataBindingUtil.bind(v);

        // chuẩn bị panel view danh sách payment
        mOrderPaymentListPanel = (OrderPaymentListPanel) findViewById(R.id.order_payment);

        // chuẩn bị panel view danh sách comment
        mOrderCommentHistoryListPanel = (OrderCommentHistoryListPanel) findViewById(R.id.order_comment);

        // chuẩn bị panel view danh sách items
        mOrderHistoryItemsListPanel = (OrderHistoryItemsListPanel) findViewById(R.id.order_items);
    }

    /**
     * Chuẩn bị các model, controller
     */
    @Override
    public void initModel() {
        // Lấy lại customer service từ controller của panel
        Controller controller = getController();

        // Controller Payment
        mOrderPaymentListController = new OrderPaymentListController();
        mOrderPaymentListController.setView(mOrderPaymentListPanel);
        mOrderPaymentListController.setMagestoreContext(controller.getMagestoreContext());

        // Controller Comment
        mOrderCommentHistoryController = new OrderCommentListController();
        mOrderCommentHistoryController.setView(mOrderCommentHistoryListPanel);
        mOrderCommentHistoryController.setMagestoreContext(controller.getMagestoreContext());

        // Controller Items
        mOrderHistoryItemsListController = new OrderHistoryItemsListController();
        mOrderHistoryItemsListController.setView(mOrderHistoryItemsListPanel);
        mOrderHistoryItemsListController.setMagestoreContext(controller.getMagestoreContext());

        if (controller instanceof OrderHistoryListController) {
            mOrderPaymentListController.setOrderService(((OrderHistoryListController) controller).getOrderService());
            mOrderCommentHistoryController.setOrderService(((OrderHistoryListController) controller).getOrderService());
            mOrderHistoryItemsListController.setOrderService(((OrderHistoryListController) controller).getOrderService());
        }
    }

    private void initControlValue() {

    }

    private void initTask() {

    }

    @Override
    public void bindItem(Order item) {
        super.bindItem(item);
        mBinding.setOrderDetail(item);
        mOrderPaymentListController.doSelectOrder(item);
        mOrderCommentHistoryController.doSelectOrder(item);
        mOrderHistoryItemsListController.doSelectOrder(item);
    }
}
