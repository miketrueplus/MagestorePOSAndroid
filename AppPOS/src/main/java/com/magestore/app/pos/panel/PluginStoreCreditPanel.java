package com.magestore.app.pos.panel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.magestore.app.lib.model.checkout.CheckoutPayment;
import com.magestore.app.lib.model.plugins.StoreCredit;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.pos.R;
import com.magestore.app.pos.controller.CheckoutListController;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;
import com.magestore.app.view.EditTextFloat;

/**
 * Created by Johan on 4/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PluginStoreCreditPanel extends AbstractDetailPanel<StoreCredit> {
    public static String STORE_CREDIT_PAYMENT_CODE = "storecredit";
    CheckoutListController mCheckoutListController;
    StoreCredit mStoreCredit;
    EditTextFloat store_credit_value;
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
        store_credit_value = (EditTextFloat) findViewById(R.id.store_credit_value);
        cb_use_max_credit = (CheckBox) findViewById(R.id.cb_use_max_credit);
        bt_apply = (Button) findViewById(R.id.bt_apply);
        bt_apply.setBackground(getResources().getDrawable(R.drawable.backgound_buton_apply_disable));

        rl_remove_store_credit = (RelativeLayout) findViewById(R.id.rl_remove_store_credit);

        store_credit_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float credit_value = store_credit_value.getValueFloat();
                if (credit_value == 0) {
                    disableApply(false);
                    if(mStoreCredit == null){mStoreCredit = mCheckoutListController.createStoreCredit();}
                    mStoreCredit.setAmount(0);
                } else {
                    disableApply(true);
                    if (credit_value < mStoreCredit.getMaxAmount()) {
                        cb_use_max_credit.setChecked(false);
                        mStoreCredit.setAmount(credit_value);
                    } else {
                        cb_use_max_credit.setChecked(true);
                        mStoreCredit.setAmount(mStoreCredit.getMaxAmount());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cb_use_max_credit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cb_use_max_credit.isChecked()) {
                    cb_use_max_credit.setChecked(false);
                } else {
                    cb_use_max_credit.setChecked(true);
                    store_credit_value.setText(ConfigUtil.formatNumber(mStoreCredit.getMaxAmount()));
                }
            }
        });

        bt_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckoutPayment payment = mCheckoutListController.createPaymentMethod();
                payment.setTitle(getContext().getString(R.string.plugin_store_credit));
                payment.setCode(STORE_CREDIT_PAYMENT_CODE);
                payment.setAmount(mStoreCredit.getAmount());
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
                mCheckoutListController.removePaymentStoreCredit();
            }
        });
    }

    private void disableApply(boolean isEnable) {
        bt_apply.setTextColor(isEnable ? ContextCompat.getColor(getContext(), R.color.white) : ContextCompat.getColor(getContext(), R.color.text_color));
        bt_apply.setEnabled(isEnable);
        bt_apply.setBackground(isEnable ? ContextCompat.getDrawable(getContext(), R.drawable.backgound_buton_apply_enable) : ContextCompat.getDrawable(getContext(), R.drawable.backgound_buton_apply_disable));
    }

    @Override
    public void bindItem(StoreCredit item) {
        super.bindItem(item);
        mStoreCredit = item;
        mStoreCredit.setMaxAmount(mStoreCredit.getBalance());
        tv_store_credit.setText(getContext().getString(R.string.plugin_store_credit_title, ConfigUtil.formatPrice(item.getBalance())));
        store_credit_value.setText(ConfigUtil.formatNumber(item.getBalance()));
    }

    public void updateMaxAmountStoreCredit(float total) {
        if (mStoreCredit != null) {
            mStoreCredit.setMaxAmount(total);
            store_credit_value.setText(ConfigUtil.formatNumber(mStoreCredit.getMaxAmount()));
        }
    }
}
