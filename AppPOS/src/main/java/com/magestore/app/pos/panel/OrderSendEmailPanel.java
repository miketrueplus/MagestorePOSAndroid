package com.magestore.app.pos.panel;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.databinding.PanelOrderSendEmailBinding;
import com.magestore.app.util.DialogUtil;
import com.magestore.app.util.StringUtil;

/**
 * Created by Johan on 1/20/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderSendEmailPanel extends AbstractDetailPanel<Order> {
    PanelOrderSendEmailBinding mBinding;
    Order mOrder;
    EditText edt_email;

    public OrderSendEmailPanel(Context context) {
        super(context);
    }

    public OrderSendEmailPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderSendEmailPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_send_email, null);
        addView(view);
        edt_email = (EditText) view.findViewById(R.id.email);
        mBinding = DataBindingUtil.bind(view);
    }

    @Override
    public void bindItem(Order item) {
        // Bind từ object sang view
        if (item == null) return;
        super.bindItem(item);
        mBinding.setOrder(item);
        mOrder = item;
    }

    @Override
    public Order bind2Item() {
        String email = edt_email.getText().toString().trim();
        mOrder.setCustomerEmail(email);
        return mOrder;
    }

    public void showAlertEmail() {
        edt_email.setError(getContext().getString(R.string.err_field_required));
    }

    public void showRequiedEmail() {
        edt_email.setError(getContext().getString(R.string.err_field_email_required));
    }

    public void showAlertRespone(boolean statusRespone, String respone) {
        String message = "";
        if (!StringUtil.isNullOrEmpty(respone)) {
            message = respone;
        } else {
            if (statusRespone) {
                message = getContext().getString(R.string.order_send_email_success);
            } else {
                message = getContext().getString(R.string.err_send_email);
            }
        }

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
