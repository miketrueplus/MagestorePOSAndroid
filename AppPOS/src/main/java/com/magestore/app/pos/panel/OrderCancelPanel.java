package com.magestore.app.pos.panel;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.util.DialogUtil;

/**
 * Created by Johan on 2/10/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderCancelPanel extends AbstractDetailPanel<Order> {
    Order mOrder;
    EditText cancel_comment;

    public OrderCancelPanel(Context context) {
        super(context);
    }

    public OrderCancelPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderCancelPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_cancel, null);
        addView(view);

        cancel_comment = (EditText) view.findViewById(R.id.cancel_comment);
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        mOrder = item;
    }

    @Override
    public Order bind2Item() {
        Controller controller = getController();
        OrderCommentParams cancelParam = ((OrderHistoryListController) controller).createCommentParams();
        String comment = cancel_comment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            cancelParam.setComment(comment);
            cancelParam.setIsVisibleOnFront("1");
            cancelParam.setIsCustomerNotified("1");
            cancelParam.setEntityName("string");
            cancelParam.setParentId(mOrder.getID());
            cancelParam.setStatus("canceled");
        }
        mOrder.setParamCancel(cancelParam);
        return mOrder;
    }

    public void showAlertRespone() {
        String message = getContext().getString(R.string.order_cancel_success);

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
