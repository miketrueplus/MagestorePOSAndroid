package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderShipmentItemsListController;
import com.magestore.app.pos.databinding.PanelOrderShipmentBinding;
import com.magestore.app.util.DialogUtil;

import java.util.List;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderShipmentPanel extends AbstractDetailPanel<Order> {
    PanelOrderShipmentBinding mBinding;
    OrderShipmentItemsListPanel mOrderShipmentItemsListPanel;
    OrderShipmentItemsListController mOrderShipmentItemsListController;
    Order mOrder;
    CheckBox cb_send_email;
    EditText track_number;
    EditText shipment_comment;

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

        cb_send_email = (CheckBox) view.findViewById(R.id.cb_send_email);

        track_number = (EditText) view.findViewById(R.id.track_number);

        shipment_comment = (EditText) view.findViewById(R.id.shipment_comment);

        mOrderShipmentItemsListPanel = (OrderShipmentItemsListPanel) findViewById(R.id.order_shipment_items);

        mBinding = DataBindingUtil.bind(view);

        initModel();
    }

    @Override
    public void initModel() {
        mOrderShipmentItemsListController = new OrderShipmentItemsListController();
        mOrderShipmentItemsListController.setView(mOrderShipmentItemsListPanel);

        if (mController instanceof OrderHistoryListController)
            mOrderShipmentItemsListController.setOrderService(((OrderHistoryListController) mController).getOrderService());
    }

    @Override
    public void bindItem(Order item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrder(item);
        mOrderShipmentItemsListController.doSelectOrder(item);
        mOrder = item;
    }

    @Override
    public Order bind2Item() {
        boolean isSendEmail = cb_send_email.isChecked();
        OrderHistoryListController orderHistoryListController = ((OrderHistoryListController) mController);
        OrderShipmentParams shipmentParams = orderHistoryListController.createOrderShipmentParams();

        shipmentParams.setOrderId(mOrder.getID());
        if (isSendEmail) {
            shipmentParams.setEmailSent("1");
        } else {
            shipmentParams.setEmailSent("0");
        }

        String comment = shipment_comment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            OrderCommentParams commentParams = orderHistoryListController.createCommentParams();
            commentParams.setComment(comment);
            List<OrderCommentParams> listComment = orderHistoryListController.createListComment();
            listComment.add(commentParams);
            shipmentParams.setComments(listComment);
        }

        String track = track_number.getText().toString();
        if (!TextUtils.isEmpty(track)) {
            OrderShipmentTrackParams trackParams = orderHistoryListController.createOrderShipmentTrackParams();
            trackParams.setTrackNumber(track);
            List<OrderShipmentTrackParams> listTrack = orderHistoryListController.createListTrack();
            listTrack.add(trackParams);
            shipmentParams.setTracks(listTrack);
        }

        shipmentParams.setItems(mOrderShipmentItemsListPanel.bind2List());
        mOrder.setParamShipment(shipmentParams);

        return mOrder;
    }

    public void showAlertRespone() {
        String message = getContext().getString(R.string.order_shipment_success);

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
