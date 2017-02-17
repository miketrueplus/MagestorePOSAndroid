package com.magestore.app.pos.panel;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.magestore.app.lib.model.checkout.Checkout;
import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.view.AbstractSimpleListView;
import com.magestore.app.lib.view.AbstractSimpleRecycleView;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.pos.databinding.CardCheckoutPaymentContentBinding;
import com.magestore.app.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2/7/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CheckoutPaymentListPanel extends AbstractSimpleRecycleView<CheckoutPayment> {
    CheckoutListController mCheckoutListController;
    Checkout mCheckout;
    float mTotalValue;
    List<EditText> listTextChangeValue;

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
    }

    @Override
    protected void bindItem(View view, CheckoutPayment item, final int position) {
        CardCheckoutPaymentContentBinding mBinding = DataBindingUtil.bind(view);
        mBinding.setCheckoutPayment(item);

        CheckoutPayment checkoutPayment = mList.get(position);

        EditText reference_number = (EditText) view.findViewById(R.id.reference_number);
        actionAddReferenceNumber(reference_number, checkoutPayment);

        EditText checkout_value = (EditText) view.findViewById(R.id.checkout_value);
        checkout_value.setText(String.valueOf(mTotalValue));
        actionChangeValueTotal(listTextChangeValue.get(position), mCheckout, checkoutPayment);
//        listTextChangeValue.add(checkout_value);


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

    private void actionRemovePayment(int position) {
        mList.remove(position);
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

    private void actionChangeValueTotal(final EditText checkout_value, final Checkout mCheckout, final CheckoutPayment checkoutPayment) {
        checkout_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = checkout_value.getText().toString();
                float grand_total = mCheckout.getGrandTotal();
                float totalValue = 0;
                try {
                    totalValue = Float.parseFloat(value);
                } catch (Exception e) {
                    totalValue = 0;
                }

                if (totalValue >= grand_total) {
                    float money = totalValue - grand_total;
                    mCheckout.setExchangeMoney(money);
                    mCheckoutListController.updateMoneyTotal(true, money);
                } else {
                    float money = grand_total - totalValue;
                    mCheckout.setRemainMoney(money);
                    mCheckoutListController.updateMoneyTotal(false, money);
                }
                Log.e("List", listTextChangeValue.size() + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setCheckout(Checkout mCheckout) {
        this.mCheckout = mCheckout;
        mTotalValue = mCheckout.getGrandTotal();
    }
}
