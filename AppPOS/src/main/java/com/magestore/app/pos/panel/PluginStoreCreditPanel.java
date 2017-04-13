package com.magestore.app.pos.panel;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

/**
 * Created by Johan on 4/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PluginStoreCreditPanel extends AbstractDetailPanel<StoreCredit> {
    static String STORE_CREDIT_PAYMENT_CODE = "storecredit";
    CheckoutListController mCheckoutListController;
    StoreCredit mStoreCredit;
    EditText store_credit_value;
    CheckBox cb_use_max_credit;
    Button bt_apply;
    RelativeLayout rl_remove_store_credit;
    TextView tv_store_credit;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public PluginStoreCreditPanel(Context context) {
        super(context);
    }

    public PluginStoreCreditPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PluginStoreCreditPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();

        tv_store_credit = (TextView) findViewById(R.id.tv_store_credit);
        tv_store_credit.setText(getContext().getString(R.string.plugin_store_credit_title, ConfigUtil.formatPrice(0)));
        store_credit_value = (EditText) findViewById(R.id.store_credit_value);
        cb_use_max_credit = (CheckBox) findViewById(R.id.cb_use_max_credit);
        bt_apply = (Button) findViewById(R.id.bt_apply);
        rl_remove_store_credit = (RelativeLayout) findViewById(R.id.rl_remove_store_credit);

        store_credit_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String credit = store_credit_value.getText().toString().trim();
                if (!StringUtil.isNullOrEmpty(credit)) {
                    float credit_value;
                    try {
                        credit_value = Float.parseFloat(credit);
                    } catch (Exception e) {
                        credit_value = 0;
                    }
                    mStoreCredit.setAmount(credit_value);
                } else {
                    store_credit_value.setText(ConfigUtil.formatNumber(0));
                    mStoreCredit.setAmount(0);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cb_use_max_credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                store_credit_value.setText(ConfigUtil.formatNumber(mStoreCredit.getBalance()));
            }
        });

        bt_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutPayment payment = mCheckoutListController.createPaymentMethod();
                payment.setTitle(getContext().getString(R.string.plugin_store_credit));
                payment.setCode(STORE_CREDIT_PAYMENT_CODE);
                payment.setIsNotEnableEditValue(true);
                payment.setPaylater("0");
                payment.setReferenceNumber("0");
                payment.setIsDefault("0");
                payment.setIsReferenceNumber("0");
                payment.setType("0");
                mCheckoutListController.onAddPaymentMethod(payment);
            }
        });

        rl_remove_store_credit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                store_credit_value.setText(ConfigUtil.formatNumber(0));
                mStoreCredit.setAmount(0);
                // TODO: remove payment
            }
        });
    }

    @Override
    public void bindItem(StoreCredit item) {
        super.bindItem(item);
        mStoreCredit = item;
        tv_store_credit.setText(getContext().getString(R.string.plugin_store_credit_title, ConfigUtil.formatPrice(item.getBalance())));
        store_credit_value.setText(ConfigUtil.formatNumber(item.getBalance()));
    }
}
