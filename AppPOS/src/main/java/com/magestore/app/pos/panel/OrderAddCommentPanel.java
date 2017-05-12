package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.databinding.PanelOrderAddCommentBinding;
import com.magestore.app.util.DialogUtil;

/**
 * Created by Johan on 1/21/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderAddCommentPanel extends AbstractDetailPanel<Order> {
    PanelOrderAddCommentBinding mBinding;
    EditText edt_comment;
    Order mOrder;
    private static String ENTITY_NAME = "order";
    private static String IS_CUSTOMER_NOTIFIED = "1";
    private static String IS_VISIBLE_ON_FRONT = "0";

    public OrderAddCommentPanel(Context context) {
        super(context);
    }

    public OrderAddCommentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderAddCommentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_add_comment, null);
        addView(view);

        edt_comment = (EditText) view.findViewById(R.id.comment);

        mBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrder(item);
        mOrder = item;
    }

    @Override
    public Order bind2Item() {

        // TODO: thiếu setCreateAt() lấy theo h máy hiệnt tại
        String comment = edt_comment.getText().toString().trim();
        OrderStatus orderStatus = ((OrderHistoryListController) mController).createOrderStatus();
        orderStatus.setComment(comment);
        orderStatus.setId(null);
        orderStatus.setEntityName(ENTITY_NAME);
        orderStatus.setIsCustomerNotified(IS_CUSTOMER_NOTIFIED);
        orderStatus.setIsVisibleOnFront(IS_VISIBLE_ON_FRONT);
        orderStatus.setParentId(mOrder.getID());
        orderStatus.setStatus(mOrder.getStatus());

        mOrder.setParamStatus(orderStatus);
        return mOrder;
    }

    public void showAlertComment(){
        edt_comment.setError(getContext().getString(R.string.err_field_required));
    }

    public void showAlertRespone(boolean success) {
        String message;
        if(success){
            message = getContext().getString(R.string.order_add_comment_success);
        }else{
            message = getContext().getString(R.string.err_comment_order);
        }

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
