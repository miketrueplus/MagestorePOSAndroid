package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderCommentListController;
import com.magestore.app.pos.controller.OrderHistoryItemsListController;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderPaymentListController;
import com.magestore.app.pos.databinding.PanelOrderDetailBinding;
import com.magestore.app.pos.util.DialogUtil;
import com.magestore.app.pos.view.MagestoreDialog;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class OrderDetailPanel extends AbstractDetailPanel<Order> {
    View v;
    Order mOrder;
    MagestoreDialog dialog;
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
        v = inflate(getContext(), R.layout.panel_order_detail, null);
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
        mOrder = item;
        mBinding.setOrderDetail(item);
        mOrderPaymentListController.doSelectOrder(item);
        mOrderCommentHistoryController.doSelectOrder(item);
        mOrderHistoryItemsListController.doSelectOrder(item);

        ImageView im_status = (ImageView) v.findViewById(R.id.im_status);
        im_status.setImageResource(R.drawable.ic_order_status);

        String status = item.getStatus().toLowerCase();
        changeColorStatusOrder(status, im_status);
    }

    private void changeColorStatusOrder(String status, ImageView im_status) {
        switch (status) {
            case "pending":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_pending));
                break;
            case "processing":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_processing));
                break;
            case "complete":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_complete));
                break;
            case "cancelled":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_cancelled));
                break;
            case "closed":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_closed));
                break;
            case "not_sync":
                im_status.setColorFilter(ContextCompat.getColor(getContext(), R.color.order_status_notsync));
                break;
        }
    }

    public void showPopupMenu(View view) {
        if (mOrder == null) {
            return;
        }
        View menuItemView = view.findViewById(R.id.order_action);
        PopupMenu popupMenu = new PopupMenu(getContext(), menuItemView);
        popupMenu.inflate(R.menu.order_action);
        String status = mOrder.getStatus().toLowerCase();
        checkShowItemMenu(status, popupMenu);
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_send_email:
                        onClickSendEmail();
                        return true;
                    case R.id.action_ship:
                        onClickShipment();
                        return true;
                    case R.id.action_cancel:
                        return true;
                    case R.id.action_add_comment:
                        onClickAddComment();
                        return true;
                    case R.id.action_re_order:
                        return true;
                    case R.id.action_refund:
                        onClickRefund();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void checkShowItemMenu(String status, PopupMenu popupMenu) {
        checkVisibleItemMenu(popupMenu, R.id.action_send_email, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "not_sync".equals(status));
        checkVisibleItemMenu(popupMenu, R.id.action_ship, "pending".equals(status) | "processing".equals(status) | "not_sync".equals(status));
        checkVisibleItemMenu(popupMenu, R.id.action_cancel, "pending".equals(status) | "processing".equals(status) | "not_sync".equals(status));
        checkVisibleItemMenu(popupMenu, R.id.action_add_comment, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "cancelled".equals(status) | "not_sync".equals(status));
        checkVisibleItemMenu(popupMenu, R.id.action_re_order, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "cancelled".equals(status) | "closed".equals(status) | "not_sync".equals(status));
        checkVisibleItemMenu(popupMenu, R.id.action_refund, "pending".equals(status) | "processing".equals(status) | "complete".equals(status) | "not_sync".equals(status));
    }

    private void checkVisibleItemMenu(PopupMenu popupMenu, int id, boolean check) {
        popupMenu.getMenu().findItem(id).setVisible(check);
    }

    private void onClickSendEmail() {
        final OrderSendEmailPanel mOrderSendEmailPanel = new OrderSendEmailPanel(getContext());
        mOrderSendEmailPanel.bindItem(mOrder);
        mOrderSendEmailPanel.setController(mController);
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_send_email_title), mOrderSendEmailPanel);
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSendEmail(mOrderSendEmailPanel);
                dialog.dismiss();
            }
        });
    }

    private void actionSendEmail(OrderSendEmailPanel mOrderSendEmailPanel) {
        Order order = mOrderSendEmailPanel.bind2Item();
        String email = order.getCustomerEmail();
        String orderId = order.getID();

        // TODO: thiếu check có phải là kiểu email hay không
        if (TextUtils.isEmpty(email)) {
            mOrderSendEmailPanel.showAlertEmail();
            return;
        }

        ((OrderHistoryListController) mController).setOrderSendEmailPanel(mOrderSendEmailPanel);
        ((OrderHistoryListController) mController).sendEmail(email, orderId);
    }

    private void onClickAddComment() {
        final OrderAddCommentPanel mOrderAddCommentPanel = new OrderAddCommentPanel(getContext());
        mOrderAddCommentPanel.bindItem(mOrder);
        mOrderAddCommentPanel.setController(mController);
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_add_comment_title), mOrderAddCommentPanel);
        dialog.show();

        dialog.getButtonSave().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionAddComment(mOrderAddCommentPanel);
                dialog.dismiss();
            }
        });
    }

    private void actionAddComment(OrderAddCommentPanel mOrderAddCommentPanel) {
        Order order = mOrderAddCommentPanel.bind2Item();
        String comment = order.getParamStatus().getComment();

        if (TextUtils.isEmpty(comment)) {
            mOrderAddCommentPanel.showAlertComment();
            return;
        }

        ((OrderHistoryListController) mController).setOrderAddCommentPanel(mOrderAddCommentPanel);
        ((OrderHistoryListController) mController).setOrderCommentListController(mOrderCommentHistoryController);
        ((OrderHistoryListController) mController).insertOrderStatus(order);
    }

    private void onClickShipment() {
        final OrderShipmentPanel mOrderShipmentPanel = new OrderShipmentPanel(getContext());
        mOrderShipmentPanel.bindItem(mOrder);
        mOrderShipmentPanel.setController(mController);
        mOrderShipmentPanel.initModel();
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_shipment_title), mOrderShipmentPanel);
        dialog.setGoneButtonSave(true);
        dialog.show();

        Button btn_submit_shipment = (Button) dialog.findViewById(R.id.btn_submit_shipment);
        btn_submit_shipment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = mOrderShipmentPanel.bind2Item();
                ((OrderHistoryListController) mController).setOrderHistoryItemsListController(mOrderHistoryItemsListController);
                ((OrderHistoryListController) mController).doAction(OrderHistoryListController.CREATE_SHIPMENT_TYPE, OrderHistoryListController.CREATE_SHIPMENT_CODE, null, order);
                dialog.dismiss();
            }
        });
    }

    private void onClickRefund() {
        final OrderRefundPanel mOrderRefundPanel = new OrderRefundPanel(getContext());
        mOrderRefundPanel.bindItem(mOrder);
        mOrderRefundPanel.setController(mController);
        mOrderRefundPanel.initModel();
        dialog = DialogUtil.dialog(getContext(), getContext().getString(R.string.order_refund_title), mOrderRefundPanel);
        dialog.setDialogSave(getContext().getString(R.string.order_refund_btn_save));
        dialog.show();
    }
}
