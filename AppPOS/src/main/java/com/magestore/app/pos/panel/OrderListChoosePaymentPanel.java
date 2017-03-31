package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.OrderHistoryListController;
import com.magestore.app.pos.databinding.CardCheckoutPaymentContentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Johan on 3/31/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class OrderListChoosePaymentPanel extends AbstractSimpleRecycleView<CheckoutPayment> {
    OrderHistoryListController mOrderHistoryListController;
    List<EditText> listTextChangeValue;
    HashMap<CheckoutPayment, EditText> mapTextId;
    Order mOrder;

    public void setOrderHistoryListController(OrderHistoryListController mOrderHistoryListController) {
        this.mOrderHistoryListController = mOrderHistoryListController;
    }

    public void setOrder(Order mOrder) {
        this.mOrder = mOrder;
    }

    public OrderListChoosePaymentPanel(Context context) {
        super(context);
    }

    public OrderListChoosePaymentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderListChoosePaymentPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initLayout() {
        listTextChangeValue = new ArrayList<>();
        mapTextId = new HashMap<>();
    }

    @Override
    protected void bindItem(View view, CheckoutPayment item, final int position) {
        CardCheckoutPaymentContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setCheckoutPayment(item);

        CheckoutPayment checkoutPayment = mList.get(position);

        EditText reference_number = (EditText) view.findViewById(R.id.reference_number);
        actionAddReferenceNumber(reference_number, checkoutPayment);

        EditText checkout_value = (EditText) view.findViewById(R.id.checkout_value);
        mapTextId.put(item, checkout_value);
        checkout_value.setText(String.valueOf(checkoutPayment.getAmount()));
        actionChangeValueTotal(checkout_value, mOrder, checkoutPayment);

        RelativeLayout rl_remove_payment = (RelativeLayout) view.findViewById(R.id.rl_remove_payment);
        rl_remove_payment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionRemovePayment(position);
            }
        });
    }

    @Override
    protected void onClickItem(View view, CheckoutPayment item, int position) {

    }

    public void updateTotal(List<CheckoutPayment> listPayment) {
        float totalValue = 0;

        if (mOrder != null) {
            float total_due = mOrder.getTotalDue();

            for (CheckoutPayment payment : listPayment) {
                totalValue += payment.getAmount();
            }

            if (totalValue >= total_due) {
                float money = totalValue - total_due;
                mOrder.setExchangeMoney(money);
                mOrderHistoryListController.updateMoneyTotal(true, money);
                // disable add payment
                mOrderHistoryListController.isEnableButtonAddPayment(false);
            } else {
                float money = total_due - totalValue;
                mOrder.setRemainMoney(money);
                mOrderHistoryListController.updateMoneyTotal(false, money);
                mOrderHistoryListController.isEnableButtonAddPayment(true);
            }
        }
    }

    private void actionAddReferenceNumber(final EditText reference_number, final CheckoutPayment checkoutPayment) {
        reference_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String referenceNumber = reference_number.getText().toString().trim();
                checkoutPayment.setReferenceNumber(referenceNumber);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionChangeValueTotal(final EditText checkout_value, final Order mOrder, final CheckoutPayment checkoutPayment) {
        checkout_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float grand_total = mOrder.getTotalDue();
                float totalValue = 0;
                float currentValue;
                float allRowTotal;
                float totalNotExchange = 0;

                String txtValue = checkout_value.getText().toString();
                try {
                    currentValue = Float.parseFloat(txtValue);
                } catch (Exception e) {
                    currentValue = 0;
                }

                for (EditText edt_value : mapTextId.values()) {
                    edt_value.setEnabled(true);
                    String value = edt_value.getText().toString();
                    try {
                        allRowTotal = Float.parseFloat(value);
                    } catch (Exception e) {
                        allRowTotal = 0;
                    }
                    totalValue += allRowTotal;
                }

                if (totalValue > grand_total) {
                    for (EditText edt_value : mapTextId.values()) {
                        if (edt_value == checkout_value) {
                            checkout_value.setEnabled(true);
                        } else {
                            edt_value.setEnabled(false);
                            String value = edt_value.getText().toString();
                            try {
                                allRowTotal = Float.parseFloat(value);
                            } catch (Exception e) {
                                allRowTotal = 0;
                            }
                            totalNotExchange += allRowTotal;
                        }
                    }

                    float money = totalValue - grand_total;
                    mOrder.setExchangeMoney(money);
                    mOrderHistoryListController.updateMoneyTotal(true, money);

                    float remain_money = grand_total - totalNotExchange;
                    checkoutPayment.setAmount(currentValue);
                    checkoutPayment.setBaseAmount(currentValue);
                    checkoutPayment.setRealAmount(remain_money);
                    checkoutPayment.setBaseRealAmount(remain_money);
                    // disable add payment
                    mOrderHistoryListController.isEnableButtonAddPayment(false);
                } else {
                    float money = grand_total - totalValue;
                    mOrder.setRemainMoney(money);
                    mOrderHistoryListController.updateMoneyTotal(false, money);
                    checkoutPayment.setAmount(currentValue);
                    checkoutPayment.setBaseAmount(currentValue);
                    checkoutPayment.setRealAmount(currentValue);
                    checkoutPayment.setBaseRealAmount(currentValue);
                    mOrderHistoryListController.isEnableButtonAddPayment(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void actionRemovePayment(int position) {
        mapTextId.remove(mList.get(position));
        mList.remove(position);
        float grand_total = mOrder.getTotalDue();
        float totalValue = 0;
        float allRowTotal;

        for (EditText edt_value : mapTextId.values()) {
            String value = edt_value.getText().toString();
            try {
                allRowTotal = Float.parseFloat(value);
            } catch (Exception e) {
                allRowTotal = 0;
            }
            totalValue += allRowTotal;
        }

        if (totalValue >= grand_total) {
            float money = totalValue - grand_total;
            mOrder.setExchangeMoney(money);
            mOrderHistoryListController.updateMoneyTotal(true, money);
            // disable add payment
            mOrderHistoryListController.isEnableButtonAddPayment(false);
        } else {
            float money = grand_total - totalValue;
            mOrder.setRemainMoney(money);
            mOrderHistoryListController.updateMoneyTotal(false, money);
            mOrderHistoryListController.isEnableButtonAddPayment(true);
        }
        notifyDataSetChanged();
        mOrderHistoryListController.onRemovePaymentMethod();
    }
}
