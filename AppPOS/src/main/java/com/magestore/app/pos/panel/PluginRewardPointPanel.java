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
import android.widget.TextView;

import com.magestore.app.lib.model.plugins.RewardPoint;
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

public class PluginRewardPointPanel extends AbstractDetailPanel<RewardPoint> {
    CheckoutListController mCheckoutListController;
    RewardPoint mRewardPoint;
    EditText reward_point_value;
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
        reward_point_value = (EditText) findViewById(R.id.reward_point_value);
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
        tv_reward_point.setText(getContext().getString(R.string.plugin_reward_point_title, ConfigUtil.formatNumber(item.getBalance())));
        reward_point_value.setText(ConfigUtil.formatNumber(item.getBalance()));

        reward_point_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String point = reward_point_value.getText().toString().trim();
                if (!StringUtil.isNullOrEmpty(point)) {
                    int point_value;
                    try {
                        point_value = Integer.parseInt(point);
                    } catch (Exception e) {
                        point_value = 0;
                    }
                    mRewardPoint.setAmount(point_value);
                } else {
                    reward_point_value.setText("0");
                    mRewardPoint.setAmount(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cb_use_max_credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
                if (check) {
                    reward_point_value.setText(ConfigUtil.formatNumber(mRewardPoint.getBalance()));
                }
            }
        });

        bt_apply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String quote_id = mCheckoutListController.getSelectedItem().getQuoteId();
                if (mRewardPoint != null) {
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
                reward_point_value.setText(ConfigUtil.formatNumber(0));
                String quote_id = mCheckoutListController.getSelectedItem().getQuoteId();
                if (mRewardPoint != null) {
                    mRewardPoint.setQuoteId(quote_id);
                    mRewardPoint.setAmount(0);
                    mCheckoutListController.doInputApplyRewardPoint(mRewardPoint);
                } else {
                    mRewardPoint = mCheckoutListController.createRewardPoint();
                    mRewardPoint.setAmount(0);
                    mRewardPoint.setQuoteId(quote_id);
                    mCheckoutListController.doInputApplyRewardPoint(mRewardPoint);
                }
            }
        });
    }
}
