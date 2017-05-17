package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardCheckoutPaymentContentBinding;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.view.EditTextFloat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutPaymentListPanel extends AbstractSimpleRecycleView<CheckoutPayment> {
    CheckoutListController mCheckoutListController;
    Checkout mCheckout;
    List<EditTextFloat> listTextChangeValue;
    HashMap<CheckoutPayment, EditTextFloat> mapTextId;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public CheckoutPaymentListPanel(Context context) {
        super(context);
    }

    public CheckoutPaymentListPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckoutPaymentListPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        EditTextFloat checkout_value = (EditTextFloat) view.findViewById(R.id.checkout_value);
        mapTextId.put(item, checkout_value);
        checkout_value.setText(ConfigUtil.formatNumber(checkoutPayment.getAmount()));
        actionChangeValueTotal(checkout_value, mCheckout, checkoutPayment);

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
//        CheckoutPayment checkoutPayment = mList.get(position);
    }

    public void updateTotal(List<CheckoutPayment> listPayment) {
        float totalValue = 0;

        if (mCheckout != null) {
            float grand_total = mCheckout.getGrandTotal();

            for (CheckoutPayment payment : listPayment) {
                totalValue += payment.getAmount();
            }

            if (totalValue >= grand_total) {
                float money = totalValue - grand_total;
                mCheckout.setExchangeMoney(money);
                mCheckoutListController.updateMoneyTotal(true, money);
                mCheckoutListController.updateMaxAmountStoreCredit(grand_total);
                // disable add payment
                mCheckoutListController.isEnableButtonAddPayment(false);
                mCheckoutListController.isEnableCreateInvoice(true);
                mCheckoutListController.changeTitlePlaceOrder(false);
            } else {
                float money = grand_total - totalValue;
                mCheckout.setRemainMoney(money);
                mCheckoutListController.updateMoneyTotal(false, money);
                mCheckoutListController.updateMaxAmountStoreCredit(money);
                mCheckoutListController.isEnableButtonAddPayment(totalValue > 0 ? true : false);
                mCheckoutListController.isEnableCreateInvoice(false);
                if(mCheckout.getStatus() == CheckoutListController.STATUS_CHECKOUT_PROCESSING){
                    mCheckoutListController.changeTitlePlaceOrder(money == grand_total ? false : true);
                }
            }
        }
    }

    private void actionRemovePayment(int position) {
        mapTextId.remove(mList.get(position));
        mList.remove(position);
        float grand_total = mCheckout.getGrandTotal();
        float totalValue = 0;
        float allRowTotal;

        for (EditTextFloat edt_value : mapTextId.values()) {
            allRowTotal = edt_value.getValueFloat();
            totalValue += allRowTotal;
        }

        if (totalValue >= grand_total) {
            float money = totalValue - grand_total;
            mCheckout.setExchangeMoney(money);
            mCheckoutListController.updateMoneyTotal(true, money);
            mCheckoutListController.updateMaxAmountStoreCredit(grand_total);
            // disable add payment
            mCheckoutListController.isEnableButtonAddPayment(false);
            mCheckoutListController.isEnableCreateInvoice(true);
            mCheckoutListController.changeTitlePlaceOrder(false);
        } else {
            float money = grand_total - totalValue;
            mCheckout.setRemainMoney(money);
            mCheckoutListController.updateMoneyTotal(false, money);
            mCheckoutListController.updateMaxAmountStoreCredit(money);
            mCheckoutListController.isEnableButtonAddPayment(totalValue > 0 ? true : false);
            mCheckoutListController.isEnableCreateInvoice(false);
            mCheckoutListController.changeTitlePlaceOrder(money == grand_total ? false : true);
        }
        if (checkPaymentStoreCredit()) {
            mCheckoutListController.isShowPluginStoreCredit(false);
        }
        notifyDataSetChanged();
        mCheckoutListController.onRemovePaymentMethod();
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

    private void actionChangeValueTotal(final EditTextFloat checkout_value, final Checkout mCheckout, final CheckoutPayment checkoutPayment) {
        checkout_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float grand_total = mCheckout.getGrandTotal();
                float totalValue = 0;
                float currentValue;
                float allRowTotal;
                float totalNotExchange = 0;

                currentValue = checkout_value.getValueFloat();

                for (EditTextFloat edt_value : mapTextId.values()) {
                    edt_value.setEnabled(true);
                    allRowTotal = edt_value.getValueFloat();
                    totalValue += allRowTotal;
                }

                if (totalValue > grand_total) {
                    for (EditTextFloat edt_value : mapTextId.values()) {
                        if (edt_value == checkout_value) {
                            checkout_value.setEnabled(true);
                        } else {
                            edt_value.setEnabled(false);
                            allRowTotal = edt_value.getValueFloat();
                            totalNotExchange += allRowTotal;
                        }
                    }

                    float money = totalValue - grand_total;
                    mCheckout.setExchangeMoney(money);
                    mCheckoutListController.updateMoneyTotal(true, money);

                    float remain_money = grand_total - totalNotExchange;
                    checkoutPayment.setAmount(currentValue);
                    checkoutPayment.setBaseAmount(currentValue);
                    checkoutPayment.setRealAmount(remain_money);
                    checkoutPayment.setBaseRealAmount(remain_money);
                    // disable add payment
                    mCheckoutListController.isEnableButtonAddPayment(false);
                    mCheckoutListController.isEnableCreateInvoice(true);
                    mCheckoutListController.changeTitlePlaceOrder(false);
                } else {
                    float money = grand_total - totalValue;
                    mCheckout.setRemainMoney(money);
                    mCheckoutListController.updateMoneyTotal(false, money);
                    checkoutPayment.setAmount(currentValue);
                    checkoutPayment.setBaseAmount(currentValue);
                    checkoutPayment.setRealAmount(currentValue);
                    checkoutPayment.setBaseRealAmount(currentValue);
                    if (totalValue == grand_total) {
                        mCheckoutListController.updateMaxAmountStoreCredit(grand_total);
                        mCheckoutListController.isEnableButtonAddPayment(false);
                        mCheckoutListController.isEnableCreateInvoice(true);
                        mCheckoutListController.changeTitlePlaceOrder(false);
                    } else {
                        mCheckoutListController.updateMaxAmountStoreCredit(money);
                        mCheckoutListController.isEnableButtonAddPayment(true);
                        mCheckoutListController.isEnableCreateInvoice(false);
                        mCheckoutListController.changeTitlePlaceOrder(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // plugin store credit
    private boolean checkPaymentStoreCredit() {
        for (CheckoutPayment payment : mList) {
            if (payment.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                return true;
            }
        }
        return false;
    }

    public void removePaymentStoreCredit() {
        int count = 0;
        boolean checkPayment = false;
        if (mList != null && mList.size() > 0) {
            for (CheckoutPayment payment : mList) {
                if (payment.getCode().equals(PluginStoreCreditPanel.STORE_CREDIT_PAYMENT_CODE)) {
                    checkPayment = true;
                    break;
                }
                count++;
            }
            if (checkPayment) {
                actionRemovePayment(count);
            }
        }
    }

    public void resetListPayment() {
        mapTextId = new HashMap<>();
    }

    public void setCheckout(Checkout mCheckout) {
        this.mCheckout = mCheckout;
    }
}
