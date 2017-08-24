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
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextFloat;

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
    EditTextFloat store_credit, adjust_fee, refund_shipping, adjust_refund, gift_card;
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

        adjust_refund = (EditTextFloat) view.findViewById(R.id.adjust_refund);
        adjust_refund.setPriceFormat(true);
        adjust_refund.setOrder(true);

        adjust_fee = (EditTextFloat) view.findViewById(R.id.adjust_fee);
        adjust_fee.setPriceFormat(true);
        adjust_fee.setOrder(true);

        ll_gift_card = (LinearLayout) view.findViewById(R.id.ll_gift_card);
        gift_card = (EditTextFloat) view.findViewById(R.id.gift_card);
        gift_card.setPriceFormat(true);
        gift_card.setOrder(true);

        ll_store_credit = (LinearLayout) view.findViewById(R.id.ll_store_credit);
        store_credit = (EditTextFloat) view.findViewById(R.id.store_credit);
        store_credit.setPriceFormat(true);
        store_credit.setOrder(true);

        ll_refund_shipping = (LinearLayout) view.findViewById(R.id.ll_refund_shipping);
        refund_shipping = (EditTextFloat) view.findViewById(R.id.refund_shipping);
        refund_shipping.setPriceFormat(true);
        refund_shipping.setOrder(true);

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
        actionChangeGiftCard(item);
    }

    private void actionChangeAdjustRefund(final Order order) {
        adjust_refund.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float adjustRefund = adjust_refund.getValueFloat();
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
                float refundShipping = refund_shipping.getValueFloat();
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
                float adjustFee = adjust_fee.getValueFloat();
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
                float storeCredit = store_credit.getValueFloat();
                if (storeCredit > ConfigUtil.convertToPrice(mOrderHistoryListController.getOrder().getMaxStoreCreditRefund())) {
                    store_credit.setText(ConfigUtil.formatNumber(ConfigUtil.convertToPrice(order.getMaxStoreCreditRefund())));
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

    private void actionChangeGiftCard(final Order order) {
        gift_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float giftCard = gift_card.getValueFloat();
                if (giftCard > ConfigUtil.convertToPrice(mOrderHistoryListController.getOrder().getMaxGiftCardRefund())) {
                    gift_card.setText(ConfigUtil.formatNumber(ConfigUtil.convertToPrice(order.getMaxGiftCardRefund())));
                    order.setGiftCardRefund(giftCard);
                } else {
                    order.setGiftCardRefund(giftCard);
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

        refundParams.setIncrementId(mOrder.getIncrementId());
        refundParams.setInvoiceId(mOrder.getInvoiceId());

        float adjustRefund = adjust_refund.getValueFloat();
        float adjustFee = adjust_fee.getValueFloat();
        refundParams.setAdjustmentPositive(ConfigUtil.convertToBasePrice(adjustRefund));
        refundParams.setAdjustmentNegative(ConfigUtil.convertToBasePrice(adjustFee));
        refundParams.setBaseCurrencyCode(mOrder.getBaseCurrencyCode());
        refundParams.setShippingAmount(mOrder.getRefundShipping());
        refundParams.setStoreCurrencyCode(ConfigUtil.getCurrentCurrency().getCode());

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
        if (order.getBaseShippingInclTax() > 0) {
            refund_shipping.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(mOrderHistoryListController.checkShippingRefund(order))));
        }
    }

    private void enableGiftCard(Order order) {
        ll_gift_card.setVisibility(mOrderHistoryListController.checkCanRefundGiftcard(order) ? VISIBLE : GONE);
        if (order.getBaseGiftVoucherDiscount() > 0) {
            gift_card.setText(ConfigUtil.formatPrice((float) Math.sqrt(ConfigUtil.convertToPrice(order.getBaseGiftVoucherDiscount()))));
        }
    }

    private void enableStoreCredit(Order order) {
        String customer_id = mOrder.getCustomerId();
        boolean checkCustomerGuest = false;
        if (!StringUtil.isNullOrEmpty(customer_id) && !customer_id.equals(ConfigUtil.getCustomerGuest().getID())) {
            checkCustomerGuest = true;
        }
        ll_store_credit.setVisibility((mOrderHistoryListController.checkCanStoreCredit(order) && checkCustomerGuest) ? VISIBLE : GONE);
    }

    public void updateTotalStoreCredit(float total) {
        store_credit.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(total)));
    }

    public void updateTotalGiftCard(float total) {
        gift_card.setText(ConfigUtil.formatPrice(ConfigUtil.convertToPrice(total)));
    }

    public void showAlertRespone(boolean success) {
        String message;
        if (success) {
            message = getContext().getString(R.string.order_refund_success);
        } else {
            message = getContext().getString(R.string.err_refund_order);
        }

        // Tạo dialog và hiển thị
        DialogUtil.confirm(getContext(), message, R.string.done);
    }
}
