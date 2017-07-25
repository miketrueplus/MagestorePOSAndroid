package com.magestore.app.pos.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.util.ConfigUtil;

/**
 * Created by Johan on 3/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderTakePaymentPanel extends AbstractDetailPanel<Order> {
    OrderAddPaymentPanel mOrderAddPaymentPanel;
    OrderListChoosePaymentPanel mOrderListChoosePaymentPanel;
    TextView txt_remain_title, txt_remain_value, tv_check_take_payment_required;
    LinearLayout ll_order_add_payment;
    RelativeLayout rl_order_add_payment;

    public OrderTakePaymentPanel(Context context) {
        super(context);
    }

    public OrderTakePaymentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderTakePaymentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_take_payment, null);
        addView(view);

        mOrderAddPaymentPanel = (OrderAddPaymentPanel) view.findViewById(R.id.order_add_payment_panel);
        mOrderListChoosePaymentPanel = (OrderListChoosePaymentPanel) view.findViewById(R.id.order_payment_list_panel);
        rl_order_add_payment = (RelativeLayout) view.findViewById(R.id.rl_order_add_payment);
        ll_order_add_payment = (LinearLayout) view.findViewById(R.id.ll_order_add_payment);
        txt_remain_title = (TextView) view.findViewById(R.id.txt_remain_title);
        txt_remain_value = (TextView) view.findViewById(R.id.txt_remain_value);
        tv_check_take_payment_required = (TextView) view.findViewById(R.id.tv_check_take_payment_required);
    }

    @Override
    public void initModel() {
        OrderHistoryListController mOrderHistoryListController = ((OrderHistoryListController) mController);
        mOrderAddPaymentPanel.setOrderHistoryListController(mOrderHistoryListController);
        mOrderListChoosePaymentPanel.setOrderHistoryListController(mOrderHistoryListController);
        mOrderHistoryListController.setOrderAddPaymentPanel(mOrderAddPaymentPanel);
        mOrderHistoryListController.setOrderListChoosePaymentPanel(mOrderListChoosePaymentPanel);
    }

    @Override
    public void bindItem(Order item) {
        super.bindItem(item);
        mOrderListChoosePaymentPanel.setOrder(item);
        ((OrderHistoryListController) mController).bindDataListChoosePayment();
        bindTotalPrice(item.getTotalDue());
        rl_order_add_payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OrderHistoryListController) mController).bindDataListChoosePayment();
                showPanelAddPaymentMethod();
                isEnableButtonAddPayment(false);
            }
        });
    }

    public void updateMoneyTotal(boolean type, float totalPrice) {
        String total = ConfigUtil.formatPrice(totalPrice);
        if (type) {
            txt_remain_title.setText(getContext().getString(R.string.sales_expected_change));
        } else {
            txt_remain_title.setText(getContext().getString(R.string.sales_remain_money));
        }
        txt_remain_value.setText(total);
    }

    public void isEnableButtonAddPayment(boolean enable) {
        ll_order_add_payment.setVisibility(enable ? VISIBLE : GONE);
    }

    public void showPanelListChoosePayment() {
        mOrderAddPaymentPanel.setVisibility(GONE);
        mOrderListChoosePaymentPanel.setVisibility(VISIBLE);
    }

    public void showPanelAddPaymentMethod() {
        mOrderAddPaymentPanel.setVisibility(VISIBLE);
        mOrderListChoosePaymentPanel.setVisibility(GONE);
    }

    public void bindTotalPrice(float totalPrice) {
        String total = ConfigUtil.formatPrice(totalPrice);
        txt_remain_value.setText(total);
    }

    // Felix 3/4/2017 : Hiển thị text khi chưa chọn Payment
    public void showNotifiSelectPayment() {

        tv_check_take_payment_required.setText(getContext().getString(R.string.sales_show_notifi_select_payment));
        tv_check_take_payment_required.setError(getContext().getString(R.string.sales_show_notifi_select_payment));
    }

    public void hideCheckPaymenrRequired(){
        if(tv_check_take_payment_required.getText().length() > 0) {
            tv_check_take_payment_required.setText("") ;
            tv_check_take_payment_required.setError(null);
        }
    }
    // Felix 3/4/2017 : End
}
