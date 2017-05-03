package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.magestore.app.lib.controller.Controller;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.controller.OrderRefundItemsListController;
import com.magestore.app.pos.databinding.PanelOrderRefundBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.DialogUtil;

import java.util.List;

/**
 * Created by Johan on 1/23/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderRefundPanel extends AbstractDetailPanel<Order> {
    PanelOrderRefundBinding mBinding;
    Order mOrder;
    OrderRefundItemsListPanel mOrderRefundItemsListPanel;
    OrderRefundItemsListController mOrderRefundItemsListController;
    OrderHistoryListController mOrderHistoryListController;
    CheckBox cb_send_email;
    EditText refund_comment;
    EditText adjust_refund;
    EditText adjust_fee, refund_shipping, gift_card, store_credit;
    LinearLayout ll_gift_card, ll_store_credit, ll_refund_shipping;

    public OrderRefundPanel(Context context) {
        super(context);
    }

    public OrderRefundPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderRefundPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        View view = inflate(getContext(), R.layout.panel_order_refund, null);
        addView(view);

        cb_send_email = (CheckBox) view.findViewById(R.id.cb_send_email);

        refund_comment = (EditText) view.findViewById(R.id.refund_comment);

        adjust_refund = (EditText) view.findViewById(R.id.adjust_refund);

        adjust_fee = (EditText) view.findViewById(R.id.adjust_fee);

        ll_gift_card = (LinearLayout) view.findViewById(R.id.ll_gift_card);
        gift_card = (EditText) view.findViewById(R.id.gift_card);

        ll_store_credit = (LinearLayout) view.findViewById(R.id.ll_store_credit);
        store_credit = (EditText) view.findViewById(R.id.store_credit);

        ll_refund_shipping = (LinearLayout) view.findViewById(R.id.ll_refund_shipping);
        refund_shipping = (EditText) view.findViewById(R.id.refund_shipping);

        mBinding = DataBindingUtil.bind(view);

        mOrderRefundItemsListPanel = (OrderRefundItemsListPanel) findViewById(R.id.order_refund_items);

        initModel();
    }

    @Override
    public void initModel() {
        Controller controller = getController();
        mOrderHistoryListController = ((OrderHistoryListController) controller);
        mOrderRefundItemsListController = new OrderRefundItemsListController();
        mOrderRefundItemsListController.setView(mOrderRefundItemsListPanel);
        mOrderRefundItemsListController.setOrderHistoryListController(mOrderHistoryListController);

        if (controller instanceof OrderHistoryListController)
            mOrderRefundItemsListController.setOrderService(((OrderHistoryListController) controller).getOrderService());
    }

    @Override
    public void bindItem(Order item) {
        if (item == null) return;
        super.bindItem(item);
        item.setRefundShipping(ConfigUtil.convertToPrice(item.getBaseShippingInclTax()));
        mOrderHistoryListController.getTotalItem();
        mBinding.setOrder(item);
        mOrder = item;
        mOrderRefundItemsListController.doSelectOrder(item);
        enableRefundShipping(item);
        enableGiftCard(item);
        enableStoreCredit(item);
        actionChangeAdjustRefund(item);
        actionChangeAdjustFee(item);
        actionChangeRefundShipping(item);
        actionChangeStoreCredit(item);
    }

    private void actionChangeAdjustRefund(final Order order) {
        adjust_refund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float adjustRefund = 0;
                String adjustValue = adjust_refund.getText().toString();
                try {
                    adjustRefund = Float.parseFloat(adjustValue);
                } catch (Exception e) {
                }

                order.setAdjustRefund(adjustRefund);
                ((OrderHistoryListController) getController()).chaneMaxStoreCreditRefund();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionChangeRefundShipping(final Order order) {
        refund_shipping.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float refundShipping = 0;
                String shippingValue = refund_shipping.getText().toString();
                try {
                    refundShipping = Float.parseFloat(shippingValue);
                } catch (Exception e) {
                }

                order.setRefundShipping(refundShipping);
                ((OrderHistoryListController) getController()).chaneMaxStoreCreditRefund();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionChangeAdjustFee(final Order order) {
        adjust_fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float adjustFee = 0;
                String adjustValue = adjust_fee.getText().toString();
                try {
                    adjustFee = Float.parseFloat(adjustValue);
                } catch (Exception e) {
                }

                order.setAdjustFree(adjustFee);
                ((OrderHistoryListController) getController()).chaneMaxStoreCreditRefund();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionChangeStoreCredit(final Order order) {
        store_credit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float storeCredit = 0;
                String storeCreditValue = store_credit.getText().toString();
                try {
                    storeCredit = Float.parseFloat(storeCreditValue);
                } catch (Exception e) {
                }
                if (storeCredit > mOrderHistoryListController.getOrder().getMaxStoreCreditRefund()) {
                    store_credit.setText(ConfigUtil.formatNumber(order.getMaxStoreCreditRefund()));
                    order.setStoreCreditRefund(order.getMaxStoreCreditRefund());
                } else {
                    order.setStoreCreditRefund(storeCredit);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public Order bind2Item() {
        boolean isSendEmail = cb_send_email.isChecked();
        OrderHistoryListController orderHistoryListController = ((OrderHistoryListController) mController);

        OrderRefundParams refundParams = orderHistoryListController.createOrderRefundParams();

        refundParams.setOrderId(mOrder.getID());
        if (isSendEmail) {
            refundParams.setEmailSent("1");
        } else {
            refundParams.setEmailSent("0");
        }

        float adjustRefund = 0;
        try {
            adjustRefund = Float.parseFloat(adjust_refund.getText().toString());
        } catch (Exception e) {

        }

        float adjustFee = 0;
        try {
            adjustFee = Float.parseFloat(adjust_fee.getText().toString());
        } catch (Exception e) {

        }
        refundParams.setAdjustmentPositive(adjustRefund);
        refundParams.setAdjustmentNegative(adjustFee);
        refundParams.setBaseCurrencyCode(mOrder.getBaseCurrencyCode());
        refundParams.setShippingAmount(mOrder.getShippingAmount());
        refundParams.setStoreCurrencyCode(mOrder.getStoreCurrencyCode());

        String comment = refund_comment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            OrderCommentParams commentParams = orderHistoryListController.createCommentParams();
            commentParams.setComment(comment);
            List<OrderCommentParams> listComment = orderHistoryListController.createListComment();
            listComment.add(commentParams);
            refundParams.setComments(listComment);
        }
        refundParams.setItems(mOrderRefundItemsListPanel.bind2List());
        mOrder.setParamRefund(refundParams);
        return mOrder;
    }

    private void enableRefundShipping(Order order) {
        ll_refund_shipping.setVisibility(mOrderHistoryListController.checkShippingRefund(order) > 0 ? VISIBLE : GONE);
        if (order.getShippingAmount() > 0) {
            refund_shipping.setText(ConfigUtil.formatNumber(mOrderHistoryListController.checkShippingRefund(order)));
        }
    }

    private void enableGiftCard(Order order) {
        ll_gift_card.setVisibility(mOrderHistoryListController.checkCanRefundGiftcard(order) ? VISIBLE : GONE);
        if (order.getGiftVoucherDiscount() > 0) {
            gift_card.setText(ConfigUtil.formatNumber((float) Math.sqrt(ConfigUtil.convertToPrice(order.getBaseGiftVoucherDiscount()))));
        }
    }

    private void enableStoreCredit(Order order) {
        ll_store_credit.setVisibility(mOrderHistoryListController.checkCanStoreCredit(order) ? VISIBLE : GONE);
    }

    public void updateTotalStoreCredit(float total) {
        store_credit.setText(ConfigUtil.formatNumber(total));
    }

    public void showAlertRespone() {
        String message = getContext().getString(R.string.order_refund_success);

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
