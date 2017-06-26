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

import com.magestore.app.lib.model.plugins.RewardPoint;
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

public class PluginRewardPointPanel extends AbstractDetailPanel<RewardPoint> {
    CheckoutListController mCheckoutListController;
    RewardPoint mRewardPoint;
    EditTextFloat reward_point_value;
    CheckBox cb_use_max_credit;
    Button bt_apply;
    RelativeLayout rl_remove_reward_point;
    TextView tv_reward_point;

    public void setCheckoutListController(CheckoutListController mCheckoutListController) {
        this.mCheckoutListController = mCheckoutListController;
    }

    public PluginRewardPointPanel(Context context) {
        super(context);
    }

    public PluginRewardPointPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PluginRewardPointPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initLayout() {
        super.initLayout();
        tv_reward_point = (TextView) findViewById(R.id.tv_reward_point);
        tv_reward_point.setText(getContext().getString(R.string.plugin_reward_point_title, "0"));
        reward_point_value = (EditTextFloat) findViewById(R.id.reward_point_value);
        cb_use_max_credit = (CheckBox) findViewById(R.id.cb_use_max_credit);
        bt_apply = (Button) findViewById(R.id.bt_apply);
        bt_apply.setBackground(getResources().getDrawable(R.drawable.backgound_buton_apply_enable));
        bt_apply.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        rl_remove_reward_point = (RelativeLayout) findViewById(R.id.rl_remove_reward_point);
    }

    @Override
    public void bindItem(RewardPoint item) {
        super.bindItem(item);
        mRewardPoint = item;
        if (mRewardPoint.getAmount() > 0) {
            int balance = mRewardPoint.getBalance() - mRewardPoint.getAmount();
            tv_reward_point.setText(getContext().getString(R.string.plugin_reward_point_title, ConfigUtil.formatNumber(balance)));
        } else {
            tv_reward_point.setText(getContext().getString(R.string.plugin_reward_point_title, ConfigUtil.formatNumber(mRewardPoint.getBalance())));
        }
        actionChangeRewardPoint(mRewardPoint);
        reward_point_value.setText(mRewardPoint.getAmount() > 0 ? ConfigUtil.formatNumber(mRewardPoint.getAmount()) : ConfigUtil.formatNumber(mRewardPoint.getMaxPoints()));

        cb_use_max_credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!cb_use_max_credit.isChecked()) {
                    cb_use_max_credit.setChecked(false);
                } else {
                    cb_use_max_credit.setChecked(true);
                    reward_point_value.setText(ConfigUtil.formatNumber(mRewardPoint.getMaxPoints()));
                }
            }
        });

        bt_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote_id = mCheckoutListController.getSelectedItem().getQuoteId();
                if (mRewardPoint != null) {
                    String point = reward_point_value.getText().toString().trim();
                    if (!StringUtil.isNullOrEmpty(point)) {
                        int point_value;
                        try {
                            point_value = Integer.parseInt(point);
                        } catch (Exception e) {
                            point_value = 0;
                        }
                        mRewardPoint.setAmount(point_value);
                    }
                    mRewardPoint.setQuoteId(quote_id);
                    mCheckoutListController.doInputApplyRewardPoint(mRewardPoint);
                } else {
                    mRewardPoint = mCheckoutListController.createRewardPoint();
                    mRewardPoint.setAmount(0);
                    mRewardPoint.setQuoteId(quote_id);
                    String point = reward_point_value.getText().toString().trim();
                    if (!StringUtil.isNullOrEmpty(point)) {
                        int point_value;
                        try {
                            point_value = Integer.parseInt(point);
                        } catch (Exception e) {
                            point_value = 0;
                        }
                        mRewardPoint.setAmount(point_value);
                    }
                    mCheckoutListController.doInputApplyRewardPoint(mRewardPoint);
                }
            }
        });

        rl_remove_reward_point.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote_id = mCheckoutListController.getSelectedItem().getQuoteId();
                if (mRewardPoint != null) {
                    reward_point_value.setText(ConfigUtil.formatNumber(mRewardPoint.getMaxPoints()));
                    mRewardPoint.setQuoteId(quote_id);
                    mRewardPoint.setAmount(0);
                    mCheckoutListController.doInputApplyRewardPoint(mRewardPoint);
                } else {
                    reward_point_value.setText(ConfigUtil.formatNumber(mRewardPoint.getMaxPoints()));
                    mRewardPoint = mCheckoutListController.createRewardPoint();
                    mRewardPoint.setAmount(0);
                    mRewardPoint.setQuoteId(quote_id);
                    mCheckoutListController.doInputApplyRewardPoint(mRewardPoint);
                }
            }
        });
    }

    private void actionChangeRewardPoint(final RewardPoint item) {
        reward_point_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int point_value = reward_point_value.getValueInteger();
                if (point_value > mRewardPoint.getMaxPoints()) {
                    reward_point_value.setText(ConfigUtil.formatNumber(mRewardPoint.getMaxPoints()));
                    mRewardPoint.setAmount(mRewardPoint.getMaxPoints());
                    cb_use_max_credit.setChecked(true);
                } else {
                    if (point_value == 0) {
                        mRewardPoint.setAmount(point_value);
                        cb_use_max_credit.setChecked(false);
                    } else {
                        mRewardPoint.setAmount(point_value);
                        cb_use_max_credit.setChecked(point_value == mRewardPoint.getMaxPoints() ? true : false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void changeBalance(RewardPoint rewardPoint) {
        tv_reward_point.setText(getContext().getString(R.string.plugin_reward_point_title, ConfigUtil.formatNumber(rewardPoint.getBalance() - rewardPoint.getAmount())));
        reward_point_value.setText(ConfigUtil.formatNumber(rewardPoint.getAmount()));
    }

    public void resetPointValue() {
        reward_point_value.setText("0");
    }
}
